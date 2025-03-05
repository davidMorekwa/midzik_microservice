// package com.midziklabs.gateway.util;

// import org.springframework.http.HttpHeaders;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.web.server.ServerWebExchange;
// import org.springframework.web.server.WebFilter;
// import org.springframework.web.server.WebFilterChain;
// import reactor.core.publisher.Mono;
// import org.springframework.stereotype.Component;
// import org.springframework.beans.factory.annotation.Autowired;
// import io.jsonwebtoken.Claims;
// import java.util.List;

// @Component
// public class AuthenticationFilter implements WebFilter {

//     @Autowired
//     private JwtUtil jwtUtil;

//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//         ServerHttpRequest request = exchange.getRequest();

//         if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//             return handleUnauthorized(exchange);
//         }

//         String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             return handleUnauthorized(exchange);
//         }

//         String token = authHeader.substring(7);
//         if (!jwtUtil.isTokenValid(token)) {
//             return handleUnauthorized(exchange);
//         }

//         Claims claims = jwtUtil.extractClaims(token);
//         List<String> roles = claims.get("roles", List.class);

//         // Forward user roles to microservices via headers
//         ServerHttpRequest modifiedRequest = exchange.getRequest()
//                 .mutate()
//                 .header("X-User-Email", claims.getSubject())
//                 .header("X-User-Roles", String.join(",", roles))
//                 .build();

//         return chain.filter(exchange.mutate().request(modifiedRequest).build());
//     }

//     private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
//         ServerHttpResponse response = exchange.getResponse();
//         response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//         return response.setComplete();
//     }
// }
