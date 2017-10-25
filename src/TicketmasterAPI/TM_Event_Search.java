//Current package
package TicketmasterAPI;

//Dependencies
import com.mongodb.*;
import org.json.*;
import java.util.*;


public class TM_Event_Search extends TM_HttpRequest {

    /*
    * Number of elements/pages returned.
    */
    public int elements = 0;
    public int pages = 0;


    /*
     * Number of request calls.
     */
    private int requestCalls = 0;


    /*
    * HTTP GET Request
    */
    public void getRequestData(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();

        sb.append("https://app.ticketmaster.com/discovery/v2/events.json?apikey=");
        sb.append(tokenCredentials().applicationToken());

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

        requestCalls++;
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
            if (requestCalls == 1) {
                elements = json.getJSONObject("page").getInt("totalElements");
            }

            if (requestCalls > 1) {
                if (requestCalls == 2) {
                    pages = json.getJSONObject("page").getInt("totalPages");
                }

                System.out.println("Connecting to database.....");

                String validation = validateDatabase(mongoClient, db(), collectionName);

                if (!validation.isEmpty()) {
                    return validation;
                }

                DBCollection collection = db().getCollection(collectionName);
                System.out.println("Collection retrieval successful!");
                System.out.println();

                JSONArray array = json.getJSONObject("_embedded").getJSONArray("events");

                for (int i=0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    //Parse the json into a dbObject and enter into database
                    DBObject dbObj = (DBObject) com.mongodb.util.JSON.parse(obj.toString());
                    if (!duplicateEntry(collection, dbObj)) {
                        collection.insert(dbObj);
                    }
                }

                return "OK";
            }
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return "Unknown";
        }

        return "No Database Effect";
    }
}
