// package com.midziklabs.gateway.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
// import org.springframework.security.config.web.server.ServerHttpSecurity;
// import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.server.SecurityWebFilterChain;

// @EnableWebFluxSecurity
// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http){
//         http
//             .csrf(csrf -> csrf.disable())
//             .authorizeExchange(ex -> ex
//                 .pathMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
//                 .anyExchange().authenticated()
//             )
//             .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

//         return http.build();
        
//     }
// }
