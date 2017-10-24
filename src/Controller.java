//Dependencies
import Crawlers.*;
import Utilities.Shutdown;


public class Controller {

    /*
     * Execute the algorithm.
     */
    public static void main(String[] args) {
        //purgeCollections();
        //System.exit(5);

        //initShutdownHook();

        Ticketmaster_Crawler tmCrawler = new Ticketmaster_Crawler();
        //StubHub_Crawler shCrawler = new StubHub_Crawler();

        tmCrawler.executeCrawler();
        //shCrawler.executeCrawler();
    }


    /*
     * Initialize shutdown hook.
     */
    private static void initShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }


    /*
     * Purge all of the database collections.
     */
    private static void purgeCollections() {
        Ticketmaster_Crawler tmCrawler = new Ticketmaster_Crawler();
        StubHub_Crawler shCrawler = new StubHub_Crawler();

        tmCrawler.purgeCollections();
        shCrawler.purgeCollections();
    }
}
