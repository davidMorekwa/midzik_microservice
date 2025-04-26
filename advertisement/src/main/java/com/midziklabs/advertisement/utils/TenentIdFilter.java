package com.midziklabs.advertisement.utils;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenentIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenantId = request.getHeader("X-TenantID");
        log.info("TenentIdFilter tenantId: " + tenantId);
        try {
            if (tenantId != null) {
                CurrentTenantHolder.setCurrentTenant(tenantId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing tenant ID");
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tenant ID");
        } finally {
            CurrentTenantHolder.clear();
        }
    }

}
