//Current package
package Execution;

//Dependencies
import TicketmasterAPI.DiscoveryAPI.TM_Event_Search;
import com.mongodb.*;
import java.util.*;


public class Ticketmaster_Crawler extends Crawler {

    /*
    * Ticketmaster database.
    */
    private static DB db = mongoClient().getDB("Ticketmaster");


    /*
     * Execute the Ticketmaster crawler.
     */
    protected void executeCrawler() {
        try {
            purgeCollections();
            long[] duration1 = loadEventsTable();
            System.out.println("Loading " + duration1[0] + " Events took: " + duration1[1] + " minutes, " + duration1[2] + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * Purge the databases.
    */
    protected void purgeCollections() {
        BasicDBObject basicDBObj = new BasicDBObject();

        DBCollection dbColl = db.getCollection("Collected_Events");
        dbColl.remove(basicDBObj);

        System.out.println();
        System.out.println("TICKETMASTER DATABASE COLLECTIONS CLEARED!!!!!");
        System.out.println();
    }


    /*
     * Load the events table.
     */
    private static long[] loadEventsTable() {
        long start = System.nanoTime();

        Map<String, String > params = new HashMap<String, String>();
        params.put("countryCode", "US");
        params.put("classificationName", "music");

        params.put("onsaleOnStartDate", "2017-10-31");
        params.put("size", "200");

        TM_Event_Search eventSearch = new TM_Event_Search();
        eventSearch.getRequestData(params);

        for (int i=2; i <= eventSearch.pages; i++) {
            params.put("page", Integer.toString(i));
            eventSearch.getRequestData(params);
            params.remove("start");
        }

        int elements = eventSearch.elements;
        if (elements > 1000) {
            elements = 1000;
        }

        long end = System.nanoTime();

        return timeTracker(elements, start, end);
    }
}
