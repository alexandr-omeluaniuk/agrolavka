package ss.martin.security.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Super user external configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".superAdmin")
public record SuperUserConfiguration(
        String email,
        String password,
        String lastname,
        String firstname
) {}
