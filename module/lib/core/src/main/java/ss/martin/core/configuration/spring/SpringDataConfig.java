package ss.martin.core.configuration.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Spring Data configuration.
 * @author Alexandr Omeluaniuk
 */
@Configuration
@EntityScan(PlatformConfiguration.JPA_PACKAGE_SCAN)
public class SpringDataConfig {
}
