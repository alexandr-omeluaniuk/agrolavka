/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ss.martin.base.constants.PlatformConfiguration;

/**
 * Application entry point.
 * @author ss
 */
@SpringBootApplication
@ConfigurationPropertiesScan({ PlatformConfiguration.BASE_PACKAGE_SCAN, "ss.agrolavka" })
public class Main extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
