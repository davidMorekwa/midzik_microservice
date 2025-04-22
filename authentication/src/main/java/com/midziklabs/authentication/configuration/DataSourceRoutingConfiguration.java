package com.midziklabs.authentication.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.midziklabs.authentication.service.TenantDataSourceService;
import com.midziklabs.authentication.utils.TenantDataSourceRouter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataSourceRoutingConfiguration {
    private final TenantDataSourceService tenantDataSourceService;
    private final DataSource defaultDataSource;
    @Bean
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create()
            .url("dbc:mysql://mysql:3306/midzik_authentication_microservice?allowPublicKeyRetrieval=true&useSSL=false")
            .username("midzik_admin")
            .password("password")
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();
    }

    @Bean
    public DataSource dataSource(TenantDataSourceService tenantDataSourceService, DataSource defaultDataSource) {
       TenantDataSourceRouter dataSourceRouter = new TenantDataSourceRouter();
        dataSourceRouter.setTargetDataSources(tenantDataSourceService.getDataSourceMap());
        dataSourceRouter.setDefaultTargetDataSource(defaultDataSource);
        return dataSourceRouter;
    }
}
