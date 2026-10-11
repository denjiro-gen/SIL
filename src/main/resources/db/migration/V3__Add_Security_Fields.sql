-- V3__Add_Security_Fields.sql
-- Hands-On Activity: Enhancing Spring Boot REST API with Security, RBAC & Global Exception Handling
-- Requirement 1: Database Schema Update (mydb_api)
-- - Add/ensure username (VARCHAR, Unique, Not Null) and password (VARCHAR, Not Null) columns to the students table.
-- - Configure role column for Role-Based Access Control (ROLE_ADMIN, ROLE_STUDENT).

ALTER TABLE students MODIFY COLUMN username VARCHAR(255) NOT NULL;
ALTER TABLE students MODIFY COLUMN password VARCHAR(255) NOT NULL;
ALTER TABLE students MODIFY COLUMN role VARCHAR(50) NOT NULL DEFAULT 'ROLE_STUDENT';
