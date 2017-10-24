//Current package
package Crawlers;

//Dependencies
import com.mongodb.*;


public abstract class Crawler {

    /*
    * Mongo client.
    */
    static MongoClient mongoClient() {
        return new MongoClient( "localhost" , 27017);
    }


    /*
     * Execute the crawler.
     */
    protected abstract void executeCrawler();


    /*
     * Purge the database collections.
     */
    protected abstract void purgeCollections();


    /*
    * Time tracker.
    */
    static long[] timeTracker(long count, long start, long end) {
        long[] times = new long[3];

        long duration = (end-start)/1000000000;
        long minutes = duration/60;
        long seconds = duration - (minutes*60);

        times[0] = count;
        times[1] = minutes;
        times[2] = seconds;
        return times;
    }
}
