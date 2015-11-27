package org.ferris.attachment.console.main;

import java.util.List;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class InitializationEvent {
    private List<String> args;

    public InitializationEvent(List<String> args) {
        this.args = args;
    }
    
    public List<String> getArgs() {
        return this.args;
    }
}
