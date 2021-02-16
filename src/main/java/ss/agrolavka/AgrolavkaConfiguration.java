/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Site configuration.
 * @author alex
 */
@Configuration
@ConfigurationProperties(prefix = "agrolavka")
public class AgrolavkaConfiguration {
    
}
