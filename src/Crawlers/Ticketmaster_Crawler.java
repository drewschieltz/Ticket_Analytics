//Current package
package Crawlers;

//Dependencies
import TicketmasterAPI.TM_Event_Search;
import com.mongodb.*;
import java.text.*;
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, String > params = new HashMap<String, String>();
        params.put("onsaleOnStartDate", df.format(calendar.getTime()));
        params.put("countryCode", "US");
        params.put("classificationName", "music");

        TM_Event_Search eventSearch = new TM_Event_Search();
        eventSearch.getRequestData(params);

        if (eventSearch.elements > 999) {
            params.put("size", "200");

            for (int i = 0; i < 4; i++) {
                params.put("page", Integer.toString(i));
                eventSearch.getRequestData(params);
            }

            params.put("page", "4");
            params.put("size", "199");
            eventSearch.getRequestData(params);
        } else {
            int pageSize = elementsPerPage(eventSearch);
            params.put("size", Integer.toString(pageSize));

            for (int i=0; i < eventSearch.pages || i == 0; i++) {
                params.put("page", Integer.toString(i));
                eventSearch.getRequestData(params);
            }
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
