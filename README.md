This is a dummy dropwizard server

to compile:
 run `mvn clean install`
 
to deploy:
 run `java -jar target/dropwizard.jar server src/main/resources/config.yml`
 
Dummy first API:
`curl -XGET http://localhost:5000/dropwizard/api`

Metrics Endpoint @ `http://localhost:5001/admin/`
 