package ss.martin.security.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.security.context.UserPrincipal;

/**
 * JWT token util.
 * @author alex
 */
@Component
public class JwtTokenUtil {
    
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    @Autowired
    private JwtTokenDateFactory dateFactory;

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
            return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(dateFactory.getIssuedAtDate())
                .setExpiration(dateFactory.getExpiredAtDate())
                .signWith(KEY)
                .compact();
        }).get();
    }

    public String getSubject(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
    }
    
    private Claims createClaims(final UserPrincipal principal) throws Exception {
        final var claims = Jwts.claims();
        claims.put(JwtConstants.CLAIM_KEY_SYSTEM_USER, serializeObjectToJson(principal.getUser()));
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
