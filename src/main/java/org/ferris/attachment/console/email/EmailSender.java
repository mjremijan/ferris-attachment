package org.ferris.attachment.console.email;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;
import org.ferris.attachment.console.arguments.ArgumentParser;
import static org.ferris.attachment.console.email.EmailEvent.SEND_EMAIL_MESSAGE;
import org.ferris.attachment.console.log4j.Log4jRollingFileAppender;
import org.ferris.attachment.console.retry.ExceptionRetry;
import org.jboss.weld.experimental.Priority;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class EmailSender {
    @Inject
    protected Logger log;
    
    @Inject @EmailProperty("host")
    protected String host;
    
    @Inject @EmailProperty("port")
    protected String port;
    
    @Inject @EmailProperty("username")
    protected String username;
    
    @Inject @EmailProperty("password")
    protected String password;
    
    @Inject @EmailProperty("emailAddress")
    protected String emailAddress;
    
    @Inject
    protected ArgumentParser parser;
    
    @ExceptionRetry
    public void render(
        @Observes @Priority(SEND_EMAIL_MESSAGE) EmailEvent evnt
    ) throws MessagingException, IOException 
    {
        log.info(String.format("ENTER"));
        if (evnt.getAttachment() == null) {
            return;
        } 
        
        // Create MimeMultipart
        MimeMultipart content = new MimeMultipart("related");
        
        // html part
        {
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(evnt.getMessage(), "US-ASCII", "html");
            content.addBodyPart(textPart);
        }
        
        // Set the attachment
        {
			MimeBodyPart attachmentPart = new MimeBodyPart();
			FileDataSource fileDataSource = new FileDataSource(evnt.getAttachment()) {
				@Override
				public String getContentType() {
					return "application/octet-stream";
				}
			};
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
			attachmentPart.setFileName(fileDataSource.getName());
            content.addBodyPart(attachmentPart);
        }
        
        
        Session smtp = null;
        {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.socketFactory.port", port);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.port", port);
            
            smtp = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        }
        smtp.setDebug(true);
        smtp.setDebugOut(getPrintStream());
        
        String[] tos = parser.getTo().split(",");
        for (String emailTo : tos) {
            MimeMessage m = new MimeMessage(smtp);
            m.setSentDate(new Date());
            m.setContent(content);
            m.setSubject(evnt.getSubject());
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            m.setReplyTo(new InternetAddress[] {new InternetAddress(emailAddress)});
            InternetAddress ia = new InternetAddress(emailAddress);
            ia.setPersonal("Attachment");
            m.setFrom(ia);

            log.info(String.format("Attempt email to %s", emailTo));
            Transport.send(m);
        }
    }
    
    protected PrintStream getPrintStream() {
        Enumeration enu = log.getAllAppenders();
        while (enu.hasMoreElements()) {
            Object o = enu.nextElement();
            if (o instanceof Log4jRollingFileAppender) {
                return ((Log4jRollingFileAppender)o).getPrintStream();
            }
        }
        return System.out;
    }
}
