# 🚀 Spring Boot REST API — Security, RBAC & Global Exception Handling

A secure, production-grade Spring Boot 3 REST API featuring **JWT Authentication**, **Role-Based Access Control (RBAC)**, **BCrypt Password Hashing**, **Flyway Database Migrations**, and **Global Exception Handling**.

---

## 📋 Quick Start Guide (Step-by-Step)

Follow these simple steps to set up, run, and test the project on any computer.

### Step 1: Prerequisites
Ensure the following tools are installed:
- **Java JDK 17** (e.g., [Eclipse Adoptium Temurin 17](https://adoptium.net/))
- **XAMPP** (for MySQL database): [Download XAMPP](https://www.apachefriends.org/)
- **VS Code** with the **Extension Pack for Java** (or IntelliJ IDEA)
- **Postman**: [Download Postman](https://www.postman.com/downloads/)
- **Git**: [Download Git](https://git-scm.com/)

---

### Step 2: Clone the Project
Open PowerShell or Command Prompt:
```bash
git clone https://github.com/denjiro-gen/SIL.git
cd SIL
```

---

### Step 3: Start MySQL via XAMPP
1. Open the **XAMPP Control Panel**.
2. Click **Start** next to **MySQL** (and **Apache** if you use phpMyAdmin).
   > ⚠️ **IMPORTANT**: **Do NOT start Tomcat in XAMPP!** Spring Boot has its own built-in Tomcat server that runs on port `8080`. Starting Tomcat in XAMPP will cause a *"Port 8080 was already in use"* error.
3. Open your browser and go to: [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
4. Click **New** in the left sidebar.
5. Create a new database named exactly:
   ```
   mydb_api
   ```
   *(Collation: utf8mb4_general_ci or default)*

---

### Step 4: Configure Database Credentials (If Needed)
Open `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb_api?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```
- By default in XAMPP, `root` has **no password** (`spring.datasource.password=` is empty).
- If your MySQL has a password, enter it after `spring.datasource.password=`.

---

### Step 5: Run the Application

#### Option A: In VS Code (Recommended)
1. Open VS Code: `File` → `Open Folder...` → select the `SIL` folder.
2. In the file explorer, navigate to:
   `src/main/java/com/example/api/ApiApplication.java`
3. Click the **Run** or **Debug** button above `public static void main(String[] args)`.
4. Wait for the terminal to print:
   ```
   Started ApiApplication in X.XXX seconds
   ```
   🎉 **The server is now running on `http://localhost:8080`!**

#### Option B: Using Maven in Terminal
If you have Maven installed:
```bash
mvn spring-boot:run
```

---

### Step 6: Database Auto-Migration & Seeding
You do **not** need to manually create tables or insert SQL records!
When the app starts:
1. **Flyway** automatically runs migrations:
   - `V1__Initial_Schema.sql` (Creates `courses`, `subjects`, `students`, `student_subjects`)
   - `V2__Insert_Data.sql` (Seeds courses, subjects, and users)
   - `V3__Add_Security_Fields.sql` (Ensures security columns `username`, `password`, `role` exist)
2. **DatabaseSeeder** automatically hashes passwords with **BCrypt**:
   - Default **ADMIN**: `admin` / `admin123`
   - Default **STUDENT**: `alex_mercer` / `password123`

---

### Step 7: Test with Postman

We have included a complete Postman collection ready to test all 10 endpoints!

1. Open **Postman**.
2. Click **Import** (top left) and select the file:
   ```
   Postman_Collection.json
   ```
3. Run the requests in this order:
   1. **`POST Login (ADMIN)`** → Logs in as `admin`. Postman **automatically saves the JWT token** into the collection variable `{{adminToken}}`!
   2. **`POST Login (STUDENT)`** → Logs in as `alex_mercer`. Automatically saves `{{studentToken}}`!
   3. **`GET All Courses`** → Returns list of courses (Requires Admin).
   4. **`GET All Subjects`** → Returns list of subjects (Requires Admin).
   5. **`GET All Students`** → Returns list of students with enrolled courses & subjects (Requires Admin).
   6. **`GET Student Self by ID`** → Returns student `id=2` (Allowed for Student).
   7. **`PUT Enroll in Course`** → Assigns student to a course (Requires Admin).
   8. **`PUT Enroll in Subject`** → Adds a subject to a student (Requires Admin).
   9. **`POST Create Course / Subject / Student`** → Adds new records (Requires Admin).

See `POSTMAN_TEST_GUIDE.txt` for the full endpoint reference table, sample request bodies, and expected responses.

---

## 🔒 Role-Based Access Control (RBAC) Matrix

| Method | Endpoint | Required Role | Description |
|---|---|---|---|
| `POST` | `/api/auth/login` | **Public** (No Token) | Authenticates and returns JWT token |
| `GET` | `/api/courses` | `ROLE_ADMIN` | List all courses |
| `POST` | `/api/courses` | `ROLE_ADMIN` | Create a new course |
| `GET` | `/api/courses/{id}` | `ROLE_ADMIN` | Get course by ID |
| `GET` | `/api/subjects` | `ROLE_ADMIN` | List all subjects |
| `POST` | `/api/subjects` | `ROLE_ADMIN` | Create a new subject |
| `GET` | `/api/subjects/{id}` | `ROLE_ADMIN` | Get subject by ID |
| `GET` | `/api/students` | `ROLE_ADMIN` | List all students |
| `POST` | `/api/students` | `ROLE_ADMIN` | Register a new student |
| `GET` | `/api/students/{id}` | `ROLE_ADMIN` or Owner `ROLE_STUDENT` | View student details (Students can only view their own profile) |
| `PUT` | `/api/students/{id}/course/{courseId}` | `ROLE_ADMIN` | Enroll student in course |
| `PUT` | `/api/students/{id}/subject/{subjectId}` | `ROLE_ADMIN` | Enroll student in subject |

---

## 🛡️ Standardized Error Response Format

All error responses across the entire application follow a unified structure:

```json
{
  "timestamp": "2026-10-11T17:16:52.039160900",
  "status": 403,
  "error": "Forbidden",
  "message": "You do not have permission to access this resource.",
  "path": "/api/students"
}
```

- **`400 Bad Request`**: Validation errors, malformed JSON, or missing required fields.
- **`401 Unauthorized`**: Missing, invalid, or expired JWT token.
- **`403 Forbidden`**: Role insufficient (e.g. Student trying to view Admin resources, or student viewing another student's profile).
- **`404 Not Found`**: Entity (Student, Course, or Subject) does not exist in the database.
- **`409 Conflict`**: Duplicate username/email or database integrity constraint violation.

---

## 🛠️ Common Troubleshooting

### 1. `Port 8080 was already in use`
- **Cause:** Tomcat is running in the XAMPP Control Panel, or another app is using port 8080.
- **Fix:** In XAMPP Control Panel, click **Stop** next to Tomcat. Only **MySQL** (and Apache) should be running.

### 2. `401 Unauthorized` on all requests
- **Cause:** You did not include the JWT token or the token expired.
- **Fix:** Run `POST Login (ADMIN)` or `POST Login (STUDENT)` in Postman to refresh your token.

### 3. `Could not find or load main class com.example.api.ApiApplication`
- **Cause:** Stale workspace build cache in VS Code.
- **Fix:** In VS Code, press `Ctrl+Shift+P` → type `Java: Clean Language Server Workspace` → select `Restart and delete`. Then click **Run** on `ApiApplication.java`.

---

## 📂 Project Architecture

The codebase follows the standard Spring Boot layered architecture:

```
src/main/java/com/example/api/
├── ApiApplication.java          # Main Application Entry Point
├── config/
│   ├── ApplicationConfig.java   # PasswordEncoder, UserDetailsService, AuthenticationManager
│   └── DatabaseSeeder.java      # BCrypt password seeder on application startup
├── controller/                  # REST Controllers exposing HTTP endpoints
│   ├── AuthController.java
│   ├── CourseController.java
│   ├── StudentController.java
│   └── SubjectController.java
├── dto/                         # Request and Response transfer objects
│   ├── AuthRequest.java / AuthResponse.java
│   ├── CourseCreateDto.java / CourseResponseDto.java
│   ├── StudentCreateDto.java / StudentResponseDto.java
│   └── SubjectCreateDto.java / SubjectResponseDto.java
├── entity/                      # JPA Entities mapped to MySQL tables
│   ├── Course.java
│   ├── Student.java
│   └── Subject.java
├── exception/                   # Global exception handling & custom exceptions
│   ├── ErrorHandler.java
│   ├── ErrorResponse.java
│   └── NotFoundException.java
├── repository/                  # Spring Data JPA repositories
│   ├── CourseRepository.java
│   ├── StudentRepository.java
│   └── SubjectRepository.java
├── security/                    # Spring Security & JWT components
│   ├── JwtAccessDeniedHandler.java  # Custom 403 Forbidden handler
│   ├── JwtAuthEntryPoint.java       # Custom 401 Unauthorized handler
│   ├── JwtAuthFilter.java           # JWT extraction and validation filter
│   ├── JwtService.java              # Token generation and claims extraction
│   ├── SecurityConfig.java          # Security filter chain and endpoint RBAC rules
│   └── StudentSecurity.java         # ID ownership authorization evaluator
└── service/                     # Business logic layer
    ├── CourseService.java
    ├── StudentService.java
    └── SubjectService.java
```
