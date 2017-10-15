//Current package
package StubHubAPI.SearchAPI;

//Dependencies
import StubHubAPI.SH_Get;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


public class SH_Find_Events extends SH_Get {

    //Number of events returned.
    public int count = 0;


    // HTTP GET request
    public void getRequestData(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.stubhub.com/search/catalog/events/v3?");

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

        sendGetRequest(sb.toString(), "Collected_Events");
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

            String validation = validateDatabase(mongoClient, db(), collectionName);

            if (!validation.isEmpty()) {
                return validation;
            }

            DBCollection collection = db().getCollection(collectionName);
            System.out.println("Collection retrieval successful!");
            System.out.println();

            count = json.getInt("numFound");

            JSONArray array = json.getJSONArray("events");
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
