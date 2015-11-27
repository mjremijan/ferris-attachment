package org.ferris.attachment.console.arguments;

import java.io.File;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
@ApplicationScoped
public class ArgumentParser {

    private List<String> args;
    
    private File sent;
    private File unsent;
    private String to;
    private String subject;
    
    private String parseForString(String argKey) {
        int argKeyIdx = 0;
        for (; argKeyIdx < args.size(); argKeyIdx++) {
            if (argKey.equals(args.get(argKeyIdx))) {
                break;
            }
        }
        
        if (argKeyIdx >= args.size()) {
            throw new ArgumentUsageException();
        } else {
            argKeyIdx++;
            if (argKeyIdx >= args.size()) {
                throw new ArgumentUsageException();
            }
        }
        
        return args.get(argKeyIdx);        
    }
    
    private File parseForDirectory(String argKey) {
        File dir = new File(parseForString(argKey));        
        if (!dir.exists() || !dir.isDirectory()) {
            throw new ArgumentUsageException();
        }
        return dir;
    }
    
    private void setArgs(List<String> args) {
        if (args == null || args.isEmpty()) {
            throw new ArgumentUsageException();
        }
        this.args = args;
    }
    
    public ArgumentParser(List<String> args) {
        setArgs(args);
        sent = parseForDirectory("-sent");
        unsent = parseForDirectory("-unsent");
        to = parseForString("-to");
        subject = parseForString("-subject");
    }
    
    public File getSentDirectory() {
        return sent;
    }
    
    public File getUnsentDirectory() {
        return unsent;
    }
    
    public String getTo() {
        return to;
    }
    
    public String getSubject() {
        return subject;
    }
}
