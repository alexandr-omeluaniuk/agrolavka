package ss.martin.security.configuration.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ss.entity.security.SystemUser;

/**
 * Spring Data configuration.
 * @author Alexandr Omeluaniuk
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class DataAuditorConfig {
    /** Auditor aware. */
    @Autowired
    private AuditorAware<SystemUser> auditorAware;
    
    @Bean
    public AuditorAware<SystemUser> auditorAware() {
        return auditorAware;
    }
}
