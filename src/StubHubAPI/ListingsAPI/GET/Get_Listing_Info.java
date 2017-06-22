package StubHubAPI.ListingsAPI.GET;

import StubHubAPI.StubHub_HttpGetRequest;

/**
 * Created by ASchieltz on 6/10/2017.
 */
public class Get_Listing_Info extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String listingID) {
        sendGetRequest("https://api.stubhub.com/inventory/listings/v2/" + listingID, "Listings");
    }
}
