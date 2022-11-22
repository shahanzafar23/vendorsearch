import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class APAirPage extends BasePage{
    public static final String loginName = "tim@jensales.com";
    public static final String loginPassword = "Magneto1!";
    public static final String websiteLink = "https://www.apairinc.com/";

    public APAirPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.getWebsiteLink() + "myaccount/signin.aspx");
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@id=\"mainContent_signInCon_txtEmail\"]");
        this.waitForClickAbleXPath("//input[@id=\"mainContent_signInCon_txtPass\"]");

        //Fill login details
        this.sendKeysXPath("//input[@id=\"mainContent_signInCon_txtEmail\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"mainContent_signInCon_txtPass\"]", this.getLoginPassword());
        //Click login button
        this.waitForClickAbleXPath("//input[@id='mainContent_signInCon_btnSignIn']");
        this.clickXPath("//input[@id='mainContent_signInCon_btnSignIn']");
    }

    @Override
    public void searchText(String text) {
        this.waitForPageLoad();
        this.waitForXPath("//input[@id='txtSkuSearch']");
        this.sendKeysXPath("//input[@id='txtSkuSearch']", text);
        this.waitForClickAbleXPath("//input[@id='btnSkuSearch']");
        this.clickXPath("//input[@id='btnSkuSearch']");
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        this.waitForPageLoad();
        Actions action = new Actions(this.getDriver());
        action.pause(Duration.ofSeconds(1)).perform();
        ProductDetails productDetails = new ProductDetails();
        List<WebElement> elementsProduct = this.getDriver().findElements(By.xpath("//div[@id='mainContent_prodList_UpdatePanel1']//*"));
        List<WebElement> elementsProductsList = this.getDriver().findElements(By.xpath("//div[contains(@class,'subContent')]//div[contains(@id,'mainContent_prodList_repItems_itemWrap')]//*"));
        if (elementsProduct.isEmpty() && elementsProductsList.isEmpty()) {
            return null;
        }
        List<WebElement> elements = elementsProduct;
        if(elementsProduct.isEmpty()) {
            this.waitForClickAbleXPath("(//div[contains(@class,'subContent')]//h3)[1]/a");
            this.clickXPath("(//div[contains(@class,'subContent')]//h3)[1]/a");
            this.waitForPageLoad();
            action.pause(Duration.ofSeconds(1)).perform();
            this.waitForXPath("//div[@id='mainContent_prodList_UpdatePanel1']//*");
            elements = this.getDriver().findElements(By.xpath("//div[@id='mainContent_prodList_UpdatePanel1']//*"));
        }

        this.waitForXPath("//span[contains(@id,'dealerPriceToggle')]");
        this.clickXPath("//span[contains(@id,'dealerPriceToggle')]");
        this.waitForXPath("(//div[@id='mainContent_prodList_UpdatePanel1']//h3)[1]");
        this.waitForXPath("//div[@id='mainContent_prodList_UpdatePanel1']//strong[contains(@class,'miniDesc')]");
        this.waitForXPath("//div[@id='mainContent_prodList_UpdatePanel1']//strong[contains(@class,'regPrice')]");
        this.waitForXPath("(//div[@id='mainContent_prodList_UpdatePanel1']//strong[contains(@class,'itemPrice')])[2]");

        productDetails.setTitle(this.getTextForXPath("(//div[contains(@class,'prodTopInfo')]//h3)"));
        productDetails.setPartNumber(this.getTextForXPath("//div[@id='mainContent_prodList_UpdatePanel1']//strong[contains(@class,'miniDesc')]"));
        productDetails.setListPrice(this.getTextForXPath("//div[@id='mainContent_prodList_UpdatePanel1']//strong[contains(@class,'regPrice')]"));
        productDetails.setDealerPrice(this.getTextForXPath("(//div[@id='mainContent_prodList_UpdatePanel1']//strong[contains(@class,'itemPrice')])[2]"));

        /*elements.forEach(webElement -> {
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("miniDesc")) {
                productDetails.setPartNumber(webElement.getText());
            }
            else if (webElement.getTagName().equals("h3")) {
                productDetails.setTitle(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("regPrice")) {
                productDetails.setListPrice(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("itemPrice")) {
                productDetails.setDealerPrice(webElement.getText());
            }
        });*/
        productDetails.setVendor("APAir");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
