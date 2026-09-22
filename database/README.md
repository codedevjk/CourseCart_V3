# CourseCart Database Initialization

This directory contains the database initialization scripts for the CourseCart project. 

## Database Configuration
- **Database Name:** `coursecart_db`
- **Engine:** MySQL 8.x with InnoDB
- **Architecture:** Shared physical database across 4 microservices with strict logical write boundaries.

## Logical Table Ownership
To ensure the microservices remain strictly decoupled in code, writes to specific tables MUST only happen from the owning service.

1. **User Service:** Owns `users`
2. **Catalog Service:** Owns `categories`, `courses`, `lessons`
3. **Commerce Service:** Owns `orders`
4. **Enrollment Service:** Owns `enrollments`, `lesson_progress`

## Initialization Instructions
This project does not use automated migration tools like Flyway or Liquibase per the simplified architectural constraints. 

To initialize the database locally, execute the schema file using the MySQL CLI:

```bash
mysql -u root -p < schema.sql
```

Ensure your MySQL service is running locally on port `3306` with the appropriate root credentials before starting the Spring Boot applications.
