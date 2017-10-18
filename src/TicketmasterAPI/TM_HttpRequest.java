//Current package
package TicketmasterAPI;

//Dependencies
import Credentials.*;
import Helpers.HttpRequest;
import com.mongodb.*;
import org.apache.http.client.methods.*;
import java.util.*;


public class TM_HttpRequest extends HttpRequest{

    /*
     * Token credentials.
     */
    protected Credentials tokenCredentials() {
        return new Ticketmaster_Token_Credentials();
    }


    /*
     * Database.
     */
    protected DB db() {
        return mongoClient.getDB("Ticketmaster");
    }


    /*
     * Determine if the database already exists.
     */
    protected boolean databaseDoesNotExist(MongoClient mongo) {
        List<String> names = mongo.getDatabaseNames();

        for (final String name : names) {
            if (name.equals("Ticketmaster")) {
                return false;
            }
        }

        return true;
    }


    /*
     * Set request headers (None required).
     */
    protected void setRequestHeaders(HttpGet request) {}
}
