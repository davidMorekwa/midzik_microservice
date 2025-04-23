package com.midziklabs.authentication.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantDataSourceService {

    private final RestTemplate restTemplate = new RestTemplate();
    private String configServerUri = "http://localhost:8888";

    private final Map<String, DataSource> dataSourceMap = new HashMap<>();


    @PostConstruct
    public void init(){
        // Fetch tenant IDs from the Config Server
        String[] tenantIds = {"tenant-a", "tenant-b"}; // Replace with actual tenant IDs

        for (String tenantId : tenantIds) {
            fetchAndCreateDataSource(tenantId);
        }
    }
    private DataSource createDataSource(String jdbcUrl, String username, String password, String driverClassName) {
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                
                .password(password)
                .driverClassName(driverClassName)
                .type(HikariDataSource.class)
                .build();
    }
    public Map<String, DataSource> getDataSource() {
        return dataSourceMap;   
    }
    private void fetchAndCreateDataSource(String tenantId) {
        String configUrl = String.format("%s/%s/default", configServerUri, tenantId); // Construct tenant-specific endpoint

        try {
            // Make a direct call to the Config Server endpoint for the tenant
            // You might need to handle authentication if your Config Server is secured
            Map<String, Object> response = restTemplate.getForObject(configUrl, Map.class);
            if (response != null && response.containsKey("propertySources")) {
                List<Map<String, Object>> propertySources = (List<Map<String, Object>>) response.get("propertySources");
                log.info("Config Response: " + propertySources);
                for (Map<String, Object> propertySource : propertySources) {
                    if (propertySource.containsKey("source")) {
                        Map<String, String> source = (Map<String, String>) propertySource.get("source");
                        log.info(configUrl + " " + source);
                        String url = source.get("spring.datasource.url");
                        String username = source.get("spring.datasource.username");
                        String password = source.get("spring.datasource.password");
                        String driverClassName = source.get("spring.datasource.driver-class-name");

                        if (url != null && username != null && password != null && driverClassName != null) {
                            DataSource ds = createDataSource(url, username, password, driverClassName);
                            dataSourceMap.put(tenantId, ds);
                        }
                    }
                }
            } else {
                log.warn("Could not find database configuration for tenant: " + tenantId + " at " + configUrl);
            }
            // Handle the case where configuration is not found
        } catch (Exception e) {
            log.error("Error fetching configuration for tenant " + tenantId + " from " + configUrl, e);
            // Handle the exception
        }
    }
}
