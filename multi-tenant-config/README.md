# Multi-Tenant Remote Configuration

This repository contains remote configuration files for multi-tenant Spring Boot applications. It is designed to work with Spring Cloud Config Server so that clients (or other microservices) can fetch configuration properties centrally from GitHub.

## Repository Structure

This repository links with https://github.com/MidZikLabs/multi-tenant which contains two YAML configuration files:

- **demo.yml**  
  Contains configuration for an application with `spring.application.name: demo`.

- **demo2.yml**  
  Contains configuration for an application with `spring.application.name: demo2`.

the two can be accessed on http://localhost:8888/demo/default & http://localhost:8888/demo2/default

