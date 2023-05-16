package ss.martin.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Simple Spring Boot application for testing purposes.
 * @author alex
 */
@SpringBootApplication(scanBasePackages = PlatformConfiguration.BASE_PACKAGE_SCAN)
@ConfigurationPropertiesScan(PlatformConfiguration.BASE_PACKAGE_SCAN)
public class TestSpringBootApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TestSpringBootApplication.class, args);
    }
}
