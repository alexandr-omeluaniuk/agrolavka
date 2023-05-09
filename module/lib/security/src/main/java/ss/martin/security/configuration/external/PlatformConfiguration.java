package ss.martin.security.configuration.external;

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
    /** Email contact name (for system emails). */
    private String systemEmailContactName;
    /** Email contact email (for system emails). */
    private String systemEmailContactEmail;
    /** Navigation configuration. */
    private URLConfiguration navigation;
    /** JWT configuration. */
    private JwtConfiguration jwt;
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
