//Current package
package StubHubAPI.ListingsAPI;

//Dependencies
import StubHubAPI.SH_HttpRequest;


public class SH_Listing_Info extends SH_HttpRequest {

    // HTTP GET request
    public void getRequestData(String listingID) {
        sendGetRequest("https://api.stubhub.com/inventory/listings/v2/" + listingID, "Approved_Listings");
    }
}
