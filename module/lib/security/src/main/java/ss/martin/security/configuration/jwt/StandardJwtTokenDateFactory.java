package ss.martin.security.configuration.jwt;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ss.martin.security.configuration.external.SecurityConfiguration;

/**
 * Standard JWT token data factory implementation.
 * @author alex
 */
@Component
class StandardJwtTokenDateFactory implements JwtTokenDateFactory {
    
    /** Platform configuration. */
    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Override
    public Date getIssuedAtDate() {
        return new Date();
    }

    @Override
    public Date getExpiredAtDate() {
        return new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(
            securityConfiguration.tokenValidityPeriodInHours()
        ));
    }
    
}
