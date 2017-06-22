package StubHubAPI.EventsAPI.GET;

import StubHubAPI.StubHub_HttpGetRequest;

/**
 * Created by ASchieltz on 6/10/2017.
 */
public class Get_Event_Info extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String eventID) {
        sendGetRequest("https://api.stubhub.com/catalog/events/v2/" + eventID, "Events");
    }
}
