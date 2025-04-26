package com.midziklabs.advertisement.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; // Use ConcurrentHashMap for thread safety

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean; // Import InitializingBean
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component; // Make it a component for injection

import com.midziklabs.advertisement.service.TenantDataSourceService; // Import the service

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MultiTenantDataSource extends AbstractRoutingDataSource implements InitializingBean { 

    @Autowired
    private TenantDataSourceService tenantDataSourceService;
    private final Map<Object, Object> internalTargetDataSources = new ConcurrentHashMap<>();

    /**
     * Sets the initial target DataSources. This might be empty if no tenants
     * are pre-loaded by the service.
     */
    @Override
    public void afterPropertiesSet() {
        this.internalTargetDataSources.putAll(tenantDataSourceService.getDataSource());
        super.setTargetDataSources(this.internalTargetDataSources);
        super.afterPropertiesSet();
        log.info("MultiTenantDataSource initialized with {} initial target(s).", internalTargetDataSources.size());
    }

    /**
     * Determines the key (tenant ID) for the current request.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = CurrentTenantHolder.getCurrentTenant();
        return tenantId;
    }

    /**
     * Overridden to dynamically add DataSources if they are not already known
     * by the router.
     */
    @Override
    protected DataSource determineTargetDataSource() {
        Object lookupKey = determineCurrentLookupKey();
        if (lookupKey == null) {
            log.warn("No tenant ID found in CurrentTenantHolder.");
            return super.determineTargetDataSource();
        }
        if (internalTargetDataSources.containsKey(lookupKey)) {
             log.debug("Found existing DataSource for tenant: {}", lookupKey);
             return super.determineTargetDataSource();
        }
        log.info("DataSource for tenant '{}' not found. Attempting to load.", lookupKey);
        DataSource dataSource = tenantDataSourceService.getDataSource(lookupKey.toString());

        if (dataSource != null) {
            log.info("Successfully loaded DataSource for tenant: {}", lookupKey);
            this.internalTargetDataSources.put(lookupKey, dataSource);
            super.setTargetDataSources(this.internalTargetDataSources);
            super.afterPropertiesSet();
             return super.determineTargetDataSource();
        } else {
            log.error("Failed to create or find DataSource for tenant: {}", lookupKey);
             return super.determineTargetDataSource();
        }
    }
}
