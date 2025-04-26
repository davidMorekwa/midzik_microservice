package com.midziklabs.advertisement.feignclient.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authentication", url = "http://authentication:8082", path = "/api/v1/auth")
public interface AuthenticationClient {

    @GetMapping("/auth-user")
    ResponseEntity<?> getAuthenticatedUser(@RequestHeader("Authorization") String authorizationHeader, @RequestHeader("X-TenantID") String tenantId);
    
}