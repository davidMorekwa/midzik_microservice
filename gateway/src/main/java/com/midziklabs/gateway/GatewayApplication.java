package com.midziklabs.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import com.midziklabs.gateway.util.JwtValidationGatewayFilterFactory;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtValidationGatewayFilterFactory jwtValidationFilter) {
		return builder.routes()
				.route("authentication",r -> r
					.path("/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/user", "/api/v1/auth/admin")
					.filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationGatewayFilterFactory.Config())))
					.uri("lb://authentication")
				)
				.route("advertisement",r -> r
					.path("/api/v1/advertisement/**")
					.filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationGatewayFilterFactory.Config())))
					.uri("lb://advertisement")
				)
				.route("category", r -> r
					.path("/api/v1/category/**")
					.filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationGatewayFilterFactory.Config())))
					.uri("lb://advertisement")
				)
				.route("location", r -> r
					.path("/api/v1/location/**")
					.filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationGatewayFilterFactory.Config())))
					.uri("lb://advertisement")
				)
				.build();
	}

}
