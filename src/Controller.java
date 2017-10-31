//Dependencies
import Algorithm.Ticketmaster_Events;
import Crawlers.*;
import Utilities.Shutdown;


public class Controller {

    /*
     * Shutdown thread.
     */
    private static Shutdown shutdown = new Shutdown();


    /*
     * Execute the algorithm.
     */
    public static void main(String[] args) {
        //initShutdownHook();
        purgeCollections();

        Ticketmaster_Crawler tmCrawler = new Ticketmaster_Crawler();
        StubHub_Crawler shCrawler = new StubHub_Crawler();

        shCrawler.executeCrawler();
        tmCrawler.executeCrawler();

        Ticketmaster_Events tmEvents = new Ticketmaster_Events();
        tmEvents.executeAlgorithm();

        shutdown.success = true;
    }


    /*
     * Initialize shutdown hook.
     */
    private static void initShutdownHook() {
        Runtime.getRuntime().addShutdownHook(shutdown);
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
