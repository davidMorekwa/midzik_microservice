package com.midziklabs.advertisement.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.midziklabs.advertisement.utils.JwtAuthFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Request in security ffilter chain");
        String base_url = "/api/v1";
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(r -> r
                        .requestMatchers(HttpMethod.POST, base_url + "/advertisement/").hasAuthority("User")
                        .requestMatchers(HttpMethod.GET, base_url + "/advertisement/location/id/{id}")
                        .hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.GET, base_url + "/advertisement/").hasAuthority("Administrator")
                        /* Category routes */
                        .requestMatchers(HttpMethod.GET, base_url + "/category/")
                        .hasAnyAuthority("Administrator", "User")
                        .requestMatchers(HttpMethod.GET, base_url + "/category/name/{name}")
                        .hasAnyAuthority("Administrator")
                        .requestMatchers(HttpMethod.GET, base_url + "/category/id/{id}").hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.DELETE, base_url + "/category/").hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.POST, base_url + "/category/").hasAuthority("Administrator")
                        /* Location routes */
                        .requestMatchers(HttpMethod.PUT, base_url + "/category/{id}").hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.POST, base_url + "/location/").hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.GET, base_url + "/location/")
                        .hasAnyAuthority("Administrator", "User")
                        .requestMatchers(HttpMethod.PUT, base_url + "/location/{id}").hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.GET, base_url + "/location/id/{id}").hasAuthority("Administrator")
                        .requestMatchers(HttpMethod.DELETE, base_url + "/location/{id}").hasAuthority("Administrator")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

}
