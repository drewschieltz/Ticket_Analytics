//Current package
package Algorithm;

//Dependencies
import com.mongodb.*;


abstract class Algorithm {

    /*
    * Mongo client.
    */
    private static MongoClient mongoClient() {
        return new MongoClient( "localhost" , 27017);
    }


    /*
    * Ticketmaster database.
    */
    protected DB tmDB = mongoClient().getDB("Ticketmaster");


    /*
    * StubHub database.
    */
    protected static DB shDB = mongoClient().getDB("StubHub");


    /*
     * Execute the crawler.
     */
    public abstract void executeAlgorithm();
}
