DROP DATABASE target;

CREATE DATABASE target;

use target;


CREATE TABLE products(
	id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20) NOT NULL , 
	description VARCHAR(50),
	quantity INT, 
	price DECIMAL(10,2), 
	created_at TIMESTAMP DEFAULT now(),
	updated_at TIMESTAMP DEFAULT now() ON UPDATE now());


CREATE TABLE purchases(
	id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id VARCHAR(20) NOT NULL , 
	products VARCHAR(50),
	reverted boolean DEFAULT FALSE,
	amount DECIMAL(10,2), 
	created_at TIMESTAMP DEFAULT now(),
	updated_at TIMESTAMP DEFAULT now() ON UPDATE now());


CREATE TABLE system_cash(
	id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	system_cash DECIMAL(10,2), 
	created_at TIMESTAMP DEFAULT now(),
	updated_at TIMESTAMP DEFAULT now() ON UPDATE now());


