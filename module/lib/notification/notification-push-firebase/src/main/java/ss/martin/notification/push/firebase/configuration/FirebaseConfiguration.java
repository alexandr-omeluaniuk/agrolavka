package ss.martin.notification.push.firebase.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Firebase external configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".push")
public record FirebaseConfiguration(
        String firebaseConfigFilePath
) {}
