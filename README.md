# University Student Management System (SMS)

REST API for managing students, teachers, courses, and enrollments. Built with Spring Boot 3, Spring Data JPA, PostgreSQL, and documented via Swagger UI.

## Tech Stack

- Java 21
- Spring Boot 3.3
- Spring Data JPA + Flyway (PostgreSQL)
- springdoc-openapi 2.5 (Swagger UI)
- JUnit 5 + Mockito + Spring Test

## Running Locally

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 15+

### 1. Create the database

```sql
CREATE USER sms_user WITH PASSWORD 'sms_password';
CREATE DATABASE sms_db OWNER sms_user;
```

### 2. Run the app

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

### 3. Open Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## Running Tests

```bash
mvn test
```

Tests use an H2 in-memory database — no PostgreSQL needed.

## API Overview

### Students `/api/v1/students`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create student |
| GET | `/` | List all (filter: `?status=ACTIVE`, `?year=2`) |
| GET | `/{id}` | Get by ID |
| PUT | `/{id}` | Update |
| DELETE | `/{id}` | Delete |
| GET | `/search?name=` | Search by name |
| GET | `/search?email=` | Search by email |
| GET | `/top?n=10` | Top-N students by GPA |

### Teachers `/api/v1/teachers`

Full CRUD (POST/GET/PUT/DELETE).

### Courses `/api/v1/courses`

Full CRUD + filter by `?teacherId=` and `?credits=`.

### Enrollments `/api/v1/enrollments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Enroll student in course |
| GET | `/{id}` | Get enrollment |
| GET | `/student/{studentId}` | Enrollments for student |
| GET | `/course/{courseId}` | Enrollments for course |
| PATCH | `/{id}/grade` | Set grade |
| PATCH | `/{id}/paid` | Mark as paid |
| GET | `/unpaid` | All unpaid |
| GET | `/unpaid/student/{id}` | Unpaid for student |
| GET | `/reports/gpa-by-course` | Average GPA per course |
| GET | `/reports/gpa-by-semester?from=&to=` | Average GPA in date range |

## Example cURL

```bash
# Create teacher
curl -X POST http://localhost:8080/api/v1/teachers \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Ivan","lastName":"Petrenko","email":"ivan@uni.ua","department":"Computer Science"}'

# Create student
curl -X POST http://localhost:8080/api/v1/students \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Olena","lastName":"Kovalenko","email":"olena@uni.ua","enrollmentDate":"2023-09-01","status":"ACTIVE","year":1}'

# Enroll student (use IDs from previous responses)
curl -X POST http://localhost:8080/api/v1/enrollments \
  -H 'Content-Type: application/json' \
  -d '{"studentId":1,"courseId":1}'

# Set grade
curl -X PATCH http://localhost:8080/api/v1/enrollments/1/grade \
  -H 'Content-Type: application/json' \
  -d '{"grade":92.5}'

# Top 5 students by GPA
curl http://localhost:8080/api/v1/students/top?n=5
```

## Project Structure

```
src/main/java/ua/university/sms/
├── controller/    REST endpoints
├── service/       Business logic (interfaces + implementations)
├── repository/    Spring Data JPA interfaces
├── model/
│   ├── entity/    JPA entities
│   └── dto/       Request/Response records
├── mapper/        Entity ↔ DTO conversion
└── exception/     GlobalExceptionHandler + custom exceptions
```
