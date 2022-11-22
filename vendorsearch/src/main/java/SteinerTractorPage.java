import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SteinerTractorPage extends BasePage {
    public static final String loginName = "tim@jensales.com";
    public static final String loginPassword = "56007!";
    public static final String websiteLink = "https://www.steinertractor.com/";

    public SteinerTractorPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.getWebsiteLink() + "e4Login?returnurl=%2f");
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//div[@id=\"close-banner\"]");
        Actions action = new Actions(this.getDriver());
        action.moveToElement(this.getDriver().findElement(By.id("close-banner"))).click().perform();
        this.waitForClickAbleXPath("//div[@class=\"login-button\"]");
        this.clickXPath("//div[@class=\"login-button\"]");
        //Wait for input fields
        this.waitForClickAbleXPath("//input[@id=\"dnn_ctr485_Login_Hub_Login_Hub_txtUsername\"]");
        this.waitForClickAbleXPath("//input[@id=\"dnn_ctr485_Login_Hub_Login_Hub_txtPassword\"]");
        //Send login credentials
        this.sendKeysXPath("//input[@id=\"dnn_ctr485_Login_Hub_Login_Hub_txtUsername\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"dnn_ctr485_Login_Hub_Login_Hub_txtPassword\"]", this.getLoginPassword());
        //Click login
        this.waitForClickAbleXPath("//a[@id=\"dnn_ctr485_Login_Hub_Login_Hub_cmdLogin\"]");
        this.clickXPath("//a[@id=\"dnn_ctr485_Login_Hub_Login_Hub_cmdLogin\"]");
        this.waitForAjaxToFinish();
        this.openPage("https://antique-tractor-parts.steinertractor.com/search?w=" + "");
        this.waitForClickAbleXPath("//div[@id=\"close-banner\"]");
        action.moveToElement(this.getDriver().findElement(By.id("close-banner"))).click().perform();
    }

    @Override
    public void searchText(String text) {
        this.waitForPageLoad();
        this.openPage("https://antique-tractor-parts.steinertractor.com/search?w=" + text);
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        this.waitForPageLoad();
        this.waitForAjaxToFinish();
        List<WebElement> elements = this.getDriver().findElements(By.xpath("//div[@class='row sli_container  ']//*"));
        if (elements.isEmpty()) {
            return null;
        }
        this.waitForClickAbleXPath("((//div[@class='row sli_container  ']//div[contains(@class, 'product')]))[1]//button");
        this.clickXPath("((//div[@class='row sli_container  ']//div[contains(@class, 'product')]))[1]//button");
        this.waitForAjaxToFinish();
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//div[@class='single_product_desc']");
        this.waitForXPath("//section[@class='single_product_details_area ']");
        ProductDetails productDetails = new ProductDetails();
        this.waitForXPathAll("(//div[@class='single_product_desc']//div)[1]//*");
        Actions action = new Actions(this.getDriver());
        action.pause(Duration.ofSeconds(2)).perform();
        this.waitForClickAbleXPath("//div[@class='single_product_desc']//h4[contains(@class,'title')]");
        this.waitForClickAbleXPath("(//div[@class='single_product_desc']//span)[1]");
        this.waitForClickAbleXPath("(//div[@class='single_product_desc']//p[@id='productCode'])");
        this.waitForClickAbleXPath("(//div[@class='single_product_desc']//h4)[2]//span[contains(@class,'price')]");
        productDetails.setTitle(this.getTextForXPath("//div[@class='single_product_desc']//h4[contains(@class,'title')]"));
        productDetails.setStock(this.getTextForXPath("(//div[@class='single_product_desc']//span)[1]"));
        productDetails.setPartNumber(this.getTextForXPath("(//div[@class='single_product_desc']//p[@id='productCode'])"));
        String prices[] = this.getTextForXPath("(//div[@class='single_product_desc']//h4)[2]//span[contains(@class,'price')]").split("\n");
        productDetails.setDealerPrice(prices[0]);
        if(prices.length > 1) {
            productDetails.setListPrice(prices[1]);
        }
        /*elements = this.getDriver().findElements(By.xpath("(//div[@class='single_product_desc']//div)[1]//*"));

        elements.forEach(webElement -> {
            if (webElement.getAttribute("id") != null && webElement.getAttribute("id").contains("productCode")) {
                productDetails.setPartNumber(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("title")) {
                productDetails.setTitle(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("text-success")) {
                productDetails.setStock(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("text-danger")) {
                productDetails.setStock(webElement.getText());
            }
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("volume-pricing")) {
                productDetails.setStockDetails(webElement.getText());
            }
        });
        List<String> prices = elements.stream().filter(webElement -> webElement.getAttribute("class").contains("price"))
                .collect(Collectors.toList()).stream()
                .map(webElement -> webElement.getText()).collect(Collectors.toList());
        productDetails.setDealerPrice(prices.get(0));*/
        productDetails.setVendor("SteinerTractor");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
