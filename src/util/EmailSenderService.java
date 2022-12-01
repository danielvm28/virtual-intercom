package util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Code adapted from <a href="https://www.adictosaltrabajo.com/2008/12/01/javamail/"> "Adictos al trabajo" </a> website
 */
public class EmailSenderService {

    private final Properties properties = new Properties();

    private Session session;

    private void init() {

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port",25);
        properties.put("mail.smtp.user", "daniel.valencia.manrique@gmail.com");
        properties.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(properties);
    }

    public void sendEmail(String subject, String text, String receiver) throws MessagingException{

        init();
        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
        message.setSubject(subject);
        message.setText(text);
        Transport t = session.getTransport("smtp");
        t.connect((String)properties.get("mail.smtp.user"), "ztnmdmvwtohdxovu");
        t.sendMessage(message, message.getAllRecipients());
        t.close();

    }

}