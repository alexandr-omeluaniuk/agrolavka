package ss.martin.notification.email.api;

import ss.martin.notification.email.api.model.EmailRequest;

/**
 * Email service.
 * @author ss
 */
public interface EmailService {
    /**
     * Send email.
     * @param emailRequest email request.
     */
    void sendEmail(EmailRequest emailRequest);
}
