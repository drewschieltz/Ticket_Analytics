//Current package
package Helpers;

//Package dependencies
import Credentials.Email_Credentials;

//Java dependencies
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class Email {

    public static Email_Credentials credentials = new Email_Credentials();

    //Test code
    public static void main(String [] args) {
        sendEmails();
    }


    /*
     * Send emails.
     */
    public static void sendEmails() {
        Session session = Session.getInstance(properties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(credentials.username(), credentials.password());
                    }
                });

        try {
            if (formatMessage(session) != null) {
                Transport.send(formatMessage(session));
                System.out.println();
                System.out.println("Email(s) sent successfully!");
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * Set the mail server properties.
     */
    private static Properties properties() {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return props;
    }


    /*
     * Format recipient string.
     */
    private static String getAddressList() {
        ArrayList<String> recipients = getRecipients();
        StringBuilder sb = new StringBuilder();
        sb.append(recipients.get(0));

        for (int i=1; i < recipients.size(); i++) {
            sb.append(",");
            sb.append(recipients.get(i));
        }

        return sb.toString();
    }


    /*
     * Return email recipients.
     */
    private static ArrayList<String> getRecipients() {
        ArrayList<String> recipients = new ArrayList<String>();

        recipients.add("drewschieltz@gmail.com");
        //recipients.add("aschieltz@midmark.com");
        //recipients.add("travispulfer@gmail.com");

        return recipients;
    }


    /*
     * Format the email subject/body.
     */
    private static Message formatMessage(Session session) {
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(credentials.username()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getAddressList()));

            message.setSubject("Test Subject");
            message.setText("Recipient,"
                    + "\n\nThis is a test of the automatic email system. Please disregard.");

            return message;
        } catch (MessagingException e) {
            System.out.println("Message failed to generate!");
        }

        return null;
    }
}
