// package com.midziklabs.gateway.util;

// import java.security.Key;
// import java.util.List;
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// @Component
// public class JwtUtil {

//     @Value("${security.jwt.secret-key}")
//     private String secretKey;

//     private Key getSignInKey() {
//         byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//         return Keys.hmacShaKeyFor(keyBytes);
//     }

//     public Claims extractClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(getSignInKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }

//     public boolean isTokenValid(String token) {
//         try {
//             extractClaims(token);
//             return true;
//         } catch (Exception e) {
//             return false;
//         }
//     }
// }
