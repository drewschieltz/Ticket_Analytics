//Current package
package Utilities;


public class Shutdown extends Thread {

    public boolean success = false;


    /*
     * Execute code on shutdown.
     */
    public void run() {
        Email email = new Email();

        if (success) {
            email.sendEmails(0);
        } else {
            email.sendEmails(3);
        }
    }
}
