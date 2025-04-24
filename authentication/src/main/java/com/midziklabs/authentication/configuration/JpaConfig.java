package com.midziklabs.authentication.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class JpaConfig {
    // @Bean
    // public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource tenantDataSource, EntityManagerFactoryBuilder builder) {
    //     Map<String, Object> jpaProperties = new HashMap<>();
    //     jpaProperties.put("hibernate.hbm2ddl.auto", "create");
    //     jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    //     return builder
    //             .dataSource(tenantDataSource)
    //             .packages("com.midziklabs.authentication.model") // change this to your entity package
    //             .persistenceUnit("multiTenantPU")
    //             .properties(jpaProperties)
    //             .build();
    // }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource tenantDataSource) {

        Map<String, Object> jpaPropertiesMap = new HashMap<>(); // Renamed for clarity
        jpaPropertiesMap.put("hibernate.hbm2ddl.auto", "none");
        jpaPropertiesMap.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        Properties jpaProperties = new Properties();
        jpaProperties.putAll(jpaPropertiesMap);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.midziklabs.authentication.model");
        factory.setJpaProperties(jpaProperties);
        factory.setDataSource(tenantDataSource);
        factory.setPersistenceUnitName("multiTenantPU");
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
