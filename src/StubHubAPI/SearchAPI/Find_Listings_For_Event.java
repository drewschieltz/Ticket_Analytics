//Current package
package StubHubAPI.SearchAPI;

//Project package dependencies
import StubHubAPI.StubHub_HttpGetRequest;

//Mongo dependencies
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

//Java dependencies
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


public class Find_Listings_For_Event extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String eventID, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.stubhub.com/search/inventory/v2?eventid=");
        sb.append(eventID);

        if (params != null) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                sb.append("&");
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }
        }

        sendGetRequest(sb.toString(), "Collected Listings");
    }


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
            System.out.println("Connecting to database.....");

            String validation = validateDatabase(mongoClient, db, collectionName);

            if (!validation.isEmpty()) {
                return validation;
            }

            DBCollection collection = db.getCollection(collectionName);
            System.out.println("Collection retrieval successful!");
            System.out.println();

            JSONArray array = json.getJSONArray("listing");
            for (int i=0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                //Parse the json into a dbObject and enter into database
                DBObject dbObj = (DBObject) com.mongodb.util.JSON.parse(obj.toString());
                if (!duplicateEntry(collection, dbObj)) {
                    collection.insert(dbObj);
                }
            }

            return "OK";
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return "Unknown";
        }
    }
}
