package ss.martin.security.configuration.jwt;

/**
 * JWT constants.
 * @author alex
 */
public final class JwtConstants {
    
    private JwtConstants() {}
    
    /** JWT claim key for system user. */
    public static final String CLAIM_KEY_SYSTEM_USER = "SYSTEM_USER";
    /** JWT claim key for user-agent. */
    public static final String CLAIM_KEY_USER_AGENT = "USER_AGENT";
    /** JWT claim key for subscription. */
    public static final String CLAIM_KEY_SUBSCRIPTION = "SUBSCRIPTION";
}
