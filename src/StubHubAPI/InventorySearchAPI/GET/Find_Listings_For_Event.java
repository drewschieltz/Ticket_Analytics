package StubHubAPI.InventorySearchAPI.GET;

import StubHubAPI.StubHub_HttpGetRequest;

/**
 * Created by ASchieltz on 6/10/2017.
 */
public class Find_Listings_For_Event extends StubHub_HttpGetRequest {
    // HTTP GET request
    public void getRequestData(String eventID) {
        //https://api.stubhub.com/search/inventory/v2?eventid=9693644 --> Valid
        sendGetRequest("https://api.stubhub.com/search/inventory/v2/" + eventID, "Listings");
    }
}
