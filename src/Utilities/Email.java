//Current package
package Utilities;

//Dependencies
import Credentials.Email_Credentials;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class Email {

    /*
     * Credentials field.
     */
    private static Email_Credentials credentials = new Email_Credentials();


    /*
     * Test code.
     */
    public static void main(String [] args) {
        //sendEmails(0);
        //sendEmails(1);
        //sendEmails(2);
        //sendEmails(3);
    }


    /*
     * Send emails.
     */
    void sendEmails(int code) {
        Session session = Session.getInstance(properties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(credentials.username(), credentials.password());
                    }
                });

        try {
            for (int i = 0; i < getRecipients(code).size(); i++) {
                Transport.send(formatMessage(session, emailBody(code).toString(), code, i));
                System.out.println();
                System.out.println("Code " + code + ": Email sent to " + getRecipientName(i) + " successfully!");
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * Return the email subject.
     */
    private static String getEmailSubject(int code) {
        switch (code) {
            case 0 : return "Program Terminated - No Errors";
            case 1 : return "Upcoming Event Recommendations";
            case 2 : return "StubHub Listing Recommendations";
            case 3 : return "Program Terminated - Errors Found";
            default : return "Disregard";
        }
    }


    /*
     * Return email body based on code parameter.
     *
     * 0 = Clean program shutdown
     * 1 = Daily Ticketmaster Events
     * 2 = Profitable StubHub Listings
     * 3 = Program ends based on error.
     */
    private static StringBuilder emailBody(int code) {
        StringBuilder sb = new StringBuilder();

        switch (code) {
            case 0 : {
                sb.append("The program has terminated with no issues.");
                break;
            }
            case 1 : {
                sb.append("This is where I would send TM events.");
                break;
            }
            case 2 : {
                sb.append("This is where I would send StubHub listings.");
                break;
            }
            case 3 : {
                sb.append("The program has terminated with issues.");
                break;
            }
            default : {
                sb.append("Error - Please disregard this issue.");
            }
        }

        return sb;
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
     * Return email recipients.
     */
    private static ArrayList<String> getRecipients(int code) {
        ArrayList<String> recipients = new ArrayList<String>();

        recipients.add("drewschieltz@gmail.com");

        //Non-developer emails only
        if (code == 1 || code == 2) {
            //recipients.add("aschieltz@midmark.com");
            recipients.add("travispulfer@gmail.com");
        }

        return recipients;
    }


    /*
     * Return the recipient name.
     */
    private static String getRecipientName(int recipientID) {
        switch (recipientID) {
            case 0 : return "Andrew";
            case 1 : return "Travis";
            default : return "Unknown User";
        }
    }


    /*
     * Format the email subject/body.
     */
    private static Message formatMessage(Session session, String text, int code, int recipientID) {
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(credentials.username()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getRecipients(code).get(recipientID)));

            message.setSubject(getEmailSubject(code));

            if (text.isEmpty()) {
                message.setText("Error - Please disregard.");
            } else {
                String sb = "Good morning " + getRecipientName(recipientID) + ",\n\n" + text;
                message.setText(sb);
            }

            return message;
        } catch (MessagingException e) {
            System.out.println("Message failed to generate!");
        }

        return message;
    }
}
