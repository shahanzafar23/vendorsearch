import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrapperPageFactory {
    public static BasePage getPage(String pageName) {
        switch (pageName) {
            case "AiProducts":
                return new AiProductsPage();
            case "APAir":
                return new APAirPage();
            case "HyCapacity":
                return new HyCapacityPage();
            case "PartsExress":
                return new PartsExressPage();
            case "Reliance":
                return new ReliancePage();
            case "Riley":
                return new RileyPage();
            case "SmaLink":
                return new SmaLinkPage();
            case "SpareX":
                return new SpareXPage();
            case "SteinerTractor":
                return new SteinerTractorPage();
            case "Stens":
                return new StensPage();
        }
        return null;
    }

    public static List<String> getAllAvailablePages() {
        return Arrays.asList("AiProducts", "APAir", "HyCapacity", "PartsExress", "Reliance", "Riley", "SmaLink", "SpareX", "SteinerTractor", "Stens", "All");
    }
}
