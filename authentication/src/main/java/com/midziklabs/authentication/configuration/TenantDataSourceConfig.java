package com.midziklabs.authentication.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.midziklabs.authentication.service.TenantDataSourceService;
import com.midziklabs.authentication.utils.MultiTenantDataSource;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class TenantDataSourceConfig {

    @Bean
    public DataSource defaultDataSource(){
        return DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:3307/midzik_microservice")
            .username("root")
            .password("password")
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .type(HikariDataSource.class)
            .build();
    }
    @Primary
    @Bean 
    public DataSource tenantDataSource(TenantDataSourceService tenantDataSourceService) {
        MultiTenantDataSource multiTenantDataSource = new MultiTenantDataSource();
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        multiTenantDataSource.setTargetDataSources(new HashMap<>(tenantDataSourceService.getDataSource()));
        return multiTenantDataSource;
    }
}
