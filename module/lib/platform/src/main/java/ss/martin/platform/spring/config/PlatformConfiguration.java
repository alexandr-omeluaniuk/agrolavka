/*
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Platform configuration.
 * @author ss
 */
@Configuration
@ConfigurationProperties(prefix = "platform")
public class PlatformConfiguration {
    /** Super admin email. */
    private String superAdminEmail;
    /** Super admin first name. */
    private String superAdminFirstName;
    /** Super admin last name. */
    private String superAdminLastName;
    /** Super admin password. */
    private String superAdminPassword;
    /** External server domain. Uses for access to public resources. */
    private String serverDomain;
    /** Mailjet api key (If Mailjet is used as email service). */
    private String mailjetApiKey;
    /** Mailjet secret key (If Mailjet is used as email service). */
    private String mailjetSecretKey;
    /** Email contact name (for system emails). */
    private String systemEmailContactName;
    /** Email contact email (for system emails). */
    private String systemEmailContactEmail;
    /** Navigation configuration. */
    private URLConfiguration navigation;
    /** JWT configuration. */
    private JwtConfiguration jwt;
    /** Firebase config json file absolute path. */
    private String firebaseConfigFilePath;
    /** Image storage absolute path. */
    private String imagesStoragePath;
    /** Content-Security-Policy header value. */
    private String contentSecurityPolicy;
    // ========================================== SET & GET ===========================================================
    /**
     * @return the systemEmailContactName
     */
    public String getSystemEmailContactName() {
        return systemEmailContactName;
    }
    /**
     * @param systemEmailContactName the systemEmailContactName to set
     */
    public void setSystemEmailContactName(String systemEmailContactName) {
        this.systemEmailContactName = systemEmailContactName;
    }
    /**
     * @return the systemEmailContactEmail
     */
    public String getSystemEmailContactEmail() {
        return systemEmailContactEmail;
    }
    /**
     * @param systemEmailContactEmail the systemEmailContactEmail to set
     */
    public void setSystemEmailContactEmail(String systemEmailContactEmail) {
        this.systemEmailContactEmail = systemEmailContactEmail;
    }
    /**
     * @return the superAdminEmail
     */
    public String getSuperAdminEmail() {
        return superAdminEmail;
    }
    /**
     * @param superAdminEmail the superAdminEmail to set
     */
    public void setSuperAdminEmail(String superAdminEmail) {
        this.superAdminEmail = superAdminEmail;
    }
    /**
     * @return the superAdminFirstName
     */
    public String getSuperAdminFirstName() {
        return superAdminFirstName;
    }
    /**
     * @param superAdminFirstName the superAdminFirstName to set
     */
    public void setSuperAdminFirstName(String superAdminFirstName) {
        this.superAdminFirstName = superAdminFirstName;
    }
    /**
     * @return the superAdminLastName
     */
    public String getSuperAdminLastName() {
        return superAdminLastName;
    }
    /**
     * @param superAdminLastName the superAdminLastName to set
     */
    public void setSuperAdminLastName(String superAdminLastName) {
        this.superAdminLastName = superAdminLastName;
    }
    /**
     * @return the superAdminPassword
     */
    public String getSuperAdminPassword() {
        return superAdminPassword;
    }
    /**
     * @param superAdminPassword the superAdminPassword to set
     */
    public void setSuperAdminPassword(String superAdminPassword) {
        this.superAdminPassword = superAdminPassword;
    }
    /**
     * @return the serverDomain
     */
    public String getServerDomain() {
        return serverDomain;
    }
    /**
     * @param serverDomain the serverDomain to set
     */
    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }
    /**
     * @return the mailjetApiKey
     */
    public String getMailjetApiKey() {
        return mailjetApiKey;
    }
    /**
     * @param mailjetApiKey the mailjetApiKey to set
     */
    public void setMailjetApiKey(String mailjetApiKey) {
        this.mailjetApiKey = mailjetApiKey;
    }
    /**
     * @return the mailjetSecretKey
     */
    public String getMailjetSecretKey() {
        return mailjetSecretKey;
    }
    /**
     * @param mailjetSecretKey the mailjetSecretKey to set
     */
    public void setMailjetSecretKey(String mailjetSecretKey) {
        this.mailjetSecretKey = mailjetSecretKey;
    }
    /**
     * @return the navigation
     */
    public URLConfiguration getNavigation() {
        return navigation;
    }
    /**
     * @param navigation the navigation to set
     */
    public void setNavigation(URLConfiguration navigation) {
        this.navigation = navigation;
    }
    /**
     * @return the firebaseConfigFilePath
     */
    public String getFirebaseConfigFilePath() {
        return firebaseConfigFilePath;
    }
    /**
     * @param firebaseConfigFilePath the firebaseConfigFilePath to set
     */
    public void setFirebaseConfigFilePath(String firebaseConfigFilePath) {
        this.firebaseConfigFilePath = firebaseConfigFilePath;
    }
    /**
     * @return the jwt
     */
    public JwtConfiguration getJwt() {
        return jwt;
    }
    /**
     * @param jwt the jwt to set
     */
    public void setJwt(JwtConfiguration jwt) {
        this.jwt = jwt;
    }
    /**
     * @return the imagesStoragePath
     */
    public String getImagesStoragePath() {
        return imagesStoragePath;
    }
    /**
     * @param imagesStoragePath the imagesStoragePath to set
     */
    public void setImagesStoragePath(String imagesStoragePath) {
        this.imagesStoragePath = imagesStoragePath;
    }
    /**
     * @return the contentSecurityPolicy
     */
    public String getContentSecurityPolicy() {
        return contentSecurityPolicy;
    }
    /**
     * @param contentSecurityPolicy the contentSecurityPolicy to set
     */
    public void setContentSecurityPolicy(String contentSecurityPolicy) {
        this.contentSecurityPolicy = contentSecurityPolicy;
    }
}
