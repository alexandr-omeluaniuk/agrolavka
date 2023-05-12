package ss.martin.security.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * JWT configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".security")
public record SecurityConfiguration(
        Integer tokenValidityPeriodInHours,
        String contentSecurityPolicy
) {}
