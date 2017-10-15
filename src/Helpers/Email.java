//Current package
package Helpers;

//Dependencies
import Credentials.Email_Credentials;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class Email {

    private static Email_Credentials credentials = new Email_Credentials();

    //Test code
    public static void main(String [] args) {}


    /*
     * Send emails.
     */
    public void sendEmails(String text) {
        Session session = Session.getInstance(properties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(credentials.username(), credentials.password());
                    }
                });

        try {
            Transport.send(formatMessage(session, text));
            System.out.println();
            System.out.println("Email(s) sent successfully!");
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
    private static Message formatMessage(Session session, String text) {
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(credentials.username()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getAddressList()));

            message.setSubject("Test Subject");

            if (text.isEmpty()) {
                message.setText("Recipient,"
                        + "\n\nThis is a test of the automatic email system. Please disregard.");
            } else {
                message.setText(text);
            }

            return message;
        } catch (MessagingException e) {
            System.out.println("Message failed to generate!");
        }

        return message;
    }
}
