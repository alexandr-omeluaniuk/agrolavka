package ss.martin.notification.email.smtp;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmail(final EmailRequest emailRequest) throws Exception {
        LOG.debug(emailRequest.toString());
        final var message = new SimpleMailMessage(); 
        message.setFrom(emailRequest.sender().email());
        message.setTo(Stream.of(emailRequest.recipients()).map(recipient -> recipient.email()).collect(Collectors.toList()).toArray(new String[0])); 
        message.setSubject(emailRequest.subject()); 
        message.setText(emailRequest.message());
        emailSender.send(message);
    }
    
}
