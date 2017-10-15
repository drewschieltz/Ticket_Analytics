//Current package
package StubHubAPI;

//Dependencies
import Credentials.StubHub_Token_Credentials;
import Helpers.HttpRequest;
import com.mongodb.*;
import java.util.List;


public class SH_HttpRequest extends HttpRequest{

    /*
     * Token credentials.
     */
    static StubHub_Token_Credentials token_credentials = new StubHub_Token_Credentials();


    /*
     * Database.
     */
    @Override
    protected DB db() {
        return mongoClient.getDB("StubHub");
    }


    /*
     * Determine if the database already exists.
     */
    @Override
    protected boolean databaseDoesNotExist(MongoClient mongo) {
        List<String> names = mongo.getDatabaseNames();

        for (final String name : names) {
            if (name.equals("StubHub")) {
                return false;
            }
        }

        return true;
    }
}
