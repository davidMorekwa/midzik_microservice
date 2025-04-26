-- Active: 1745398574921@@127.0.0.1@3307@midzik_advertisement_microservice

CREATE DATABASE IF NOT EXISTS midzik_advertisement_microservice;
CREATE DATABASE IF NOT EXISTS midzik_authentication_microservice;
CREATE DATABASE IF NOT EXISTS midzik_notification_microservice;
CREATE DATABASE IF NOT EXISTS midzik_payment_microservice;
CREATE DATABASE IF NOT EXISTS midzik_microservice;



USE midzik_microservice;
CREATE TABLE IF NOT EXISTS t_roles(id INT(8) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL);
CREATE TABLE IF NOT EXISTS t_users(id INT(8) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role_id INT(8) NOT NULL, email VARCHAR(255) NOT NULL, FOREIGN KEY(role_id) REFERENCES t_roles(id));
INSERT INTO t_roles(name) VALUES("Administrator"),("Reviewer"),("User");
