# webshop-app
Webshop-like application created using Spring Boot. Homework for the Java developer training course.

### **To run this application:**
* **PostgreSQL** must be installed and running at **localhost:5432**.
  Environment variables **'DB_USER'** and **'DB_PASS'** must be set for accessing database and a schema named **practicum** must exist in database.

### **Commands to run this application:**
* **gradle bootJar** to create a jar, that can be later used with command **java -jar myapp.jar**
* **gradle bootRun** to run application
* **gradle test** to run tests

### Versions of software used in the development:
* Java JDK 21
* PostgreSQL 17
* Spring Boot 3.4.5
* Docker 4.40.0