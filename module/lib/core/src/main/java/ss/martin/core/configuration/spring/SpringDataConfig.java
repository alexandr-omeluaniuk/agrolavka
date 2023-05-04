package ss.martin.core.configuration.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Data configuration.
 * @author Alexandr Omeluaniuk
 */
@Configuration
@EntityScan("ss.entity")
public class SpringDataConfig {
}
