//Current package
package Crawlers;

//Dependencies
import Credentials.Credentials;
import com.mongodb.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.params.*;
import org.apache.http.impl.client.*;
import org.json.*;
import java.io.*;
import java.util.Set;


public abstract class HttpRequest {

    /*******************************************************
     * Fields
     ******************************************************/

    /*
     * Mongo client access.
     */
    protected static MongoClient mongoClient = new MongoClient( "localhost" , 27017);


    /*******************************************************
     * Abstract Methods
     ******************************************************/

    /*
     * Token credentials.
     */
    protected abstract Credentials tokenCredentials();


    /*
    * Determine if the database already exists.
    */
    protected abstract boolean databaseDoesNotExist(MongoClient mongo);


    /*
     * Database.
     */
    protected abstract DB db();


    /*
     * Set request headers.
     */
    protected abstract void setRequestHeaders(HttpGet request);


    /*******************************************************
     * HTTP Request
     ******************************************************/

    /*
     * Send HTTP Get request.
     */
    protected void sendGetRequest(String path, String collectionName) {

        System.out.println("Sending Http GET request......");

        try {
            //Set up client & request
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);

            //Set up Http request
            HttpGet request = new HttpGet(path);
            setRequestHeaders(request);

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
                String line;
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
    *
    * Return codes:
    *               OK - Success
    *    DB Conn Error - Database connection error
    *           DB DNE - Database does not exist
    *   Collection DNE - Collection does not exist
    *        Duplicate - Entry already exists
    *          Unknown - Unknown Error
    */
    public String loadIntoDB(JSONObject json, String collectionName) {
        try {
            // Initialize variables
            System.out.println("Connecting to database.....");

            String validation = validateDatabase(mongoClient, db(), collectionName);
            if (!validation.isEmpty()) {
                return validation;
            }

            DBCollection collection = db().getCollection(collectionName);
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
     * Validate that the given database/collection
     * parameters are accessible.
     */
    protected String validateDatabase(MongoClient mongoClient, DB db, String collectionName) {
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

        return "";
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
     * Check for duplicate values.
     */
    protected boolean duplicateEntry(DBCollection collection, DBObject dbObj) {
        DBCursor obj = collection.find(dbObj);
        return obj.count() > 0;
    }
}
