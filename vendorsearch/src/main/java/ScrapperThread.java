public class ScrapperThread extends Thread{
    private BasePage page;
    private String searchText;
    private ProductDetails productDetails;

    public ScrapperThread(BasePage page, String searchStr) {
        super();
        this.page = page;
        this.searchText = searchStr;
    }

    public BasePage getPage() {
        return page;
    }

    @Override
    public void run() {
        try {
            page.setupDriver(false);
            page.login();
            page.searchText(this.searchText);
            this.productDetails = page.getProductDetails();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            page.tearDownDriver();
        }
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }
}
