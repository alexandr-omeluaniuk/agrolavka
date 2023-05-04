/*
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.wrapper;

import java.io.File;

/**
 * Email request.
 * @author ss
 */
public class EmailRequest {
    /** Sender. */
    private EmailContact sender;
    /** Recipients. */
    private EmailContact[] recipients = new EmailContact[0];
    /** Subject. */
    private String subject;
    /** Message. */
    private String message;
    /** Attachments. */
    private EmailAttachment[] attachments;
    // ===================================== SET & GET ================================================================
    /**
     * @return the sender
     */
    public EmailContact getSender() {
        return sender;
    }
    /**
     * @param sender the sender to set
     */
    public void setSender(EmailContact sender) {
        this.sender = sender;
    }
    /**
     * @return the recipients
     */
    public EmailContact[] getRecipients() {
        return recipients;
    }
    /**
     * @param recipients the recipients to set
     */
    public void setRecipients(EmailContact[] recipients) {
        this.recipients = recipients;
    }
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * @return the attachments
     */
    public EmailAttachment[] getAttachments() {
        return attachments;
    }
    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(EmailAttachment[] attachments) {
        this.attachments = attachments;
    }
    /**
     * Email attachment.
     */
    public static class EmailAttachment {
        /** Name. */
        private String name;
        /** Content type. */
        private String contentType;
        /** File. */
        private File file;
        /**
         * Constructor.
         */
        public EmailAttachment() {
        }
        /**
         * Constructor.
         * @param name name.
         * @param contentType content type.
         * @param file file.
         */
        public EmailAttachment(String name, String contentType, File file) {
            this.name = name;
            this.contentType = contentType;
            this.file = file;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the contentType
         */
        public String getContentType() {
            return contentType;
        }
        /**
         * @param contentType the contentType to set
         */
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
        /**
         * @return the file
         */
        public File getFile() {
            return file;
        }
        /**
         * @param file the file to set
         */
        public void setFile(File file) {
            this.file = file;
        }
    }
    /**
     * Email contact.
     */
    public static class EmailContact {
        /** Contact fullname. */
        private String name;
        /** Contact email. */
        private String email;
        /**
         * Constructor.
         */
        public EmailContact() {
        }
        /**
         * Constructor.
         * @param name contact fullname.
         * @param email contact email.
         */
        public EmailContact(String name, String email) {
            this.name = name;
            this.email = email;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the email
         */
        public String getEmail() {
            return email;
        }
        /**
         * @param email the email to set
         */
        public void setEmail(String email) {
            this.email = email;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" <").append(email).append(">");
            return sb.toString();
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Email request\n");
        sb.append("  >> sender: ").append(sender == null ? "<>" : sender.toString()).append("\n");
        sb.append("  >> recipients:\n");
        for (EmailContact ec : recipients) {
            sb.append("    >").append(ec.toString()).append("\n");
        }
        sb.append("  >> subject: ").append(subject).append("\n");
        return sb.toString();
    }
}
