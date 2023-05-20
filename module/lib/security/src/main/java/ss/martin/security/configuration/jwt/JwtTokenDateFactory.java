package ss.martin.security.configuration.jwt;

import java.util.Date;

/**
 * Date factory for JWT token.
 * @author alex
 */
public interface JwtTokenDateFactory {
    
    /**
     * Get issued at date for JWT token.
     * @return date.
     */
    Date getIssuedAtDate();
    
    /**
     * Get expired at date for JWT token.
     * @return date.
     */
    Date getExpiredAtDate();
}
