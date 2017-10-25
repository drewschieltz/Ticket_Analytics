//Current package
package Utilities;


public class Shutdown extends Thread {

    /*
     * Execute code on shutdown.
     */
    public void run() {
        Email email = new Email();
        email.sendEmails(0);
    }
}
