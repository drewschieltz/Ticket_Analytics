package StubHubAPI;

import StubHubAPI.EventsAPI.Get_Event_Info;
import StubHubAPI.ListingsAPI.Get_Listing_Info;
import StubHubAPI.SearchAPI.Find_Listings_For_Event;

import StubHubAPI.VenuesAPI.Venue_Info;
import com.mongodb.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class StubHub_HttpGetRequest {

    //Run code - For testing only
    public static void main(String[] args) throws Exception {
        /*LISTINGS V2 API*/
        //getListingInfo("1261109138");

        /*VENUES V2 API*/
        //getVenueInfo("180239");

        /*EVENTS V2 API*/
        //getEventInfo("9693644");

        /*SEARCH V2 API*/

        //Map<String, String> params = new HashMap<String, String>() {};
        //params.put("quantity", "8");
        //params.put("pricemin", "150");
        //findListingsForEvent("9693644", params);

        
    }


    /*******************************************************
     * Send Get Request
     ******************************************************/


    public void sendGetRequest(String path, String collectionName) {

        System.out.println("Sending Http GET request......");

        try {

            //Set up client & request
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);

            HttpGet request = new HttpGet(path);

            // add request headers
            String token = "eaf08ffb-ef73-301f-bac0-94d3d471eec0";
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("User-Agent", "Mozilla/5.0");

            //Execute request
            HttpResponse response = client.execute(request);

            System.out.println("\nSending 'GET' request to URL : " + path);

            int respCode = response.getStatusLine().getStatusCode();

            System.out.println("Response Code : " + respCode);
            System.out.println();
            System.out.println("-------------");

            if (respCode == 200) {
                //Load response result
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                //Load data into db
                System.out.println();
                System.out.println("Pushing info into the database......");
                System.out.println();

                JSONObject json;

                if (result.toString().contains("xml")) {
                    json = XML.toJSONObject(result.toString());
                } else {
                    json = new JSONObject(result.toString());
                }

                String code = loadIntoDB(json, collectionName);

                System.out.println();
                System.out.println("Mongo Response Code: " + code);
                System.out.println();
                System.out.println("=====================================================");
            } else {
                System.out.println();
                System.out.println("Data retrieval unsuccessful!");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving data. Process aborted.");
            e.printStackTrace();
        }
    }


    /*******************************************************
     * Database Methods
     ******************************************************/

    /*
    * Load data into database.
    * Return codes:
    *               OK - Success
    *    DB Conn Error - Database connection error
    *           DB DNE - Database does not exist
    *   Collection DNE - Collection does not exist
    *        Duplicate - Entry already exists
    *          Unknown - Unknown Error
    */
    private String loadIntoDB(JSONObject json, String collectionName) {
        try {
            // Initialize variables
            System.out.println("Connecting to database.....");
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            DB db = mongoClient.getDB("StubHub");

            //Attempt to connect to the mongo server
            try {
                mongoClient.getAddress();
            } catch(Exception e) {
                System.out.println("Connection to database failed. Process aborted.");
                return "DB Conn Error";
            }

            //Confirm database exists
            if (databaseDoesNotExist(mongoClient)) {
                System.out.println("Database does not exist. Process aborted.");
                return "DB DNE";
            }

            System.out.println("Connection to database successful!");
            System.out.println();

            //Confirm collection exists. If so, retrieve the collection
            System.out.println("Connecting to collection.....");
            if (collectionDoesNotExist(db, collectionName)) {
                System.out.println("Collection does not exist. Process aborted.");
                return "Collection DNE";
            }

            DBCollection collection = db.getCollection(collectionName);
            System.out.println("Collection retrieval successful!");
            System.out.println();

            //Parse the json into a dbObject
            DBObject dbObj = (DBObject) com.mongodb.util.JSON.parse(json.toString());

            //Confirm this entry is not duplicate
            if (duplicateEntry(collection, dbObj)) {
                System.out.println("Entry already exists. Process aborted.");
                return "Duplicate";
            }

            //Enter information into db
            System.out.println("Inserting data into database.....");
            collection.insert(dbObj);
            System.out.println("Data insertion successful!");

            return "OK";
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return "Unknown";
        }
    }


    /*
     * Determine if the collection already exists.
     */
    private boolean databaseDoesNotExist(MongoClient mongo) {
        List<String> names = mongo.getDatabaseNames();

        for (final String name : names) {
            if (name.equals("StubHub")) {
                return false;
            }
        }

        return true;
    }


    /*
     * Determine if the collection already exists.
     */
    private boolean collectionDoesNotExist(DB db, String collectionName) {
        Set<String> names = db.getCollectionNames();

        for (final String name : names) {
            if (name.equals(collectionName)) {
                return false;
            }
        }

        return true;
    }


    /*
     * Check for duplicate values
     */
    private boolean duplicateEntry(DBCollection collection, DBObject dbObj) {
        DBCursor obj = collection.find(dbObj);
        return obj.count() > 0;
    }


    /*******************************************************
     * Class Getters
     ******************************************************/

    /*
     * Get a single listing from my account.
     */
    private static void getListing() {
        //Call Get_Listing
    }


    /*
     * Get all listings from my account.
     */
    private static void getAllListings() {
        //Call Get_All_Listings
    }


    /*
     * Get all sales from my account.
     */
    private static void getAllSales() {
        //Call Get_All_Sales
    }


    /*
     * Get event information.
     */
    private static void getEventInfo(String eventID) {
        Get_Event_Info http = new Get_Event_Info();
        http.getRequestData(eventID);
    }

    /*
     * Get event meta data.
     */
    private static void getEventMetaData() {
        //Call Get_Evet_MetaData
    }


    /*
     * Find events based on certain criterion.
     */
    private static void findEvents() {
        //Call Find_Events
    }


    /*
     * Find listings for a certain event.
     */
    private static void findListingsForEvent(String eventID, Map<String, String> params) {
        //Call Find_Listings_For_Event
        Find_Listings_For_Event http = new Find_Listings_For_Event();
        http.getRequestData(eventID, params);
    }


    /*
     * Get listing information.
     */
    private static void getListingInfo(String eventID) {
        Get_Listing_Info http = new Get_Listing_Info();
        http.getRequestData(eventID);
    }


    /*
     * Get a specific price alert.
     */
    private static void getPriceAlert() {
        //Call Get_Price_Alert
    }


    /*
     * Get all of my price alerts.
     */
    private static void getPriceAlerts() {
        //Call Get_Price_Alerts
    }


    /*
     * Get venue information.
     */
    private static void getVenueInfo(String venueID) {
        Venue_Info http = new Venue_Info();
        http.getRequestData(venueID);
    }
}
