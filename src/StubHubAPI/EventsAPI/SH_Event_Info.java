//Current package
package StubHubAPI.EventsAPI;

//Dependencies
import StubHubAPI.SH_HttpRequest;


public class SH_Event_Info extends SH_HttpRequest {

    // HTTP GET request
    public void getRequestData(String eventID) {
        sendGetRequest("https://api.stubhub.com/catalog/events/v2/" + eventID, "Events");
    }
}
