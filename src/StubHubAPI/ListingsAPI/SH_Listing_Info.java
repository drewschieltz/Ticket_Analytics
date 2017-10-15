//Current package
package StubHubAPI.ListingsAPI;

//Dependencies
import StubHubAPI.SH_Get;


public class SH_Listing_Info extends SH_Get {

    // HTTP GET request
    public void getRequestData(String listingID) {
        sendGetRequest("https://api.stubhub.com/inventory/listings/v2/" + listingID, "Approved_Listings");
    }
}
