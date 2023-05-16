package ss.martin.security.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
public class JwtTokenUtil implements Serializable {
    /** UID. */
    private static final long serialVersionUID = -2550185165626007488L;
    
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    /** Platform configuration. */
    @Autowired
    private SecurityConfiguration securityConfiguration;
    
    /**
     * Retrieve username from jwt token.
     * @param token JWT token.
     * @return username.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Generate JWT token.
     * @param principal principal.
     * @return JWT token.
     */
    public String generateToken(final UserPrincipal principal) {
        return ((ThrowingSupplier<String>) () -> {
            final var claims = Jwts.claims();
            claims.put(JwtConstants.CLAIM_KEY_SYSTEM_USER, serializeObjectToJson(principal.getUser()));
            claims.put(JwtConstants.CLAIM_KEY_USER_AGENT, serializeObjectToJson(principal.getUserAgent()));
            claims.put(
                    JwtConstants.CLAIM_KEY_SUBSCRIPTION, 
                    serializeObjectToJson(principal.getUser().getSubscription())
            );
            return doGenerateToken(claims, principal.getUser().getEmail());
        }).get();
    }

    private String doGenerateToken(Claims claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(
                        securityConfiguration.tokenValidityPeriodInHours()
                ))).signWith(KEY).compact();
    }
    
    public Boolean validateToken(String token, String email) {
        final String username = getUsernameFromToken(token);
        return (username.equals(email) && !isTokenExpired(token));
    }
    
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * Serialize object to json string.
     * @param obj object.
     * @return json string.
     * @throws IOException serialization error.
     */
    private String serializeObjectToJson(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        objectMapper.writeValue(baos, obj);
        String json = new String(baos.toByteArray(), "UTF-8");
        baos.close();
        return json;
    }
}
