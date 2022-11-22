import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SmaLinkPage extends BasePage{
    public static final String loginName = "tim@jensales.com";
    public static final String loginPassword = "manuals";
    public static final String websiteLink = "https://smalink.com/";

    public SmaLinkPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.getWebsiteLink() + "Default.aspx?Page=Logon");
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@id=\"email\"]");
        this.waitForClickAbleXPath("//input[@id=\"password\"]");
        //Fill login Details
        this.sendKeysXPath("//input[@id=\"email\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"password\"]", this.getLoginPassword());
        //Click on login
        this.clickXPath("//button[@name=\"SubmitLogon_Content\"]");
    }

    @Override
    public void searchText(String text) {
        this.waitForPageLoad();
        //Wait for search bar
        this.waitForClickAbleXPath("//input[@name=\"txtSearchText\"]");
        //Type search text
        this.sendKeysXPath("//input[@name=\"txtSearchText\"]", text);
        //Click on search button
        this.clickXPath("//input[@class=\"SearchButton\"]");
    }

    @Override
    public ProductDetails getProductDetails() {
        this.waitForPageLoad();
        List<WebElement> elements = this.getDriver().findElements(By.xpath("//form[@name='frmItemSearchResults']//*"));
        if (elements.isEmpty())
        {
            return null;
        }
        this.waitForXPath("(//form[@name='frmItemSearchResults']//div[@class='Product'])[1]//*");
        elements = this.getDriver().findElements(By.xpath("(//form[@name='frmItemSearchResults']//div[@class='Product'])[1]//*"));
        ProductDetails productDetails = new ProductDetails();
        elements.forEach(webElement -> {
            if (webElement.getAttribute("class").contains("ItemDetailsLink")) {
                productDetails.setTitle(webElement.getText());
            }
            if (webElement.getAttribute("class").contains("IDItemNumber")) {
                productDetails.setPartNumber(webElement.getText());
            }
            if (webElement.getAttribute("class").contains("ItemSearchResults_Availability")) {
                productDetails.setStock(webElement.getText());
            }
            if (webElement.getAttribute("class").contains("PriceRef CustPrice")) {
                productDetails.setDealerPrice(webElement.getText());
            }
            if (webElement.getAttribute("class").contains("PriceRef msrp")) {
                productDetails.setListPrice(webElement.getText());
            }

        });
        productDetails.setVendor("SmaLink");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
