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
    public void sendGetRequest(String venueID) {

        System.out.println("Sending Http GET request......");

        try {
            //Access path
            String url = "https://api.stubhub.com/catalog/venues/v2/" + venueID;

            //Set up client & request
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
            HttpGet request = new HttpGet(url);

            // add request headers
            String token = "16ede3a5-ad0f-3042-991a-adb3e2e2754a";
            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("User-Agent", USER_AGENT);

            //Execute request
            HttpResponse response = client.execute(request);

            System.out.println("\nSending 'GET' request to URL : " + url);
            int respCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code : " + respCode);
            System.out.println("-------------");

            if (respCode == 200) {
                //Load response result
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                //Load data into db
                System.out.println();
                System.out.println("Pushing info into the database......");

                JSONObject xmlJSONObj = XML.toJSONObject(result.toString());
                int code = loadIntoDB(xmlJSONObj, "Venues");

                System.out.println();
                System.out.println("Mongo Response Code: " + code);
            } else {
                System.out.println("Data retrieval unsuccessful!");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving venue information");
            e.printStackTrace();
        }
    }
}
