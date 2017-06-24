//Current package
package StubHubAPI.ListingsAPI;

//Project package dependencies
import StubHubAPI.StubHub_HttpGetRequest;


public class Get_Listing_Info extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String listingID) {
        sendGetRequest("https://api.stubhub.com/inventory/listings/v2/" + listingID, "Listings");
    }
}
