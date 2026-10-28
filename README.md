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

1. Create the database:
   ```sql
   CREATE DATABASE doctor_appointment_db;
   ```
2. Open `src/main/resources/application.properties` and set `spring.datasource.username` and
   `spring.datasource.password` to your PostgreSQL credentials.
3. Start the application from the project folder:
   ```bash
   ./mvnw spring-boot:run
   ```
   (on Windows cmd: `mvnw.cmd spring-boot:run`)
4. The API runs at `http://localhost:8081`. Tables are created automatically
   (`spring.jpa.hibernate.ddl-auto=update`).
5. Import `doctor-appointment-management-system-midterm.postman_collection.json` into Postman to test
   every requirement.
