# Doctor Appointment Management System — Backend

- **Student:** Shema Christian
- **Student ID:** 26991
- **Course:** INSY 8322 Web Technology and Internet (AUCA) — Mid-term project
- **Project:** Doctor Appointment Management System

Spring Boot + Spring Data JPA + PostgreSQL REST API.

## Requirements

- Java 21
- PostgreSQL (tested with version 18)

## Steps to run the API

1. Create the database user and database (as the `postgres` superuser):
   ```sql
   CREATE ROLE doctor_appointment LOGIN PASSWORD 'your-password';
   CREATE DATABASE doctor_appointment OWNER doctor_appointment;
   ```
2. Open `src/main/resources/application.properties` and make `spring.datasource.username` and
   `spring.datasource.password` match the user created above.
3. Start the application from the project folder:
   ```bash
   ./mvnw spring-boot:run
   ```
   (on Windows cmd: `mvnw.cmd spring-boot:run`)
4. The API runs at `http://localhost:8080`. Tables are created automatically
   (`spring.jpa.hibernate.ddl-auto=update`).
5. Import `doctor-appointment-management-system-midterm.postman_collection.json` into Postman to test
   every requirement.
