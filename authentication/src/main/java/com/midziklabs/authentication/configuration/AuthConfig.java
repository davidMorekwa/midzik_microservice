package com.midziklabs.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.midziklabs.authentication.utils.JwtAuthenticationFilter;
import com.midziklabs.authentication.utils.TenentIdFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TenentIdFilter tenantIdFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/welcome").permitAll()
                        .requestMatchers("/api/v1/auth/roles").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tenantIdFilter, JwtAuthenticationFilter.class);
        return http.build();
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource(){
    
    // CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(List.of("http://localhost:3000"));
    // configuration.setAllowedOrigins(List.of("GET", "POST"));
    // configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    
    // source.registerCorsConfiguration("/**", configuration);
    
    // return source;
    // }
}
