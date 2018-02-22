//Current package
package Crawlers;

//Dependencies
import java.io.*;
import java.util.*;
import java.net.*;
import StubHubAPI.*;
import com.mongodb.*;
import com.mongodb.util.JSON;
import jdk.internal.cmm.SystemResourcePressureImpl;
import org.json.JSONArray;
import org.json.JSONObject;


public class StubHub_Crawler extends Crawler {

    /*
     * Ticketmaster database.
     */
    private static DB tmDB = mongoClient().getDB("Ticketmaster");


    /*
     * StubHub database.
     */
    private static DB shDB = mongoClient().getDB("StubHub");


    /*
     * Execute the StubHub crawler.
     */
    public void executeCrawler() {
        try {
            long[] duration = loadEventsTable();
            System.out.println("Loading " + duration[0] + " Events took: " + duration[1] + " minutes, " + duration[2] + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * Purge the databases.
     */
    public void purgeCollections() {
        BasicDBObject basicDBObj = new BasicDBObject();

        DBCollection dbColl = shDB.getCollection("Collected_Events");
        dbColl.remove(basicDBObj);

        dbColl = shDB.getCollection("Collected_Listings");
        dbColl.remove(basicDBObj);

        dbColl = shDB.getCollection("Approved_Listings");
        dbColl.remove(basicDBObj);

        System.out.println();
        System.out.println("STUBHUB DATABASE COLLECTIONS CLEARED!!!!!");
        System.out.println();
    }


    /*
    * Load the "Collected_Listings" table.
    */
    private static long[] loadEventsTable() {
        long start = System.nanoTime();

        DBCollection collection = tmDB.getCollection("Collected_Events");
        DBCursor cursor = collection.find();
        SH_Find_Events findEvents = new SH_Find_Events();

        int index = 1;
        while (cursor.hasNext()) {
            System.out.println("Loading Event " + index + " of " + collection.count());
            DBObject rootObj = cursor.next();

            //Get coordinates
            String[] coordinates = getEventCoordinates(rootObj);
            String lat = coordinates[0];
            String lng = coordinates[1];

            //Get date
            String date = getEventDate(rootObj);

            Map<String, String> params = new HashMap<String, String>();
            params.put("point", lat + "," + lng);
            params.put("eventDate", date);

            try {
                String name = URLEncoder.encode(rootObj.get("name").toString(), "UTF-8");
                params.put("name", name);
            } catch (UnsupportedEncodingException e) {
                System.out.println("One or more parameters could not be encoded. Default values will be queried.");
                System.out.println();
            }

            findEvents.getRequestData(params);
            index++;
        }

        long end = System.nanoTime();
        long count = shDB.getCollection("Collected_Events").count();
        return timeTracker(count, start, end);
    }


    /*
     * Get event coordinates.
     */
    private static String[] getEventCoordinates(DBObject root) {
        String[] coordinates = new String[2];

        DBObject embeddedObj = (DBObject) root.get("_embedded");
        ArrayList<DBObject> venues = (ArrayList<DBObject>) embeddedObj.get("venues");

        for (int i = 0; i < venues.size(); i++) {
            DBObject venue = venues.get(i);
            DBObject location= (DBObject) venue.get("location");

            if (location!= null) {
                coordinates[0] = location.get("latitude").toString();
                coordinates[1] = location.get("longitude").toString();
            }
        }

        return coordinates;
    }


    /*
     * Get event date.
     */
    private static String getEventDate(DBObject root) {
        DBObject dates = (DBObject) root.get("dates");
        DBObject localDate = (DBObject) dates.get("start");

        if (localDate!= null) {
            return localDate.get("dateTime").toString();
        }

        return "";
    }


    //GETS EVENTS FROM STUBHUB
    /*
     * Load the "Collected_Events" table... NOT USED!!!!!!!!
     */
    /*
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
    */
}

