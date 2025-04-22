package com.midziklabs.authentication.service;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.midziklabs.authentication.utils.TenantDataSourceRouter;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantDataSourceService {
    private final Map<Object, Object> dataSourceMap = new HashMap<>();
    private final ApplicationContext applicationContext;


    @PostConstruct
    public void loadTenantsFromConfigServer(){
        // Load tenant data sources from the config server then call the createDataSource method
    }

    private DataSource createDataSource(String jdbcUrl, String username, String password){
        return DataSourceBuilder.create()
            .url(jdbcUrl)
            .username(username)
            .password(password)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();
    }

    public DataSource getDataSource(String tenantId) {
        return (DataSource) dataSourceMap.get(tenantId);
    }

    public void addDataSource(String tenantId, String jdbcUrl, String username, String password) {
        DataSource dataSource = createDataSource(jdbcUrl, username, password);
        dataSourceMap.put(tenantId, dataSource);
        updateRoutingDataSource();
    }

    public void removeDataSource(String tenantId) {
        dataSourceMap.remove(tenantId);
        updateRoutingDataSource();
    }

    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    private void updateRoutingDataSource() {
        TenantDataSourceRouter dataSourceRouter = (TenantDataSourceRouter) applicationContext.getBean("dataSource");
        // The targetDataSources map in TenantDataSourceService is already Map<String, DataSource>
        // AbstractRoutingDataSource expects Map<Object, Object>, so we can pass it directly.
        dataSourceRouter.setTargetDataSources((Map<Object, Object>) (Object) dataSourceMap);
    }
}