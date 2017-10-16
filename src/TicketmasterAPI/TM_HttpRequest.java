//Current package
package TicketmasterAPI;

//Dependencies
import Credentials.*;
import Helpers.HttpRequest;
import com.mongodb.*;
import java.util.List;


public class TM_HttpRequest extends HttpRequest{

    /*
     * Token credentials.
     */
    @Override
    protected Credentials tokenCredentials() {
        return new Ticketmaster_Token_Credentials();
    }


    /*
     * Database.
     */
    @Override
    protected DB db() {
        return mongoClient.getDB("Ticketmaster");
    }


    /*
     * Determine if the database already exists.
     */
    @Override
    public boolean databaseDoesNotExist(MongoClient mongo) {
        List<String> names = mongo.getDatabaseNames();

        for (final String name : names) {
            if (name.equals("Ticketmaster")) {
                return false;
            }
        }

        return true;
    }
}
