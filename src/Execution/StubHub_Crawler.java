//Current package
package Execution;

//Java dependency
import java.io.UnsupportedEncodingException;
//import java.util.Timer;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;

//Project package dependency
import StubHubAPI.*;

/**
 * Created by ASchieltz on 7/23/2017.
 */
public class StubHub_Crawler {

    /*
     * Execute the algorithm.
     */
    public static void main(String[] args) throws Exception {
        loadEventsTable();

        //Timer timer = new Timer();
    }


    /*
     * Load the "Collected Events Table".
     */
    private static void loadEventsTable() {
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
        params.put("start", "0");

        StubHub_HttpGetRequest requester = new StubHub_HttpGetRequest();
        requester.findEvents(params);
    }
}
