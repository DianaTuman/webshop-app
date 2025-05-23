# webshop-app

Webshop-like application created using Spring Boot. Homework for the Java developer training course.
The application will start at **localhost:8080**.

### Versions of software used in the development:

* Java JDK 21
* PostgreSQL 17
* Spring Boot 3.4.5
* Docker 4.40.0

### **To run this application without Docker:**

* **PostgreSQL** must be installed and running at **localhost:5432**.
  Environment variables **'DB_USER'** and **'DB_PASS'** must be set for accessing database and a schema named **practicum** must exist in database.
* **gradle bootRun** command to run application

### **To run this application with Docker:**
**Docker** must be installed and running
1. **gradle clean bootJar** command to create a jar
2. **docker-compose up --build** command to run the jar inside the docker container
