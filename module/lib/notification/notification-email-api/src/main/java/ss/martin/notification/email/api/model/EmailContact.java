package ss.martin.notification.email.api.model;

/**
 * Email contact.
 * @author alex
 */
public record EmailContact(
        String name,
        String email
) {
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <").append(email).append(">");
        return sb.toString();
    }
}
