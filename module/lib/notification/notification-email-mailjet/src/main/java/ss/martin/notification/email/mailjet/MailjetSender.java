package ss.martin.notification.email.mailjet;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.resource.Emailv31;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.notification.email.api.EmailService;
import ss.martin.notification.email.api.model.EmailAttachment;
import ss.martin.notification.email.api.model.EmailRequest;
import ss.martin.notification.email.mailjet.configuration.MailjetConfiguration;

/**
 * Mailjet email service implementation.
 * @author ss
 */
@Service("MailjetSender")
class MailjetSender implements EmailService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MailjetSender.class);
    /** Platform configuration. */
    @Autowired
    private MailjetConfiguration config;
    
    @Override
    public void sendEmail(final EmailRequest emailRequest) {
        LOG.debug(emailRequest.toString());
        ((ThrowingRunnable) () -> {
            final var clientOptions = ClientOptions.builder()
                    .apiKey(config.apikey())
                    .apiSecretKey(config.secretkey())
                    .build();
            final var client = new MailjetClient(clientOptions);
            final var response = client.post(prepareEmail(emailRequest));
            LOG.debug("response status: " + response.getStatus());
            LOG.debug("response data: " + response.getData());
        }).run();
    }
    
    private MailjetRequest prepareEmail(final EmailRequest emailRequest) throws IOException {
        final var recipients = new JSONArray();
        for (final var recipient : emailRequest.recipients()) {
            recipients.put(new JSONObject().put("Email", recipient.email()).put("Name", recipient.name()));
        }
        final var arrAttachments = new JSONArray();
        if (emailRequest.attachments() != null) {
            for (EmailAttachment attachment : emailRequest.attachments()) {
                String base64 = Base64.getEncoder().encodeToString(
                           Files.readAllBytes(Paths.get(attachment.file().toURI())));
                JSONObject obj = new JSONObject()
                        .put("ContentType", attachment.contentType())
                        .put("Filename", attachment.name())
                        .put("Base64Content", base64);
                arrAttachments.put(obj);
            }
        }
        final var props = new JSONArray()
            .put(new JSONObject().put(Emailv31.Message.FROM, new JSONObject()
            .put("Email", emailRequest.sender().email())
            .put("Name", emailRequest.sender().name()))
            .put(Emailv31.Message.TO, recipients)
            .put(Emailv31.Message.SUBJECT, emailRequest.subject())
            .put(Emailv31.Message.TEXTPART, emailRequest.message() == null ? "" : emailRequest.message())
            .put(Emailv31.Message.HTMLPART, "").put(Emailv31.Message.ATTACHMENTS, arrAttachments));
        return new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, props);
    }
}
