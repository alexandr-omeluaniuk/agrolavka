/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import ss.martin.security.configuration.external.PlatformConfiguration;
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
    private PlatformConfiguration configuration;
    /**
     * Retrieve username from jwt token.
     * @param token JWT token.
     * @return username.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserPrincipal principal) throws IOException {
        Claims claims = Jwts.claims();
        claims.put(JwtConstants.CLAIM_KEY_SYSTEM_USER, serializeObjectToJSON(principal.getUser()));
        claims.put(JwtConstants.CLAIM_KEY_USER_AGENT, serializeObjectToJSON(principal.getUserAgent()));
        claims.put(JwtConstants.CLAIM_KEY_SUBSCRIPTION, serializeObjectToJSON(principal.getUser().getSubscription()));
        return doGenerateToken(claims, principal.getUser().getEmail());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string 
    private String doGenerateToken(Claims claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(
                        configuration.getJwt().getValidityPeriodInHours()
                ))).signWith(KEY).compact();
    }
    public Boolean validateToken(String token, String email) {
        final String username = getUsernameFromToken(token);
        return (username.equals(email) && !isTokenExpired(token));
    }
    /**
     * Serialize object to json string.
     * @param obj object.
     * @return json string.
     * @throws Exception serialization error.
     */
    private String serializeObjectToJSON(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        objectMapper.writeValue(baos, obj);
        String json = new String(baos.toByteArray(), "UTF-8");
        baos.close();
        return json;
    }
}
