//Current package
package Execution;

//Dependencies
import Helpers.ShutdownHook;


public class Controller {

    /*
     * Execute the algorithm.
     */
    public static void main(String[] args) {
        initShutdownHook();

        StubHub_Crawler shCrawler = new StubHub_Crawler();
        shCrawler.executeCrawler();
    }


    /*
     * Initialize shutdown hook.
     */
    private static void initShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }
}
