package it.polimi.meteocal.boundary;

import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author teo
 */
@Stateless
public class SendEmailBean {
    
        static Properties mailServerProperties;
        static Session getMailSession;
        static MimeMessage generateMailMessage;
        
        /**
         * Sends an Email to to, with subject and content emailBody
         * from the gmail account meteocalpolimi@gmail.com
         * 
         * @param to
         * @param subject
         * @param emailBody
         * @throws AddressException
         * @throws MessagingException 
         */
        public void generateAndSendEmail(String to,String subject,String emailBody) throws AddressException, MessagingException {
 
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(emailBody, "text/html");

        Transport transport = getMailSession.getTransport("smtp");

        transport.connect("smtp.gmail.com", "meteocalpolimi@gmail.com", "bubbole123");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}
