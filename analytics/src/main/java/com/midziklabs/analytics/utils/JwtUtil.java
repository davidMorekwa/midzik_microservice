package com.midziklabs.analytics.utils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private Key getSignInKey() {
        log.info("Secret key: "+secretKey);
        byte[] keyBytes = Base64.getDecoder().decode(this.secretKey);
        log.info("Key bytes"+keyBytes.toString());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        Claims c = extractClaims(token);
        return c.getSubject();
    }

    public Claims extractClaims(String token) {
        log.info("Getting token claims");
        Key key = getSignInKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);

            log.info("Claims"+claims.toString());
            // Perform additional checks if needed (e.g., issuer, audience)
            return !isTokenExpired(claims);

        } catch (ExpiredJwtException e) {
            log.error("Token expired", e.getMessage());
            return false; // Token expired
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token", e.getMessage());
            return false; // Malformed token
        } catch (SignatureException e) {
            log.error("Invalid JWT token signature", e.getMessage());
            return false; // Invalid signature
        } catch (Exception e) {
            log.error("an erro occured when trying to validate JWT token", e.getMessage());
            log.error("Error", e);
            return false; // Other exceptions
        }
    }
    
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
