package ss.martin.security.configuration.spring;

import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ss.martin.security.api.AlertService;

/**
 * Module beans configuration.
 * @author alex
 */
@Configuration
class ModuleBeans {
    
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    @ConditionalOnMissingBean(AlertService.class)
    AlertService alertService() {
        final var logger = LoggerFactory.getLogger(AlertService.class);
        return (message, ex) -> {
            Optional.ofNullable(message).ifPresent(m -> logger.error(m));
            Optional.ofNullable(ex).ifPresent(e -> logger.error(e.getMessage(), e));
        };
    }
}
