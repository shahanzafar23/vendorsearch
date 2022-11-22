import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SpareXPage extends BasePage {
    public static final String loginName = "acc6024";
    public static final String loginPassword = "main200";
    public static final String websiteLink = "https://us.sparex.com/";

    public SpareXPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.getWebsiteLink() + "customer/account/login");
        //this.openWebSite();
        this.waitForPageLoad();
        //Accept cookie button
        this.waitForXPath("//div[@class=\"truste-consent-track-class\"]");
        this.clickXPath("//button[@id=\"truste-consent-button\"]");
        Actions action = new Actions(this.getDriver());
        action.pause(Duration.ofSeconds(3)).perform();
        //Wait for input fields
        this.waitForClickAbleXPath("//input[@id=\"email\"]");
        this.waitForClickAbleXPath("//input[@id=\"pass\"]");
        //Send login credentials
        this.sendKeysXPath("//input[@id=\"email\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"pass\"]", this.getLoginPassword());
        action.pause(Duration.ofSeconds(3)).perform();
        //Click login
        this.waitForClickAbleXPath("//button[@id=\"send2\"]");
        this.clickXPath("//button[@id=\"send2\"]");
    }

    @Override
    public void searchText(String text) {
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@id='search']");
        this.sendKeysXPath("//input[@id='search']", Keys.CONTROL + "a");
        this.sendKeysXPath("//input[@id='search']", Keys.DELETE + "");
        this.sendKeysXPath("//input[@id='search']", text);
        this.waitForClickAbleXPath("//button[@class='action search button']");
        this.clickXPath("//button[@class='action search button']");
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        this.waitForPageLoad();
        List<WebElement> elementsMultipleProducts = this.getDriver().findElements(By.xpath("//div[@class='products wrapper category-products list products-list']//*"));
        List<WebElement> elementsSingleProduct = this.getDriver().findElements(By.xpath("//div[@class='product-view product-view-dealer ']//*"));
        if (elementsSingleProduct.isEmpty() && elementsMultipleProducts.isEmpty()) {
            return null;
        }
        if(!elementsMultipleProducts.isEmpty())
        {
            this.clickXPath("//div[@class='products wrapper category-products list products-list']//ul/li[1]//h2//a");
            this.waitForPageLoad();
        }
        List<WebElement> elements = this.getDriver().findElements(By.xpath("//div[@class='product-view product-view-dealer ']//*"));
        ProductDetails productDetails = new ProductDetails();
        elements.forEach(webElement -> {
            if (webElement.getAttribute("itemprop") != null && webElement.getAttribute("itemprop").contains("sku")) {
                productDetails.setPartNumber(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("product-name")) {
                productDetails.setTitle(webElement.findElement(By.tagName("h1")).getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("listscript listscript-two")) {
                productDetails.setDescription(webElement.getText());
            }
            if(webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("price")) {
                String[] prices = webElement.getText().toLowerCase().split("list");
                productDetails.setDealerPrice(prices[0]);
                if(prices.length > 1) {
                    productDetails.setListPrice(prices[1]);
                }
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("stock-wrap")) {
                List<WebElement> oos = webElement.findElements(By.className("oos"));
                if(oos.isEmpty()) {
                    productDetails.setStock(webElement.findElement(By.className("inst")).getText());
                }
                else {
                    productDetails.setStock(webElement.findElement(By.className("oos")).getText());
                }
            }
        });
        productDetails.setVendor("SpareX");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
