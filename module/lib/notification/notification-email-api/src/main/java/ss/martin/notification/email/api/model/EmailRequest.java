package ss.martin.notification.email.api.model;

/**
 * Email request.
 * @author ss
 */
public record EmailRequest(
        EmailContact sender,
        EmailContact[] recipients,
        String subject,
        String message,
        EmailAttachment[] attachments
) {
    
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
