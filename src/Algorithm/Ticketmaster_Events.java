//Current package
package Algorithm;

//Dependencies
import com.mongodb.*;
import org.json.*;


public class Ticketmaster_Events extends Algorithm {

    /*
     * Execute the algorithm.
     */
    public void executeAlgorithm() {
        DBCollection collection = tmDB.getCollection("Collected_Events");
        DBCursor cursor = collection.find();

        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            DBObject shObj = getStubHubEvent(getTMDateTime(obj), getTMVenueAddress(obj));


            if (shObj == null) {
                System.out.println("Could not find event on StubHub...");
            } else {
                System.out.println("SUCCESS");
            }
        }
    }


    /*
     * Find the corresponding StubHub event.
     */
    private DBObject getStubHubEvent(String dateTime, String address) {
        DBCollection collection = shDB.getCollection("Collected_Events");
        DBCursor cursor = collection.find();

        while (cursor.hasNext()) {
            DBObject obj = cursor.next();

            System.out.println(getSHVenueAddress(obj));
            //System.out.println(address);
            //System.out.println("---------------");

            if (getSHDateTime(obj).equals(dateTime)) {
                if (getSHVenueAddress(obj).equals(address)) {
                    System.out.println("2");
                    return obj;
                }
                //if (getSHVenueCoordinates(obj,true).equals(latitude)) {
                    //System.out.println("2");
                    //if (getSHVenueCoordinates(obj, false).equals(longitude)) {
                        //System.out.println("3");
                        //return obj;
                    //}
                //}
            }
        }
        return null;
    }


    /*******************************************************
     * Ticketmaster Getters
     ******************************************************/

    /*
     * Return the UTC date/time code.
     */
    private String getTMDateTime(DBObject obj) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            JSONObject dates = json.getJSONObject("dates");
            JSONObject start = dates.getJSONObject("start");
            return start.get("dateTime").toString().substring(0, 18);
        } catch (Exception e) {
            System.out.println("Could not retrieve event date on Ticketmaster...");
            return "Fail";
        }
    }


    /*
     * Return the venue's latitude/longitude coordinates.
     */
    /*
    private String getTMVenueCoordinates(DBObject obj, boolean latitude) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            JSONObject embedded = json.getJSONObject("_embedded");
            JSONArray venues = embedded.getJSONArray("venues");
            JSONObject location = venues.getJSONObject(0).getJSONObject("location");

            if (latitude) {
                return location.get("latitude").toString();
            } else {
                return location.get("longitude").toString();
            }
        } catch (Exception e) {
            System.out.println("Could not retrieve venue's latitude on Ticketmaster...");
            return "Fail";
        }
    }
    */


    /*
     * Return the venue's address.
     */
    private String getTMVenueAddress(DBObject obj) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            JSONObject embedded = json.getJSONObject("_embedded");
            JSONArray venues = embedded.getJSONArray("venues");

            JSONObject address = venues.getJSONObject(0).getJSONObject("address");
            JSONObject city = venues.getJSONObject(0).getJSONObject("city");
            JSONObject state = venues.getJSONObject(0).getJSONObject("state");

            String sb = "";
            sb += address.get("line1").toString();
            sb += city.get("name").toString();
            sb += state.get("stateCode").toString();

            return sb;
        } catch (Exception e) {
            System.out.println("Could not retrieve venue's address...");
            return "Fail";
        }
    }


    /*******************************************************
     * StubHub Getters
     ******************************************************/

    /*
     * Return the UTC date/time code.
     */
    private String getSHDateTime(DBObject obj) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            return json.get("eventDateUTC").toString().substring(0, 18);
        } catch (Exception e) {
            System.out.println("Could not retrieve event date on StubHub...");
            return "Fail";
        }
    }


    /*
     * Return the venue's latitude/longitude coordinates.
     */
    /*
    private String getSHVenueCoordinates(DBObject obj, boolean latitude) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            JSONObject venue = json.getJSONObject("venue");

            if (latitude) {
                return venue.get("latitude").toString();
            } else {
                return venue.get("longitude").toString();
            }
        } catch (Exception e) {
            System.out.println("Could not retrieve venue's latitude on StubHub...");
            return "Fail";
        }
    }
    */


    /*
     * Return the venue's address.
     */
    private String getSHVenueAddress(DBObject obj) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            JSONObject venue = json.getJSONObject("venue");

            String sb = "";
            sb += venue.get("address1").toString();
            sb += venue.get("city").toString();
            sb += venue.get("state").toString();

            return sb;
        } catch (Exception e) {
            System.out.println("Could not retrieve venue's address...");
            return "Fail";
        }
    }
}
