import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    private WebDriver driver;
    private String loginName;
    private String loginPassword;
    private String websiteLink;
    private WebDriverWait wait;

    public BasePage(String loginName, String loginPassword, String websiteLink) {
        this.loginName = loginName;
        this.loginPassword = loginPassword;
        this.websiteLink = websiteLink;
    }

    public void setupDriver(boolean headless) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //"--headless",
        if(headless) {
            options.addArguments("--headless", "--disable-gpu", "--window-size=1700,1200", "--ignore-certificate-errors");
        }
        else {
            options.addArguments("--disable-gpu", "--window-size=1700,1200", "--ignore-certificate-errors");
        }
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        WebDriver driver = new ChromeDriver(options);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void tearDownDriver() {
        try {
            if(this.driver != null) {
                this.driver.quit();
            }
        }catch (Exception e){
            this.driver.quit();
        }
    }

    public boolean isBrowserClosed()
    {
        boolean isClosed = false;
        try {
            driver.getTitle();
        } catch(Exception ex) {
            isClosed = true;
        }
        return isClosed;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void openWebSite() {
        this.driver.get(this.websiteLink);
    }

    public void openPage(String page) {
        this.driver.get(page);
    }

    public void waitForXPath(String xPath) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xPath)));
    }

    public void waitForXPathAll(String xPath) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xPath)));
    }

    public void waitForAngularToFinish(){
        ExpectedCondition<Boolean> angularload = driver -> (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.angular != null) && (angular.element(document).injector() != null) && (angular.element(document).injector().get('$http').pendingRequests.length === 0);");
        wait.until(angularload);
    }

    public void waitForClickAbleXPath(String xPath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
    }

    public void waitForSelectAbleXPath(String xPath) {
        wait.until(ExpectedConditions.elementToBeSelected(By.xpath(xPath)));
    }

    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public void waitForAjaxToFinish() {
        ExpectedCondition<Boolean> jQueryLoad = driver -> (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
        wait.until(jQueryLoad);
    }

    public void waitForCaptchaFrameLoad(String xPath) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(xPath)));
    }

    public void clickXPath(String xPath) {
        driver.findElement(By.xpath(xPath)).click();
    }

    public String getTextForXPath(String xPath) {
        return driver.findElement(By.xpath(xPath)).getText();
    }

    public void sendKeysXPath(String xPath, String keys) {
        driver.findElement(By.xpath(xPath)).sendKeys(keys);
    }

    public abstract void login();
    public abstract void searchText(String text);
    public abstract ProductDetails getProductDetails();
}
