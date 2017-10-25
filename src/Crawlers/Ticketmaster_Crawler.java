//Current package
package Crawlers;

//Dependencies
import TicketmasterAPI.TM_Event_Search;
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
    public void executeCrawler() {
        try {
            purgeCollections();
            long[] duration = loadEventsTable();

            System.out.println("Loading " + duration[0] + " Events took: " + duration[1] +
                                " minutes, " + duration[2] + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * Purge the databases.
    */
    public void purgeCollections() {
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
        params.put("onsaleOnStartDate", "2017-10-21");

        TM_Event_Search eventSearch = new TM_Event_Search();
        eventSearch.getRequestData(params);

        params.put("size", Integer.toString(elementsPerPage(eventSearch)));

        for (int i=0; i < eventSearch.pages || i == 0; i++) {
            params.put("page", Integer.toString(i));
            eventSearch.getRequestData(params);
        }

        long end = System.nanoTime();
        return timeTracker(eventSearch.elements, start, end);
    }


    /*
     * Determine the "size" parameter value.
     */
    private static int elementsPerPage(TM_Event_Search eventSearch) {
        for (int i = 200; true; i--) {
            if (eventSearch.elements % i == 0) {
                return i;
            }
        }
    }
}
