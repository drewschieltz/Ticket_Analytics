//Current package
package Helpers;

/*
  * Shutdown hook.
  */
public class Shutdown extends Thread {

    /*
     * Execute code on shutdown.
     */
    public void run() {
        Email email = new Email();
        email.sendEmails("Program has been terminated. Please review cause for termination.");
    }
}
