import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RileyPage extends BasePage{
    public static final String loginName = "jen200";
    public static final String loginPassword = "bert07";
    public static final String websiteLink = "https://www.rileytractorparts.com/";

    public RileyPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openWebSite();
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@id=\"txtUserID\"]");
        this.waitForClickAbleXPath("//input[@id=\"txtPassword\"]");

        //Fill login details
        this.sendKeysXPath("//input[@id=\"txtUserID\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"txtPassword\"]", this.getLoginPassword());
        //Click login button
        this.waitForClickAbleXPath("//input[@id='btnaccept']");
        this.clickXPath("//input[@id='btnaccept']");

    }

    @Override
    public void searchText(String text) {
        this.waitForPageLoad();
        this.waitForClickAbleXPath("//input[@id='SrchBox']");
        this.sendKeysXPath("//input[@id='SrchBox']", text);
        this.waitForClickAbleXPath("//input[@id='imgbutton']");
        this.clickXPath("//input[@id='imgbutton']");
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        this.waitForPageLoad();
        this.waitForXPath("//table[@id='Table1']");
        List<WebElement> elements = this.getDriver().findElements(By.xpath("//table[@id='dgItems']//*"));
        if (elements.isEmpty()) {
            return null;
        }
        this.waitForXPath("(//table[@id='dgItems']//tr[@class='GridItems'])[1]");
        ProductDetails productDetails = new ProductDetails();
        productDetails.setTitle(this.getTextForXPath("((//table[@id='dgItems']//tr[@class='GridItems'])[1]/td)[3]"));
        productDetails.setPartNumber(this.getTextForXPath("((//table[@id='dgItems']//tr[@class='GridItems'])[1]/td)[4]"));
        productDetails.setDealerPrice(this.getTextForXPath("((//table[@id='dgItems']//tr[@class='GridItems'])[1]/td)[5]"));
        productDetails.setVendor("Riley");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
