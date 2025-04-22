package com.midziklabs.authentication.utils;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantDataSourceRouter extends AbstractRoutingDataSource {
    private Map<Object, DataSource> targetDataSources;
    private Object defaultTargetDataSource;
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantDatabaseContextHolder.getCurrentTenant();
    }
    
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        // Safely cast the incoming Map to your specific type
        this.targetDataSources = (Map<Object, DataSource>) (Object) targetDataSources;
        super.setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = (DataSource) defaultTargetDataSource;
        super.setDefaultTargetDataSource(this.defaultTargetDataSource);
    }
    public Map<Object, DataSource> getResolvedTargetDataSources() {
        return this.targetDataSources;
    }
}
