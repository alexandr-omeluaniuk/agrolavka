package ss.martin.platform.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Storage configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = PlatformConfiguration.PREFIX + ".storage")
public record StorageConfiguration(
        String path
) {}
