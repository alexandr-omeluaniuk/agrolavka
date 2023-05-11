package ss.martin.security.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Domain configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".domain")
public record DomainConfiguration(
        String host,
        String email,
        String emailName
) {}
