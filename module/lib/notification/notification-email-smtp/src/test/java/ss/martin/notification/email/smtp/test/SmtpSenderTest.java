package ss.martin.notification.email.smtp.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.martin.notification.email.api.EmailService;
import ss.martin.test.AbstractComponentTest;

public class SmtpSenderTest extends AbstractComponentTest {
    
    @Autowired
    private EmailService emailService;
    
    @Test
    public void testSendEmail() {
    
    }
}
