import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class TestScrap {
    public static void main(String []args) throws InterruptedException, IOException {

        /*BasePage page = new StensPage();
        page.setupDriver(false);
        page.login();
        System.out.println("Please enter");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        while(!s.isBlank() && !s.isEmpty()) {
            page.searchText(s);
            ProductDetails productDetails = page.getProductDetails();
            if(productDetails != null) {
                System.out.println(page.getClass().getName());
                System.out.println(productDetails.getPartNumber());
                System.out.println(productDetails.getDealerPrice());
            }
            System.out.println("Please enter");
            s = scanner.nextLine();
        }*/

        /*Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "src/main/resources/cleanMemory.bat"});

        ScrapperThread[] threads = new ScrapperThread[10];

        threads[0] = new ScrapperThread(new StensPage(), "8N3679C");
        //threads[1] = new ScrapperThread(new SmaLinkPage(), "TP-RE53073");
        //threads[0] = new ScrapperThread(new SpareXPage(), "8N3679C");
        //threads[2] = new ScrapperThread(new SteinerTractorPage(), "ABC065");
        //threads[0] = new ScrapperThread(new AiProductsPage(), "8N3679C");
        //threads[5] = new ScrapperThread(new APAirPage(), "205-390");
        //threads[6] = new ScrapperThread(new HyCapacityPage(), "RE53073");
        //threads[7] = new ScrapperThread(new PartsExressPage(), "RE53073");
        //threads[0] = new ScrapperThread(new ReliancePage(), "RE53073");
        //threads[9] = new ScrapperThread(new RileyPage(), "RE53073");

        for(int index = 0; index < 1 ; index++) {
            threads[index].start();
        }
        for(int index = 0; index < 1 ; index++) {
            threads[index].join();
        }

//        threads[0].start();
//        threads[0].join();
//
//        threads[1].start();
//        threads[1].join();
//
//        threads[2].start();
//        threads[2].join();
//
//        threads[3].start();
//        threads[3].join();
//
//        threads[4].start();
//        threads[4].join();
//
//        threads[5].start();
//        threads[5].join();
//
//        threads[6].start();
//        threads[6].join();
//
//        threads[7].start();
//        threads[7].join();
//
//        threads[8].start();
//        threads[8].join();


        for(int index = 0; index < 1 ; index++) {
            if(threads[index].getProductDetails() != null) {
                System.out.println(threads[index].getPage().getClass().getName());
                System.out.println(threads[index].getProductDetails().getPartNumber());
                System.out.println(threads[index].getProductDetails().getDealerPrice());
            }
        }*/
    }
}
