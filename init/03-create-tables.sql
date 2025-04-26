-- This script creates the tables for the authentication microservice.
USE midzik_authentication_microservice;
CREATE TABLE IF NOT EXISTS t_roles(id INT(8) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL);
CREATE TABLE IF NOT EXISTS t_users(id INT(8) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role_id INT(8) NOT NULL, email VARCHAR(255) NOT NULL, FOREIGN KEY(role_id) REFERENCES t_roles(id));
INSERT INTO t_roles(name) VALUES("Administrator"),("Reviewer"),("User");
-- This script creates the tables for the advertisement microservice.
USE midzik_advertisement_microservice;
CREATE TABLE IF NOT EXISTS t_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL 
);
CREATE TABLE IF NOT EXISTS t_locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL, 
    address VARCHAR(512),
    county VARCHAR(255),
    price DOUBLE
);
CREATE TABLE IF NOT EXISTS t_advertisements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255), 
    description TEXT, 
    user_id INT, 
    reviewer_id INT, 
    status VARCHAR(50), 
    category_id BIGINT NULL, 
    file_path VARCHAR(512),
    loops INT,
    CONSTRAINT fk_advertisement_category
        FOREIGN KEY (category_id)
        REFERENCES t_categories(id)
        ON DELETE SET NULL 
        ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS advertisement_location (
    advertisement_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    PRIMARY KEY (advertisement_id, location_id), 

    CONSTRAINT fk_adloc_advertisement
        FOREIGN KEY (advertisement_id)
        REFERENCES t_advertisements(id)
        ON DELETE CASCADE  
        ON UPDATE CASCADE, 

    CONSTRAINT fk_adloc_location
        FOREIGN KEY (location_id)
        REFERENCES t_locations(id)
        ON DELETE CASCADE  
        ON UPDATE CASCADE 
);
CREATE TABLE IF NOT EXISTS t_users_replica (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Length might need adjustment depending on hashing algorithm
    role_id BIGINT NOT NULL         -- Likely a foreign key to a roles table, but no constraint defined in the Java entity itself.
);