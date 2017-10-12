//Current package
package StubHubAPI;

//Apache dependencies
import StubHubAPI.UserManagementAPI.Create_Price_Alert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class StubHub_HttpPostRequest extends StubHub_HttpRequest {

    //Run code - For testing only
    public static void main(String[] args) throws Exception {
        //Test code
    }


    /*******************************************************
     * Send Post Request
     ******************************************************/

    /*
     * Send HTTP Post request
     */
    public void sendPostRequest(String path, String collectionName) {

        System.out.println("Sending Http POST request......");

        try {
            //Set up client & request
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);

            HttpPost request = new HttpPost(path);

            // add request headers --> TODO
            String token = token_credentials.applicationToken();
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("User-Agent", "Mozilla/5.0");

            //Execute request
            HttpResponse response = client.execute(request);

            System.out.println("\nSending 'POST' request to URL : " + path);

            int respCode = response.getStatusLine().getStatusCode();

            System.out.println("Response Code : " + respCode);
            System.out.println();
            System.out.println("-------------");

            if (respCode == 200) {
                //Load response result
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                //Load data into db
                System.out.println();
                System.out.println("Pushing info into the database......");
                System.out.println();

                JSONObject json;

                if (result.toString().contains("xml")) {
                    json = XML.toJSONObject(result.toString());
                } else {
                    json = new JSONObject(result.toString());
                }

                String code = loadIntoDB(json, collectionName);

                System.out.println();
                System.out.println("Mongo Response Code: " + code);
                System.out.println();
                System.out.println("=====================================================");
            } else {
                System.out.println();
                System.out.println("Data retrieval unsuccessful!");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving data. Process aborted.");
            e.printStackTrace();
        }
    }


    /*******************************************************
     * Class Getters
     ******************************************************/

    /*
     * Create price alert.
     */
    private static void createPriceAlert(String eventID) {
        Create_Price_Alert http = new Create_Price_Alert();
        //http.postRequestData(eventID);
    }
}
