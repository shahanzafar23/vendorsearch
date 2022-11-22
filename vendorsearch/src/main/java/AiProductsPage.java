import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class AiProductsPage extends BasePage{
    public static final String loginName = "mn0625";
    public static final String loginPassword = "manuals";
    public static final String websiteLink = "https://www.aiproducts.com/";
    private final String loginUrl = "https://" + loginName + ":"+ loginPassword + "@www.aiproducts.com/";
    private final String resellerWebsite = "https://www.allpartsstore.com/index.htm?CustomerNumber=MN0625";

    public AiProductsPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.resellerWebsite);
        this.waitForPageLoad();
    }

    @Override
    public void searchText(String text) {
        this.openPage(this.resellerWebsite);
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@name='TextSearch']");
        this.sendKeysXPath("//input[@name='TextSearch']", text);
        this.getDriver().findElement(By.xpath("//input[@name='TextSearch']")).sendKeys(Keys.ENTER);
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        ProductDetails productDetails = new ProductDetails();
        List<WebElement> elements = this.getDriver().findElements(By.xpath("//div[contains(@class,'itemrow')]//*"));
        List<WebElement> elements1 = this.getDriver().findElements(By.xpath("//span[contains(@class,'partNumber')]//*"));
        if(elements.isEmpty() && elements1.isEmpty()){
            return null;
        }
        if(!elements.isEmpty()) {
            this.waitForClickAbleXPath("(//div[contains(@class,'itemrow')]//div[contains(@class,'itembox')])[1]//a");
            this.clickXPath("(//div[contains(@class,'itemrow')]//div[contains(@class,'itembox')])[1]//a");
            this.waitForPageLoad();
        }
        this.waitForXPath("//span[contains(@class,'partNumber')]");
        productDetails.setPartNumber(this.getTextForXPath("//span[contains(@class,'partNumber')]"));
        this.waitForXPath("//div[contains(@id,'partDescription')]");
        productDetails.setTitle(this.getTextForXPath("//div[contains(@id,'partDescription')]"));
        List<WebElement> stockElements = this.getDriver().findElements(By.xpath("(//span[contains(@class,'partMessage')])[1]//img"));
        if(!stockElements.isEmpty()) {
            String src = this.getDriver().findElement(By.xpath("(//span[contains(@class,'partMessage')])[1]//img")).getAttribute("src");
            if (!src.isBlank()) {
                if (src.equalsIgnoreCase("https://www.allpartsstore.com/images/notAvailable.png")) {
                    productDetails.setStock("Out of Stock");
                } else {
                    productDetails.setStock("In Stock");
                }
            }
        }
        else {
            productDetails.setStock("Ship directly from factory");
        }
        if(!productDetails.getPartNumber().isBlank()) {
            this.openPage(this.loginUrl + "catalog/Available.htm?ItemNumber=" + productDetails.getPartNumber());
            this.waitForPageLoad();
            Actions action = new Actions(this.getDriver());
            action.pause(Duration.ofSeconds(2)).perform();
            productDetails.setDealerPrice(this.getTextForXPath("((//tr[@class='RowSpacing'])[4]//td)[3]"));
            productDetails.setListPrice(this.getTextForXPath("((//tr[@class='RowSpacing'])[7]//td)[3]"));
            String stockDetails = "";
            for (int index = 10; index < 18; index++) {
                stockDetails += this.getTextForXPath("(//tr[@class='RowSpacing'])[" + index + "]");
                stockDetails += "\n";
            }
            productDetails.setStockDetails(stockDetails);
            productDetails.setVendor("AiProducts");
            productDetails.setLink(this.getDriver().getCurrentUrl());
            if (!productDetails.getDealerPrice().contains("$")) {
                return null;
            }
        }
        return productDetails;
    }
}
