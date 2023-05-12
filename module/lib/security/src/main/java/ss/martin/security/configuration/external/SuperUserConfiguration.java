package ss.martin.security.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Super user external configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".admin")
public record SuperUserConfiguration(
        String email,
        String firstname,
        String lastname,
        String password
) {}
