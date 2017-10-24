//Current package
package StubHubAPI;

//Dependencies
import Credentials.*;
import Crawlers.HttpRequest;
import com.mongodb.*;
import org.apache.http.client.methods.*;
import java.util.*;


public class SH_HttpRequest extends HttpRequest {

    /*
     * Token credentials.
     */
    protected Credentials tokenCredentials() {
        return new StubHub_Token_Credentials();
    }


    /*
     * Database.
     */
    protected DB db() {
        return mongoClient.getDB("StubHub");
    }


    /*
     * Determine if the database already exists.
     */
    protected boolean databaseDoesNotExist(MongoClient mongo) {
        List<String> names = mongo.getDatabaseNames();

        for (final String name : names) {
            if (name.equals("StubHub")) {
                return false;
            }
        }

        return true;
    }

    /*
     * Set request headers.
     */
    protected void setRequestHeaders(HttpGet request) {
        request.setHeader("Authorization", "Bearer " + tokenCredentials().applicationToken());
        request.setHeader("User-Agent", "Mozilla/5.0");
    }
}
