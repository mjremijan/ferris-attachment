package org.ferris.attachment.console.arguments;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import org.ferris.attachment.console.main.InitializationEvent;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
@ApplicationScoped
public class ArgumentParserProducer {

    protected ArgumentParser parser;
    
    protected void init(@Observes InitializationEvent evnt) {
        parser = new ArgumentParser(evnt.getArgs());
    }
    
    @Produces
    protected ArgumentParser produceArgumentParser() {
        return parser;
    }
}
