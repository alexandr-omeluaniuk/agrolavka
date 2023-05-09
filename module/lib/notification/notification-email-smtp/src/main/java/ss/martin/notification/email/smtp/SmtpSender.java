package ss.martin.notification.email.smtp;

import jakarta.mail.MessagingException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.function.ThrowingConsumer;
import ss.martin.notification.email.api.EmailService;
import ss.martin.notification.email.api.model.EmailRequest;

/**
 * Send email via SMTP.
 * @author alex
 */
@Service("SmtpSender")
class SmtpSender implements EmailService {
    
    private static final Logger LOG = LoggerFactory.getLogger(SmtpSender.class);
    
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmail(final EmailRequest emailRequest) {
        LOG.debug(emailRequest.toString());
        final ThrowingConsumer<EmailRequest> sendFunction = this::doSend;
        sendFunction.accept(emailRequest);
    }
    
    private void doSend(final EmailRequest emailRequest) throws MessagingException {
        final var message = emailSender.createMimeMessage();
        final var helper = new MimeMessageHelper(message, emailRequest.attachments().length > 0, "UTF-8");
        helper.setFrom(emailRequest.sender().toString());
        final var recipients = Stream.of(emailRequest.recipients()).map(recipient -> recipient.toString())
                .collect(Collectors.toList()).toArray(new String[0]);
        helper.setTo(recipients);
        helper.setSubject(emailRequest.subject());
        helper.setText(emailRequest.message(), true);
        for (final var attachment : emailRequest.attachments()) {
            helper.addAttachment(
                    attachment.name(),
                    new FileSystemResource(attachment.file()),
                    attachment.contentType()
            );
        }
        emailSender.send(message);
    }
}
