package ss.martin.notification.email.mailjet.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Mailjet external configuration.
 * @author ss
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".mailjet")
public record MailjetConfiguration(
        String apikey,
        String secretkey
) {}
