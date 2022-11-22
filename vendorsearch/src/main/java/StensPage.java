import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class StensPage extends BasePage{
    public static final String loginName = "tim@jensales.com";
    public static final String loginPassword = "manuals";
    public static final String websiteLink = "https://www.stens.com/";

    public StensPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openWebSite();
        this.waitForPageLoad();

        this.waitForClickAbleXPath("//a[@class='header-profile-login-link header-profile-link']");
        this.clickXPath("//a[@class='header-profile-login-link header-profile-link']");
        //Wait for login form
        this.waitForXPath("//div[@class=\"login-register-login-form-controls\"]");
        //Wait for input fields
        this.waitForClickAbleXPath("//input[@id=\"login-email\"]");
        this.waitForClickAbleXPath("//input[@id=\"login-password\"]");
        //Fill login Details
        this.sendKeysXPath("//input[@id=\"login-email\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"login-password\"]", this.getLoginPassword());
        this.waitForClickAbleXPath("//button[@class=\"login-register-login-submit\"]");
        //Click on login
        this.clickXPath("//button[@class=\"login-register-login-submit\"]");
        this.waitForPageLoad();
    }

    @Override
    public void searchText(String text) {
        this.waitForClickAbleXPath("//input[@class='itemssearcher-input typeahead tt-input']");
        this.sendKeysXPath("//input[@class='itemssearcher-input typeahead tt-input']", text);
        this.clickXPath("//button[@class='site-search-button-submit']");
        this.waitForAjaxToFinish();
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        this.waitForPageLoad();
        if (!this.getDriver().findElements(By.className("facets-facet-browse-empty-items")).isEmpty())
        {
            return null;
        }
        List<WebElement> elements = this.getDriver().findElements(By.xpath("(//div[@class='facets-item-cell-list'])[1]//*"));
        ProductDetails productDetails = new ProductDetails();
        this.waitForXPathAll("(//div[@class='facets-item-cell-list'])[1]//*");
        /*Actions action = new Actions(this.getDriver());
        action.pause(Duration.ofSeconds(2)).perform();
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//span[contains(@class,'facets-item-cell-list-sku-value')])[1]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//h2[contains(@class,'facets-item-cell-list-title')])[1]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//div[contains(@class,'facets-item-cell-list-description-content')])[1]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//div[contains(@class,'item-cell-details')])[1]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//span[contains(@class,'product-line-stock-msg-in-text')])[1]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//div[contains(@class,'product-line-stock-msg-primary-text')])[1]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//ul[contains(@class,'product-views-price product-views-list-price')])[1]//li[2]");
        this.waitForXPath("((//div[@class='facets-item-cell-list'])[1]//ul[contains(@class,'product-views-price product-views-your-price')])[1]//li[2]");

        productDetails.setPartNumber(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//span[contains(@class,'facets-item-cell-list-sku-value')])[1]"));
        productDetails.setTitle(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//h2[contains(@class,'facets-item-cell-list-title')])[1]"));
        productDetails.setDescription(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//div[contains(@class,'facets-item-cell-list-description-content')])[1]"));
        productDetails.setQty(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//div[contains(@class,'item-cell-details')])[1]"));
        productDetails.setStock(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//span[contains(@class,'product-line-stock-msg-in-text')])[1]"));
        productDetails.setStockDetails(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//div[contains(@class,'product-line-stock-msg-primary-text')])[1]"));
        productDetails.setListPrice(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//ul[contains(@class,'product-views-price product-views-list-price')])[1]//li[2]"));
        productDetails.setDealerPrice(this.getTextForXPath("((//div[@class='facets-item-cell-list'])[1]//ul[contains(@class,'product-views-price product-views-your-price')])[1]//li[2]"));

*/        elements.forEach(webElement -> {
            if (webElement.getTagName().equals("ul") && webElement.getAttribute("class") != null && webElement.getAttribute("class").equals("product-views-price product-views-list-price")) {
                String priceSplit[] = webElement.getText().split("\n");
                if(priceSplit.length > 1) {
                    productDetails.setListPrice(priceSplit[1]);
                }
                else {
                    productDetails.setListPrice(webElement.getText());
                }
            }
            else if (webElement.getTagName().equals("ul") && webElement.getAttribute("class") != null && webElement.getAttribute("class").equals("product-views-price product-views-your-price")) {
                String priceSplit[] = webElement.getText().split("\n");
                if(priceSplit.length > 1) {
                    productDetails.setDealerPrice(priceSplit[1]);
                }
                else {
                    productDetails.setDealerPrice(webElement.getText());
                }
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("product-line-stock-msg-in")){
                productDetails.setStock(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("product-line-stock-msg-primary")) {
                productDetails.setStockDetails(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("facets-item-cell-list-title")) {
                productDetails.setTitle(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("facets-item-cell-list-sku")) {
                productDetails.setPartNumber(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("facets-item-cell-list-description-content")) {
                productDetails.setDescription(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getTagName().equals("ul") && webElement.getAttribute("class").contains("item-cell-info-list")) {
                productDetails.setQty(webElement.getText().replaceAll("\n"," , ").trim());
            }
        });
        productDetails.setVendor("Stens");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
