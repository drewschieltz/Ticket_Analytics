//Current package
package StubHubAPI.SearchAPI;

//Project package dependencies
import StubHubAPI.StubHub_HttpGetRequest;

//Java dependencies
import java.util.Iterator;
import java.util.Map;


public class Find_Events extends StubHub_HttpGetRequest {

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

        sendGetRequest(sb.toString(), "Event_Search_Results");
    }
}
