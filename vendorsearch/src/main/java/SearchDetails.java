import java.util.ArrayList;
import java.util.List;

public class SearchDetails {
    private String searchStr;
    private String searchTime;

    private List<ProductDetails> productDetails = new ArrayList<>();

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }
}
