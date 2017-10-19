//Current package
package Execution;

//Dependencies
import java.io.*;
import java.util.*;
import java.net.*;
import StubHubAPI.SearchAPI.*;
import StubHubAPI.SH_HttpRequest;
import com.mongodb.*;
import org.json.*;


class StubHub_Crawler extends Crawler {

    /*
     * StubHub database.
     */
    private static DB db = mongoClient().getDB("StubHub");


    /*
     * Execute the StubHub crawler.
     */
    protected void executeCrawler() {
        //purgeCollections();
        //System.exit(1);

        try {
            long[] duration1 = loadEventsTable();
            long[] duration2 = loadListingsTable();
            long[] duration3 = filterListings();

            System.out.println("Loading " + duration1[0] + " Events took: " + duration1[1] + " minutes, " + duration1[2] + " seconds");
            System.out.println("Loading " + duration2[0] + " Listings took: " + duration2[1] + " minutes, " + duration2[2] + " seconds");
            System.out.println("Filtering Listings took: " + duration3[1] + " minutes, " + duration3[2]+ " seconds");
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


    /*
     / Filter listings to find profitable ones.
     */
    private static long[] filterListings() throws JSONException {
        long start = System.nanoTime();

        DBCollection collection = db.getCollection("Collected_Listings");
        DBCursor cursor = collection.find();

        for (int i = 1; cursor.hasNext(); i++) {
            System.out.println("Checking Listing " + i + " of " + collection.count());
            DBObject obj = cursor.next();

            if (obj.keySet().contains("faceValue")) {
                double faceValue;
                double currentPrice;

                JSONObject fvObj = new JSONObject(obj.get("faceValue").toString());
                faceValue = Double.parseDouble(fvObj.get("amount").toString());

                JSONObject cpObj = new JSONObject(obj.get("currentPrice").toString());
                currentPrice = Double.parseDouble(cpObj.get("amount").toString());

                if (faceValue < 400 & faceValue > currentPrice + 50) {
                    JSONObject json = new JSONObject(obj);

                    SH_HttpRequest httpRequest = new SH_HttpRequest();
                    httpRequest.loadIntoDB(json, "Approved_Listings");
                }
            }
        }

        long end = System.nanoTime();
        return timeTracker(0, start, end);
    }
}

