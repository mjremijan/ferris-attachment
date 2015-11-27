package org.ferris.attachment.console.email;

import java.io.File;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class EmailEvent {

    public static final int GET_NEXT_UNSENT_ATTACHMENT = 10;
    public static final int GET_UNSENT_COUNT = 20;
    public static final int GET_SENT_COUNT = 30;
    public static final int RENDER_EMAIL_MESSAGE = 40;
    public static final int PRINT_EMAIL_MESSAGE = 50;
    public static final int SEND_EMAIL_MESSAGE = 60;
    public static final int MARK_THE_UNSENT_ATTACHMENT_AS_SENT = 70;

    private String subject;
    private String message;
    private File attachment;
    private int sentCount;
    private int unsentCount;

    public int getUnsentCount() {
        return unsentCount;
    }

    public void setUnsentCount(int unsentCount) {
        this.unsentCount = unsentCount;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalCount() {
        return getUnsentCount() + getSentCount();
    }
}
