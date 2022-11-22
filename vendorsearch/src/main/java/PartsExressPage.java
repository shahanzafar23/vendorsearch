import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PartsExressPage extends BasePage {
    public static final String loginName = "tim@jensales.com";
    public static final String loginPassword = "tim";
    public static final String websiteLink = "https://parts-exp.com/";

    public PartsExressPage() {
        super(loginName, loginPassword, websiteLink);
    }

    @Override
    public void login() {
        this.openPage(this.getWebsiteLink() + "Account/Login");
        this.waitForPageLoad();
        //Wait for input fields
        this.waitForClickAbleXPath("//input[@id=\"Email\"]");
        this.waitForClickAbleXPath("//input[@id=\"Password\"]");
        //Send login credentials
        this.sendKeysXPath("//input[@id=\"Email\"]", this.getLoginName());
        this.sendKeysXPath("//input[@id=\"Password\"]", this.getLoginPassword());
        //Click login
        this.waitForClickAbleXPath("//input[@value='Log in']");
        this.clickXPath("//input[@value='Log in']");
    }

    @Override
    public void searchText(String text) {
        this.openWebSite();
        this.waitForPageLoad();
        this.waitForXPath("//input[@id='PartNumber']");
        this.sendKeysXPath("//input[@id='PartNumber']", text);
        this.getDriver().findElement(By.xpath("//form[@action='/Part/SearchByNumber']")).submit();
        this.waitForPageLoad();
    }

    @Override
    public ProductDetails getProductDetails() {
        ProductDetails productDetails = new ProductDetails();
        this.waitForPageLoad();
        List<WebElement> elements = this.getDriver().findElements(By.xpath("//div[@class='partlisting container-fluid']//*"));
        if (elements.isEmpty()) {
            return null;
        }
        productDetails.setTitle(this.getTextForXPath("(//div[@class='partlisting container-fluid']//div)[1]//h3"));
        productDetails.setPartNumber(this.getTextForXPath("((//div[@class='partlisting container-fluid']//div)[1]//table//td)[1]"));
        productDetails.setListPrice(this.getDriver().findElement(By.xpath("//div[contains(@class,'pricebox')]//div//div")).getText());
        if(!productDetails.getPartNumber().isBlank()) {
            this.openPage(this.getWebsiteLink() + "Part/Availability/" + productDetails.getPartNumber() + "/?quantity=1");
            this.waitForPageLoad();
            String prices[] = this.getDriver().findElement(By.xpath("//div[@class='panel-body']//div//div")).getText().split("\n");
            productDetails.setDealerPrice(prices[0]);
            if (prices.length > 1) {
                productDetails.setListPrice(prices[1]);
            }
            String[] stocks = this.getDriver().findElement(By.xpath("((//div[@class='panel panel-default'])[2])//div[@class='list-group']")).getText().trim().split("\n");
            productDetails.setStock(stocks[0]);
            if(stocks.length > 1){
                String stockDetail = "";
                for(int i = 1; i < stocks.length; i++){
                    stockDetail += stocks[i] + " ";
                }
                productDetails.setStockDetails(stockDetail);
            }
        }
        else {
            productDetails.setStock("N/A");
        }
        productDetails.setVendor("PartsExress");
        productDetails.setLink(this.getDriver().getCurrentUrl());
        return productDetails;
    }
}
