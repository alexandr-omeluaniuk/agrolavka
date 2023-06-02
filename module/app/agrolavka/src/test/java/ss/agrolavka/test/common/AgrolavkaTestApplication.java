package ss.agrolavka.test.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ss.martin.base.constants.PlatformConfiguration;

@SpringBootApplication(scanBasePackages = { PlatformConfiguration.BASE_PACKAGE_SCAN, "ss.agrolavka" })
@ConfigurationPropertiesScan({ PlatformConfiguration.BASE_PACKAGE_SCAN, "ss.agrolavka" })
public class AgrolavkaTestApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AgrolavkaTestApplication.class, args);
    }
}
