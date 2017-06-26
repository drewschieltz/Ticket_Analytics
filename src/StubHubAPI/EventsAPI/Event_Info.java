//Current package
package StubHubAPI.EventsAPI;

//Project package dependencies
import StubHubAPI.StubHub_HttpGetRequest;


public class Event_Info extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String eventID) {
        sendGetRequest("https://api.stubhub.com/catalog/events/v2/" + eventID, "Events");
    }
}
