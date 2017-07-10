DROP DATABASE dropwizard_dummy;

CREATE DATABASE dropwizard_dummy;

use dropwizard_dummy;

CREATE TABLE api_response(name VARCHAR(20), response VARCHAR(20), created_on DATE);

INSERT INTO api_response(`name`,`response`,`created_on`) VALUES(`api`,`{}`,now());