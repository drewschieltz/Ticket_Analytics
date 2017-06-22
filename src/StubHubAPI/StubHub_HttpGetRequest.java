package StubHubAPI;

//import StubHubAPI.AccountManagementAPI.GET.*;
//import StubHubAPI.EventsAPI.GET.*;
//import StubHubAPI.EventSearchAPI.GET.*;
//import StubHubAPI.InventorySearchAPI.GET.*;
//import StubHubAPI.ListingsAPI.GET.*;
//import StubHubAPI.UserManagementAPI.GET.*;
import StubHubAPI.VenuesAPI.GET.*;

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
import java.util.List;
import java.util.Set;

public class StubHub_HttpGetRequest {

    //Fields
    public final String token = "16ede3a5-ad0f-3042-991a-adb3e2e2754a";


    //Run code - For testing only
    public static void main(String[] args) throws Exception {
        getVenueInfo("180239");
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
            String token = "16ede3a5-ad0f-3042-991a-adb3e2e2754a";
            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("User-Agent", "Mozilla/5.0");

            //Execute request
            HttpResponse response = client.execute(request);

            System.out.println("\nSending 'GET' request to URL : " + path);
            int respCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code : " + respCode);
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

                JSONObject xmlJSONObj = XML.toJSONObject(result.toString());
                int code = loadIntoDB(xmlJSONObj, collectionName);

                System.out.println();
                System.out.println("Mongo Response Code: " + code);
            } else {
                System.out.println("Data retrieval unsuccessful!");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving venue information");
            e.printStackTrace();
        }
    }


    /*******************************************************
     * Database Methods
     ******************************************************/

    /*
    * Load data into database.
    * Return codes:
    *   100 - Success
    *   200 - Database connection error
    *   225 - Database does not exist
    *   250 - Collection does not exist
    *   300 - Duplicate
    *   400 - Unknown error
    */
    private int loadIntoDB(JSONObject json, String collectionName) {
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
                return 200;
            }

            //Confirm database exists
            if (databaseDoesNotExist(mongoClient, "StubHub")) {
                System.out.println("Database does not exist. Process aborted.");
                return 225;
            }

            System.out.println("Connection to database successful!");
            System.out.println();

            //Confirm collection exists. If so, retrieve the collection
            System.out.println("Connecting to collection.....");
            if (collectionDoesNotExist(db, collectionName)) {
                System.out.println("Collection does not exist. Process aborted.");
                return 250;
            }

            DBCollection collection = db.getCollection(collectionName);
            System.out.println("Collection retrieval successful!");
            System.out.println();

            //Parse the json into a dbObject
            DBObject dbObj = (DBObject) com.mongodb.util.JSON.parse(json.toString());

            //Confirm this entry is not duplicate
            if (duplicateEntry(collection, dbObj)) {
                System.out.println("Entry already exists. Process aborted.");
                return 300;
            }

            //Enter information into db
            System.out.println("Inserting data into database.....");
            collection.insert(dbObj);
            System.out.println("Data insertion successful!");

            return 100;
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return 400;
        }
    }


    /*
     * Determine if the collection already exists.
     */
    private boolean databaseDoesNotExist(MongoClient mongo, String dbName) {
        List<String> names = mongo.getDatabaseNames();

        for (final String name : names) {
            if (name.equals(dbName)) {
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
    public void getListing() {
        //Call Get_Listing
    }


    /*
     * Get all listings from my account.
     */
    public void getAllListings() {
        //Call Get_All_Listings
    }


    /*
     * Get all sales from my account.
     */
    public void getAllSales() {
        //Call Get_All_Sales
    }


    /*
     * Get event information.
     */
    public void getEventInfo() {
        //Call Get_Event_Info
    }

    /*
     * Get event meta data.
     */
    public void getEventMetaData() {
        //Call Get_Evet_MetaData
    }


    /*
     * Find events based on certain criterion.
     */
    public void findEvents() {
        //Call Find_Events
    }


    /*
     * Find listings for a certain event.
     */
    public void findListingsForEvent() {
        //Call Find_Listings_For_Event
    }


    /*
     * For information about certain sections for an event.
     */
    public void findSectionDataForEvent() {
        //Call Find_Section_Data_For_Event
    }


    /*
     * Search for a specific listing.
     */
    public void searchForListing() {
        //Call Search_For_Listing
    }


    /*
     * Get listing information.
     */
    public void getListingInfo() {
        //Call Get_Listing_Info
    }


    /*
     * Get a specific price alert.
     */
    public void getPriceAlert() {
        //Call Get_Price_Alert
    }


    /*
     * Get all of my price alerts.
     */
    public void getPriceAlerts() {
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
