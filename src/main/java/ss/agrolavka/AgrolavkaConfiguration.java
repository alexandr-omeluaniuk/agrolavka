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
    /** My sklad: username. */
    private String mySkladUsername;
    /** My sklad: password. */
    private String mySkladPassword;
    /**
     * @return the mySkladUsername
     */
    public String getMySkladUsername() {
        return mySkladUsername;
    }
    /**
     * @param mySkladUsername the mySkladUsername to set
     */
    public void setMySkladUsername(String mySkladUsername) {
        this.mySkladUsername = mySkladUsername;
    }
    /**
     * @return the mySkladPassword
     */
    public String getMySkladPassword() {
        return mySkladPassword;
    }
    /**
     * @param mySkladPassword the mySkladPassword to set
     */
    public void setMySkladPassword(String mySkladPassword) {
        this.mySkladPassword = mySkladPassword;
    }
}
