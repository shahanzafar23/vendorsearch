import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class HyCapacityPage extends BasePage{
    public static final String loginName = "6217";
    public static final String loginPassword = "Magneto1!";
    public static final String websiteLink = "https://www.hy-capacity.com/";

    public HyCapacityPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openWebSite();
        this.waitForPageLoad();
        //Wait for login button
        this.waitForClickAbleXPath("//a[@id='loginlink']");
        this.waitForPageLoad();
        Actions action = new Actions(this.getDriver());
        action.moveToElement(this.getDriver().findElement(By.id("loginlink"))).click().perform();
        //Wait for input fields
        this.waitForXPath("//input[@id=\"LoginModel_DealerCode\"]");
        this.waitForXPath("//input[@id=\"LoginModel_Password\"]");
        //Send login credentials
        this.sendKeysXPath("//input[@id=\"LoginModel_DealerCode\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"LoginModel_Password\"]", this.getLoginPassword());
        //Click login
        this.waitForClickAbleXPath("//a[@id=\"btnLogin\"]");
        this.clickXPath("//a[@id=\"btnLogin\"]");
        this.waitForPageLoad();
    }

    @Override
    public void searchText(String text) {
        this.waitForAjaxToFinish();
        Actions action = new Actions(this.getDriver());
        action.pause(Duration.ofSeconds(2)).perform();
        this.openPage(this.getWebsiteLink() + "search/?k=1bg&searchkeyword=" + text);
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        ProductDetails productDetails = new ProductDetails();
        this.waitForPageLoad();
        List<WebElement> elements = this.getDriver().findElements(By.xpath("((//div[@class='product-list'])//div)[1]//*"));
        if (elements.isEmpty()) {
            return null;
        }
        elements.forEach(webElement -> {
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("item-number")) {
                productDetails.setPartNumber(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("item-title")) {
                productDetails.setTitle(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("price-dealer")) {
                productDetails.setDealerPrice(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("price-list")) {
                productDetails.setListPrice(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("stock-status")) {
                productDetails.setStock(webElement.getText());
            }
        });
        productDetails.setVendor("HyCapacity");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
