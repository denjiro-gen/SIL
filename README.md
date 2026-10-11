# Spring Boot Security API 🚀

## ⚙️ How to Clone and Run on Another Laptop

### Requirements (Install these first)
| Tool | Download Link |
|---|---|
| Java JDK 17 | https://adoptium.net |
| Maven | https://maven.apache.org/download.cgi |
| XAMPP (MySQL) | https://www.apachefriends.org |
| Git | https://git-scm.com |

---

### Step 1: Clone the Repository
Open a terminal (Command Prompt or PowerShell) and run:
```bash
git clone https://github.com/denjiro-gen/SIL.git
cd SIL
```

### Step 2: Start MySQL via XAMPP
1. Open **XAMPP Control Panel**
2. Click **Start** next to **MySQL**
3. Open **phpMyAdmin** at `http://localhost/phpmyadmin`
4. Click **New** and create a database named exactly: `mydb_api`

### Step 3: Run the Application
In the terminal (inside the SIL folder), run:
```bash
mvn spring-boot:run
```
Wait until you see this line in the console:
```
Tomcat initialized with port(s): 8080 (http)
```
The app will automatically create all the database tables and insert the test users for you!

### Step 4: Test with Postman
Use these default login credentials:

| Role | Username | Password |
|---|---|---|
| Admin | admin | admin123 |
| Student | alex_mercer | password123 |

Send a `POST` request to `http://localhost:8080/api/auth/login` with the credentials above to get your JWT token. See `POSTMAN_TEST_GUIDE.txt` for the full testing steps.

---


This is a backend REST API built with Spring Boot, MySQL, and Spring Security (JWT). It features Role-Based Access Control (RBAC) and clean global exception handling.

## 📂 Project Architecture (Java Folders Explained)

The source code inside `src/main/java/com/example/api/` is split into several packages (folders) following the standard **Spring Boot Layered Architecture**. Here is what every folder does:

### 1. `config` (Configuration)
Contains settings that run when the application starts. 
- Example: `DatabaseSeeder.java` sits here to automatically hash and insert the default Admin and Student passwords whenever you start the server.

### 2. `controller` (The API Endpoints)
The entry point for the frontend or Postman. Controllers listen for HTTP requests (GET, POST, PUT, DELETE) at specific URLs (like `/api/students`). 
- They don't do the heavy lifting; they just receive the request, hand it to the `service` layer, and then return the response back to the user.

### 3. `dto` (Data Transfer Objects)
DTOs are simple templates used to format the data going **in** and **out** of your API.
- For example, when creating a student, we don't need them to send an ID because the database generates it. We use a `StudentCreateDto` to specify that they only need to send a username, email, password, and name. It hides sensitive information (like passwords) from being sent back in responses.

### 4. `entity` (Database Tables)
These classes represent the actual tables in your MySQL Database. 
- A class called `Student` marked with `@Entity` tells Spring Boot to map it directly to the `students` table. The variables inside the class (like `id`, `name`) become the columns in that table.

### 5. `repository` (Database Access)
This layer talks directly to the MySQL database.
- It contains Java Interfaces that extend `JpaRepository`. Without writing any SQL queries yourself, Spring Boot automatically gives these files the ability to save, delete, find, and update entities in the database.

### 6. `service` (Business Logic)
This is where the actual "thinking" happens. 
- Controllers pass data here. The Service layer runs checks (e.g., "Does this email already exist?"), handles logic, and tells the Repository what to save. This keeps the Controllers clean and focused only on web traffic.

### 7. `security` (Authentication & Authorization)
Handles all the JWT (JSON Web Token) generation and validation.
- Contains the filters that check every single incoming request to see if the user provided a valid token in the Authorization header. It also defines which roles (`ADMIN` or `STUDENT`) are allowed to access which controllers.

### 8. `exception` (Error Handling)
Catches crashes and errors before they reach the user.
- Instead of showing ugly Java stack traces when something goes wrong (like a user not being found), the `ErrorHandler` intercepts the crash and formats a clean, readable JSON error message (like a `404 Not Found` or `400 Bad Request`).
