//Current package
package StubHubAPI.VenuesAPI;

//Project package dependencies
import StubHubAPI.StubHub_HttpGetRequest;


public class Venue_Info extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String venueID) {
        sendGetRequest("https://api.stubhub.com/catalog/venues/v2/" + venueID, "Venues");
    }
}
