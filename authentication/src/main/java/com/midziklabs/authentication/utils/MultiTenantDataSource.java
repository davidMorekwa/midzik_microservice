package com.midziklabs.authentication.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiTenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info( "MultiTenantDataSource determineCurrentLookupKey tenantId: " + CurrentTenantHolder.getCurrentTenant());
        return CurrentTenantHolder.getCurrentTenant();
    }
    
}
