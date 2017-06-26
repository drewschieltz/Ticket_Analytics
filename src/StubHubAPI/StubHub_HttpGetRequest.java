//Current Package
package StubHubAPI;

//Project package dependencies
import StubHubAPI.EventsAPI.*;
import StubHubAPI.ListingsAPI.*;
import StubHubAPI.SearchAPI.*;
import StubHubAPI.VenuesAPI.*;

//Apache dependencies
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;

//XML/JSON dependencies
import org.json.JSONObject;
import org.json.XML;

//Other java dependencies
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class StubHub_HttpGetRequest extends StubHub_HttpRequest {

    //Run code - For testing only
    public static void main(String[] args) throws Exception {
        /*LISTINGS V2 API*/
        //getListingInfo("1261109138");

        /*VENUES V2 API*/
        //getVenueInfo("180239");

        /*EVENTS V2 API*/
        //getEventInfo("9892364");

        /*SEARCH V2 API*/

        //Map<String, String> params = new HashMap<String, String>() {};
        //params.put("quantity", "8");
        //params.put("pricemin", "150");
        //findListingsForEvent("9693644", params);

        //Map<String, String> params = new HashMap<String, String>();
        //params.put("name", "church");
        //params.put("state", "TX");
        //params.put("parking", "false");
        //findEvents(params);
    }


    /*******************************************************
     * Send Get Request
     ******************************************************/

    /*
     * Send HTTP Get request.
     */
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
     * Class Getters
     ******************************************************/

    /*
     * Get event information.
     */
    private static void getEventInfo(String eventID) {
        Event_Info http = new Event_Info();
        http.getRequestData(eventID);
    }


    /*
     * Find events based on certain criterion.
     */
    private static void findEvents(Map<String, String> params) {
        Find_Events http = new Find_Events();
        http.getRequestData(params);
    }


    /*
     * Find listings for a certain event.
     */
    private static void findListingsForEvent(String eventID, Map<String, String> params) {
        Find_Listings_For_Event http = new Find_Listings_For_Event();
        http.getRequestData(eventID, params);
    }


    /*
     * Get listing information.
     */
    private static void getListingInfo(String eventID) {
        Listing_Info http = new Listing_Info();
        http.getRequestData(eventID);
    }


    /*
     * Get venue information.
     */
    private static void getVenueInfo(String venueID) {
        Venue_Info http = new Venue_Info();
        http.getRequestData(venueID);
    }
}
