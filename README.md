# webshop-app

Webshop-like application created using Spring Boot. Homework for the Java developer training course.
The application will start at **localhost:8080**. The payment REST-service will start at **localhost:8081**.

### Versions of software used in the development:

* Java JDK 21
* Spring Boot 3.4.5
* PostgreSQL 17
* Redis 7.4.2
* Docker 4.40.0

### **To run this application without Docker:**

* **PostgreSQL** must be installed and running at **localhost:5432**.
  Environment variables **'DB_USER'** and **'DB_PASS'** must be set for accessing database and a schema named *
  *practicum** must exist in database.
* **Redis** must be installed and running at **localhost:6379**.
* **gradle :main-app:bootRun** command to run main application
* **gradle :payment-app:bootRun** command to run payment REST-service

### **To run this application with Docker:**

**Docker** must be installed and running

1. **gradle clean buildAll** command to create a jars
2. **docker-compose up --build** command to run the jars inside the docker container
