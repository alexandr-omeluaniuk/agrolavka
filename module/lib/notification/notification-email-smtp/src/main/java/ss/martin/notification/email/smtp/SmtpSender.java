package ss.martin.notification.email.smtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ss.martin.notification.email.api.EmailService;
import ss.martin.notification.email.api.model.EmailRequest;

/**
 * Send email via SMTP
 * @author alex
 */
@Service("SmtpSender")
class SmtpSender implements EmailService {
    
    private static final Logger LOG = LoggerFactory.getLogger(SmtpSender.class);

    @Override
    public void sendEmail(final EmailRequest emailRequest) throws Exception {
        LOG.debug(emailRequest.toString());
        
    }
    
}
