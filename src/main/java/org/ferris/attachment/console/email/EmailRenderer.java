package org.ferris.attachment.console.email;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.ferris.attachment.console.arguments.ArgumentParser;
import static org.ferris.attachment.console.email.EmailEvent.RENDER_EMAIL_MESSAGE;
import org.ferris.attachment.console.message.MessageProperty;
import org.jboss.weld.experimental.Priority;


/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class EmailRenderer {
    
    @Inject
    protected Logger log;

    @Inject @MessageProperty("subject")
    protected String subjectTemplate;
    
    @Inject @MessageProperty("message")
    protected String messageTemplate;
    
    @Inject
    protected ArgumentParser parser;
    
    protected void render(
        @Observes @Priority(RENDER_EMAIL_MESSAGE) EmailEvent evnt
    ) { 
        log.info("ENTER");
        if (evnt.getAttachment() == null) {
            return;
        }
        
        log.debug(String.format("Rendering email subject.."));
        evnt.setSubject(
            String.format(
                  subjectTemplate
                , parser.getSubject()
                , evnt.getSentCount() + 1
                , evnt.getTotalCount()
            )
        );
        
        log.debug(String.format("Rendering email message.."));
        StringBuilder sp = new StringBuilder();
        sp.append("<!doctype html><html lang=\"en-US\">\n");
        sp.append("<body>\n");
        {
            sp.append(messageTemplate).append("\n");
            
        }        
        
        sp.append("</body>\n");
        sp.append("</html>\n");
 
        evnt.setMessage(sp.toString());
    }
}
