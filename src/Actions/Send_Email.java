//Current package
package Actions;

//Java dependencies
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class Send_Email {

    //Test code
    public static void main(String [] args) {
        sendEmails();
    }

    /*
     * Send emails.
     */
    public static void sendEmails() {
        final String username = "joe.andy.tix@gmail.com";
        final String password = "15WHwwjd02";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("joe.andy.tix@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getAddresses()));

            message.setSubject("Test Subject");
            message.setText("Recipient,"
                    + "\n\nThis is a test of the automatic email system. Please disregard.");

            Transport.send(message);

            System.out.println("Email(s) sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * Format recipient string.
     */
    private static String getAddresses() {
        ArrayList<String> recipients = getRecipients();
        String addresses = recipients.get(0);

        for (int i=1; i < recipients.size(); i++) {
            addresses += "," + recipients.get(i);
        }

        return addresses;
    }


    /*
     * Return email recipients.
     */
    private static ArrayList<String> getRecipients() {
        ArrayList<String> recipients = new ArrayList<String>();

        recipients.add("drewschieltz@gmail.com");
        //recipients.add("travispulfer@gmail.com");

        return recipients;
    }
}
