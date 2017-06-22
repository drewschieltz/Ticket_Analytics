package StubHubAPI.VenuesAPI.GET;

import StubHubAPI.StubHub_HttpGetRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by ASchieltz on 6/10/2017.
 */
public class Venue_Info extends StubHub_HttpGetRequest {

    // HTTP GET request
    public void getRequestData(String venueID) {
        sendGetRequest("https://api.stubhub.com/catalog/venues/v2/" + venueID, "Venues");
    }
}
