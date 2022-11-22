import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class ReliancePage extends BasePage{
    public static final String loginName = "18169";
    public static final String loginPassword = "manuals";
    public static final String websiteLink = "https://reliancepowerparts.com/";

    public ReliancePage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.getWebsiteLink() + "Accounts/Login?retUrl=%2FRelianceFrontPage");
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@id=\"login-box\"]");
        this.waitForClickAbleXPath("//input[@id=\"Password\"]");

        //Fill login details
        this.sendKeysXPath("//input[@id=\"login-box\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"Password\"]", this.getLoginPassword());
        //Click login button
        this.waitForClickAbleXPath("//button[@class='btn btn-lg btn-dark btn-block']");
        this.clickXPath("//button[@class='btn btn-lg btn-dark btn-block']");
    }

    @Override
    public void searchText(String text) {
        this.openWebSite();
        this.waitForPageLoad();
        this.waitForAjaxToFinish();
        this.waitForClickAbleXPath("//input[@id='searchfield1']");
        this.sendKeysXPath("//input[@id='searchfield1']", text);
        this.getDriver().findElement(By.xpath("//input[@id='searchfield1']")).sendKeys(Keys.ENTER);
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        //this.waitForXPath("(//form[@action='/Cart/Add']//li)//*");
        Actions action = new Actions(this.getDriver());
        action.pause(Duration.ofSeconds(1)).perform();
        List<WebElement> elements = this.getDriver().findElements(By.xpath("(//form[@action='/Cart/Add']//li)//*"));
        if (elements.isEmpty()) {
            return null;
        }
        this.waitForXPath("(//form[@action='/Cart/Add']//li)[1]");
        ProductDetails productDetails = new ProductDetails();
        elements = this.getDriver().findElements(By.xpath("(//form[@action='/Cart/Add']//li)[1]//*"));
        elements.forEach(webElement -> {
            if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("product-name")) {
                productDetails.setTitle(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("product-id")) {
                productDetails.setPartNumber(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("secondary-description")) {
                productDetails.setDescription(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("product-item-price")) {
                productDetails.setDealerPrice(webElement.getText());
            }
            else if (webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("availability-container")) {
                String[] split = webElement.getText().split("\n");
                productDetails.setStock(split[0]);
            }
        });
        productDetails.setVendor("Reliance");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
