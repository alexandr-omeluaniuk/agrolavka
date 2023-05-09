package ss.martin.notification.email.smtp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * SMTP server configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX)
public class SmtpConfiguration {
    
}
