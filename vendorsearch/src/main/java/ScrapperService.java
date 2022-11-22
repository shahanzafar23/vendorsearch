import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScrapperService {
    private HashMap<String, BasePage> pages = new HashMap<>();
    private final Logger log = Logger.getLogger(ScrapperService.class.getName());

    public ScrapperService() {
        this.pages.put("AiProducts", new AiProductsPage());
        this.pages.put("APAir", new APAirPage());
        this.pages.put("HyCapacity", new HyCapacityPage());
        this.pages.put("PartsExress", new PartsExressPage());
        this.pages.put("Reliance", new ReliancePage());
        this.pages.put("Riley",new RileyPage());
        this.pages.put("SmaLink", new SmaLinkPage());
        this.pages.put("SpareX", new SpareXPage());
        this.pages.put("SteinerTractor", new SteinerTractorPage());
        this.pages.put("Stens", new StensPage());
    }

    public void quitAll() {
        try {
            this.pages.values().stream().forEach(basePage -> {
                basePage.tearDownDriver();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginToAll() {
        log.info("Logging in to all");
        try {
            this.pages.values().stream().forEach(basePage -> {
                if (basePage.getDriver() == null || basePage.isBrowserClosed()) {
                    basePage.setupDriver(false);
                }
                try {
                    basePage.login();
                }catch (Exception e){
                    log.error("Error in logging in");
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void login(String vendor) {
        log.info("Logging in to vendor " + vendor);
        try {
            BasePage basePage1 = this.pages.get(vendor);
            if (basePage1 != null) {
                if (basePage1.getDriver() == null || basePage1.isBrowserClosed()) {
                    basePage1.setupDriver(false);
                }
                basePage1.login();
                return;
            }
        } catch (Exception e) {
            log.error("Error in logging in");
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<ProductDetails> searchProduct(String productNumber, String vendor) {
        log.info("Searching the item " + productNumber + " from vendor " + vendor);
        if (!vendor.equalsIgnoreCase("all") && this.pages.get(vendor).getDriver() == null) {
            return null;
        }
        if (vendor.equalsIgnoreCase("all")) {
            if (this.pages.values().stream().allMatch(basePage -> basePage.getDriver() == null))
                return null;
        }
        List<ProductDetails> productDetails = new ArrayList<>();
        try {
            if (vendor.equalsIgnoreCase("all")) {
                this.pages.values().forEach(basePage -> {
                    if (basePage.getDriver() == null || basePage.isBrowserClosed()) {
                        basePage.setupDriver(false);
                        try {
                            basePage.login();
                        }catch (Exception e){
                            log.error("Error in logging in");
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    try {
                        basePage.searchText(productNumber);
                    }catch (Exception e){
                        log.error("Error in searching");
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                });
                this.pages.values().forEach(basePage -> {
                    try {
                        ProductDetails productDetails1 = basePage.getProductDetails();
                        if (productDetails1 != null) {
                            productDetails.add(productDetails1);
                        }
                    }catch (Exception e){
                        log.error("Error in getting products details");
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                });
            } else {
                BasePage basePage = this.pages.get(vendor);
                if (basePage.getDriver() == null || basePage.isBrowserClosed()) {
                    return null;
                }
                try {
                    basePage.searchText(productNumber);
                    ProductDetails productDetails1 = basePage.getProductDetails();
                    if (productDetails1 != null) {
                        productDetails.add(productDetails1);
                    }
                }catch (Exception e){
                    log.error("Error in searching getting products");
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
            return productDetails;
        } catch (Exception e) {
            log.error("Error");
            log.error(e.getMessage());
            e.printStackTrace();
            return productDetails;
        }
    }
}
