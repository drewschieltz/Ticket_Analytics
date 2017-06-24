package StubHubAPI.SearchAPI;

import StubHubAPI.StubHub_HttpGetRequest;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ASchieltz on 6/10/2017.
 */
public class Find_Listings_For_Event extends StubHub_HttpGetRequest {
    // HTTP GET request
    public void getRequestData(String eventID, Map<String, String> params) {
        String path;
        path = "https://api.stubhub.com/search/inventory/v2?eventid=" + eventID;

        if (params != null) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                path += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }

        sendGetRequest(path, "Event_Listings");
    }
}
