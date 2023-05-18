package ss.martin.security.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.security.configuration.external.SecurityConfiguration;
import ss.martin.security.context.UserPrincipal;

/**
 * JWT token util.
 * @author alex
 */
@Component
public class JwtTokenUtil {
    
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    /** Platform configuration. */
    @Autowired
    private SecurityConfiguration securityConfiguration;
    
    /**
     * Retrieve username from jwt token.
     * @param token JWT token.
     * @return username.
     */
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaimsFromToken(token));
    }
    
    /**
     * Generate JWT token.
     * @param principal principal.
     * @return JWT token.
     */
    public String generateToken(final UserPrincipal principal) {
        return ((ThrowingSupplier<String>) () -> {
            final var claims = createClaims(principal);
            final var subject = principal.getUser().getEmail();
            final var issuedAt = new Date(System.currentTimeMillis());
            final var expirationDate = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(
                securityConfiguration.tokenValidityPeriodInHours()
            ));
            return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(KEY)
                .compact();
        }).get();
    }

    public Boolean validateToken(final String token, final String email) {
        return getUsernameFromToken(token).equals(email) && !isTokenExpired(token);
    }
    
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(final String token) {
        final var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    private Claims createClaims(final UserPrincipal principal) throws Exception {
        final var claims = Jwts.claims();
        claims.put(JwtConstants.CLAIM_KEY_SYSTEM_USER, serializeObjectToJson(principal.getUser()));
        claims.put(JwtConstants.CLAIM_KEY_USER_AGENT, serializeObjectToJson(principal.getUserAgent()));
        claims.put(
                JwtConstants.CLAIM_KEY_SUBSCRIPTION, 
                serializeObjectToJson(principal.getUser().getSubscription())
        );
        return claims;
    }
    
    /**
     * Serialize object to json string.
     * @param obj object.
     * @return json string.
     * @throws IOException serialization error.
     */
    private String serializeObjectToJson(final Object obj) throws IOException {
        try (final var baos = new ByteArrayOutputStream()) {
            new ObjectMapper().writeValue(baos, obj);
            return new String(baos.toByteArray(), "UTF-8");
        }
    }
}
