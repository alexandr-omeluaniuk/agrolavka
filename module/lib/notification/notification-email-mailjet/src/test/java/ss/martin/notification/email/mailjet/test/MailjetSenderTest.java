package ss.martin.notification.email.mailjet.test;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.notification.email.api.EmailService;
import ss.martin.notification.email.api.model.EmailAttachment;
import ss.martin.notification.email.api.model.EmailContact;
import ss.martin.notification.email.api.model.EmailRequest;
import ss.martin.test.AbstractComponentTest;

public class MailjetSenderTest extends AbstractComponentTest {
    
    @Autowired
    private EmailService emailService;
    
    @Test
    public void testSendEmail() {
        final var email = new EmailRequest(
                new EmailContact("Site admin", "agrolavka.by@gmail.com"), 
                new EmailContact[] { new EmailContact("Alex A", "starshistrelok@gmail.com") }, 
                "Test email", 
                "Some text for test email....",
                new EmailAttachment[] {
                    new EmailAttachment("test-attachment.txt", "plain/text", new File("src/test/resources/sample.txt"))
                }
        );
        
        assertDoesNotThrow(() -> emailService.sendEmail(email));
    }
}
