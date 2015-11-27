package org.ferris.attachment.console.attachment;

import java.io.File;
import static java.util.Arrays.sort;
import static java.util.Comparator.comparing;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.ferris.attachment.console.arguments.ArgumentParser;
import org.ferris.attachment.console.email.EmailEvent;
import static org.ferris.attachment.console.email.EmailEvent.GET_NEXT_UNSENT_ATTACHMENT;
import static org.ferris.attachment.console.email.EmailEvent.GET_SENT_COUNT;
import static org.ferris.attachment.console.email.EmailEvent.GET_UNSENT_COUNT;
import static org.ferris.attachment.console.email.EmailEvent.MARK_THE_UNSENT_ATTACHMENT_AS_SENT;
import org.jboss.weld.experimental.Priority;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class AttachmentHandler {

    @Inject
    protected Logger log;
    
    @Inject
    protected ArgumentParser parser;
               
    protected void getNextUnsentAttachement(
        @Observes @Priority(GET_NEXT_UNSENT_ATTACHMENT) EmailEvent evnt
    ) {
        log.info(String.format("ENTER"));
        
        File attachment 
            = null;
        
        File [] unsentFiles         
            = parser.getUnsentDirectory().listFiles(f -> f.isFile());      
        
        if (unsentFiles != null && unsentFiles.length >= 1) {
            log.info(String.format("Found %d unsent attachments", unsentFiles.length));
            sort(unsentFiles, comparing(File::getName));
            attachment = unsentFiles[0];
        } else {
            log.info(String.format("Found NO unsent attachments"));
        }
        
        evnt.setAttachment(attachment);
    }
    
    
    protected void getUnsentCount(
        @Observes @Priority(GET_UNSENT_COUNT) EmailEvent evnt
    ) {
        log.info(String.format("ENTER"));
        
        int cnt = 0;
        
        File [] files         
            = parser.getUnsentDirectory().listFiles(f -> f.isFile());      
        
        if (files != null) {
            cnt = files.length;
        } 
        
        evnt.setUnsentCount(cnt);
    }
    
    
    protected void getSentCount(
        @Observes @Priority(GET_SENT_COUNT) EmailEvent evnt
    ) {
        log.info(String.format("ENTER"));
        
        int cnt = 0;
        
        File [] files         
            = parser.getSentDirectory().listFiles(f -> f.isFile());      
        
        if (files != null) {
            cnt = files.length;
        } 
        
        evnt.setSentCount(cnt);
    }
    
    
    protected void markTheUnsentAttachmentAsSent(
        @Observes @Priority(MARK_THE_UNSENT_ATTACHMENT_AS_SENT) EmailEvent evnt
    ) {
        log.info(String.format("ENTER"));
        evnt.getAttachment().renameTo(
            new File(parser.getSentDirectory(), evnt.getAttachment().getName())
        );            
    }
}
