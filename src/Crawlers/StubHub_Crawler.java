//Current package
package Crawlers;

//Dependencies
import java.io.*;
import java.util.*;
import java.net.*;
import StubHubAPI.*;
import com.mongodb.*;


public class StubHub_Crawler extends Crawler {

    /*
     * StubHub database.
     */
    private static DB db = mongoClient().getDB("StubHub");


    /*
     * Execute the StubHub crawler.
     */
    public void executeCrawler() {
        try {
            long[] duration1 = loadEventsTable();
            //long[] duration2 = loadListingsTable();

            System.out.println("Loading " + duration1[0] + " Events took: " + duration1[1] + " minutes, " + duration1[2] + " seconds");
            //System.out.println("Loading " + duration2[0] + " Listings took: " + duration2[1] + " minutes, " + duration2[2] + " seconds");
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

        dbColl = db.getCollection("Collected_Listings");
        dbColl.remove(basicDBObj);

        dbColl = db.getCollection("Approved_Listings");
        dbColl.remove(basicDBObj);

        System.out.println();
        System.out.println("STUBHUB DATABASE COLLECTIONS CLEARED!!!!!");
        System.out.println();
    }


    /*
     * Load the "Collected_Events" table.
     */
    private static long[] loadEventsTable() {
        long start = System.nanoTime();

        Map<String, String> params = new HashMap<String, String>();
        String geoName = null;
        String catName = "music";

        try {
            catName = URLEncoder.encode("music |concert", "UTF-8");
            geoName = URLEncoder.encode("United States", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("One or more parameters could not be encoded. Default values will be queried.");
            System.out.println();
        }

        if (geoName != null) {
            params.put("geoName", geoName);
        }

        params.put("categoryName", catName);
        params.put("status", "Active");
        params.put("minAvailableTickets", "100");
        params.put("limit", "500");

        SH_Find_Events findEvents = new SH_Find_Events();
        findEvents.getRequestData(params);

        for (int i=500; i < findEvents.count; i += 500) {
            params.put("start", String.valueOf(i));
            findEvents.getRequestData(params);
            params.remove("start");
        }

        long end = System.nanoTime();
        return timeTracker(findEvents.count, start, end);
    }


    /*
     * Load the "Collected_Listings" table.
     */
    private static long[] loadListingsTable() {
        long start = System.nanoTime();

        SH_Find_Listings findListings = new SH_Find_Listings();
        Map<String, String> params = new HashMap<String, String>();

        //Load parameters
        params.put("pricemax", "400");
        params.put("quantity", "2");
        params.put("deliverytypelist", "2,5");

        DBCollection collection = db.getCollection("Collected_Events");
        DBCursor cursor = collection.find();

        int index = 1;
        while (cursor.hasNext()) {
            System.out.println("Loading Event " + index + " of " + collection.count());
            DBObject obj = cursor.next();
            String id = obj.get("id").toString();

            findListings.getRequestData(id, params);
            index++;
        }

        long end = System.nanoTime();
        long count = db.getCollection("Collected_Listings").count();
        return timeTracker(count, start, end);
    }
}

