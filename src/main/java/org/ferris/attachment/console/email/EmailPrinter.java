package org.ferris.attachment.console.email;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import static org.ferris.attachment.console.email.EmailEvent.PRINT_EMAIL_MESSAGE;
import org.jboss.weld.experimental.Priority;


/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class EmailPrinter {
    
    @Inject
    protected Logger log;
    
    public void render(
        @Observes @Priority(PRINT_EMAIL_MESSAGE) EmailEvent evnt
    ) { 
        log.info("ENTER");
        if (evnt.getAttachment() == null) {
            return;
        } 
        log.info(String.format("\nEmail subject:\n%s",evnt.getSubject()));
        log.info(String.format("\nEmail message:\n%s",evnt.getMessage()));
        log.info(String.format("\nEmail attachment:\n%s", evnt.getAttachment().getName()));
    }
}
