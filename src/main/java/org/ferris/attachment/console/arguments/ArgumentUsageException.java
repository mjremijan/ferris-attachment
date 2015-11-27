package org.ferris.attachment.console.arguments;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class ArgumentUsageException extends RuntimeException {

    private static final long serialVersionUID = 98746516549984494L;

    public ArgumentUsageException() {
        super(
              "\n"
            + "USAGE\n"
            + "  start-attachment.sh \n"
            + "    -unsent   /path/to/directory/for/unsent/attachments \n"
            + "    -sent     /path/to/directory/for/sent/attachments \n"            
            + "    -to       \"Rita Red <rita.red@somewhere.com>, Oscar Orange <oscar.orange@somewhere.com>\" \n"
            + "    -subject  \"Attachment email\""
            + "\n"
        );
    }
    
}
