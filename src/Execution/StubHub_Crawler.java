//Current package
package Execution;

//Java dependency
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;

//Project package dependency
import StubHubAPI.SearchAPI.Find_Events;
import StubHubAPI.SearchAPI.Find_Listings_For_Event;
import Helpers.Email;
import com.mongodb.*;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ASchieltz on 7/23/2017.
 */
public class StubHub_Crawler {

    public static MongoClient mongoClient = new MongoClient( "localhost" , 27017);
    public static DB db = mongoClient.getDB("StubHub");


    /*
     * Shutdown hook.
     */
    static class ShutdownHook extends Thread {

        public void run() {
            Email email = new Email();
            email.sendEmails("Program has terminated. Please restart.");
        }
    }


    /*
     * Execute the algorithm.
     */
    public static void main(String[] args) {
        try {
            long start1 = System.nanoTime();
            //loadEventsTable();
            long end1 = System.nanoTime();

            long start2 = System.nanoTime();
            //loadListingsTable();
            long end2 = System.nanoTime();

            long start3 = System.nanoTime();
            filterListings();
            long end3 = System.nanoTime();

            long duration1 = (end1-start1)/1000000000;
            long duration2 = (end2-start2)/1000000000;
            long duration3 = (end3-start3)/1000000000;

            long minutes1 = duration1/60;
            long seconds1 = duration1 - (minutes1*60);

            long minutes2 = duration2/60;
            long seconds2 = duration2 - (minutes2*60);

            long minutes3 = duration3/60;
            long seconds3 = duration3 - (minutes3*60);

            System.out.println("Loading Events took: " + minutes1 + " minutes, " + seconds1 + " seconds");
            System.out.println("Loading Listings took: " + minutes2 + " minutes, " + seconds2 + " seconds");
            System.out.println("Filtering Listings took: " + minutes3 + " minutes, " + seconds3 + " seconds");

            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        } catch (Exception e) {
            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        }
    }


    /*
     * Load the "Collected Events" table.
     */
    private static void loadEventsTable() {
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

        Find_Events findEvents = new Find_Events();
        findEvents.getRequestData(params);

        System.out.println("Total Events: " + findEvents.count);

        for (int i=500; i < findEvents.count; i += 500) {
            params.put("start", String.valueOf(i));
            findEvents.getRequestData(params);
            params.remove("start");
        }
    }

    /*
     * Load the "Collected Listings" table.
     */
    private static void loadListingsTable() {
        Find_Listings_For_Event findListings = new Find_Listings_For_Event();
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
    }

    /*
     / Filter listings to find profitable ones.
     */
    private static void filterListings() throws JSONException {
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
                    //Load json into Approved_Listings
                }
            }
        }
    }
}
