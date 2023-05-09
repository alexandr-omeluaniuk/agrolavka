package ss.martin.notification.email.smtp.test;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.notification.email.api.EmailService;
import ss.martin.notification.email.api.model.EmailAttachment;
import ss.martin.notification.email.api.model.EmailContact;
import ss.martin.notification.email.api.model.EmailRequest;
import ss.martin.test.AbstractComponentTest;

public class SmtpSenderTest extends AbstractComponentTest {
    
    @Autowired
    private EmailService emailService;
    
    @Test
    public void testSendEmail() {
        final var email = new EmailRequest(
                new EmailContact("Site admin", "agrolavka.by@gmail.com"), 
                new EmailContact[] { new EmailContact("Alex A", "alexandr.omelyaniuk@gmail.com") }, 
                "Test email", 
                "<b>Some text for test email....</b>",
                new EmailAttachment[] {
                    new EmailAttachment("test-attachment.txt", "plain/text", new File("src/test/resources/sample.txt"))
                }
        );
        
        assertDoesNotThrow(() -> emailService.sendEmail(email));
    }
    
    @Test
    public void testSendEmail_NoAttachments() {
        final var email = new EmailRequest(
                new EmailContact("Site admin", "agrolavka.by@gmail.com"), 
                new EmailContact[] { new EmailContact("Alex A", "alexandr.omelyaniuk@gmail.com") }, 
                "Test email", 
                "<b>Some text for test email....</b>",
                new EmailAttachment[0]
        );
        
        assertDoesNotThrow(() -> emailService.sendEmail(email));
    }
}
