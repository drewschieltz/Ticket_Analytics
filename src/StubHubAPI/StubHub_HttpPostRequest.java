package StubHubAPI;

import StubHubAPI.ListingsAPI.POST.*;
import StubHubAPI.UserManagementAPI.POST.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASchieltz on 6/10/2017.
 */
public class StubHub_HttpPostRequest {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        StubHub_HttpPostRequest http = new StubHub_HttpPostRequest();

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }


    /*
     * Add barcodes to listing.
     */
    public void addBarcodesToListing() {
        //Call Add_Barcodes_To_Listing
    }


    /*
     * Create listing.
     */
    public void createListing() {
        //Call Create_Listing
    }


    /*
     * Create listing with barcode.
     */
    public void createListingWithBarcode() {
        //Call Create_Listing_With_Barcode
    }


    /*
     * Create price alert.
     */
    public void createPriceAlert() {
        //Call Create_Price_Alert
    }


    /*
     * Http POST request
     */
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }
}
