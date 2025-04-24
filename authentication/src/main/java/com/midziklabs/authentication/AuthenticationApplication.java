package com.midziklabs.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.midziklabs.authentication.model")
public class AuthenticationApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

}
