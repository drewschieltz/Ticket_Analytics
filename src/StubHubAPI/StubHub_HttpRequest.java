//Current package
package StubHubAPI;

//Package dependencies
import Credentials.Token_Credentials;

//MongoDB dependencies
import com.mongodb.*;

//JSON Dependencies
import org.json.JSONObject;

//Java dependencies
import java.util.List;
import java.util.Set;


public class StubHub_HttpRequest {

    public static Token_Credentials token_credentials = new Token_Credentials();

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
            MongoClient mongoClient = new MongoClient( "localhost" , 27017);
            DB db = mongoClient.getDB("StubHub");

            String validation = validateDatabase(mongoClient, db, collectionName);
            if (!validation.isEmpty()) {
                return validation;
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
     * Validate that the given database/collection
     * parameters are accessible.
     */
    public String validateDatabase(MongoClient mongoClient, DB db, String collectionName) {
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
    public boolean collectionDoesNotExist(DB db, String collectionName) {
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
    public boolean duplicateEntry(DBCollection collection, DBObject dbObj) {
        DBCursor obj = collection.find(dbObj);
        return obj.count() > 0;
    }
}
