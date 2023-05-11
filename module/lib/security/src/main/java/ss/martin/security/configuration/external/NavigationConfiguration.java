package ss.martin.security.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * URL configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".navigation")
public record NavigationConfiguration(
        String login,
        String logout,
        String loginPage,
        String protectedRest,
        String registrationVerification,
        String views
) {}
