# Theory Progress API

A minimal Kotlin / Spring Boot backend for the Class B theory-progress interview exercise. It exposes one read-only endpoint backed by three mock students in memory.

## Requirements

- JDK 25 installed and available to the Gradle wrapper. Check with `java -version`.
- Internet access on the first build to download Gradle and dependencies.

The project uses Kotlin 2.3.21 and Spring Boot 4.1.1. Gradle 9.7.1 is provided through the committed wrapper; a separate Gradle installation is not required.

## Run

From the repository root:

```bash
./gradlew bootRun
```

The API listens at `http://localhost:8080`. Stop the application with `Ctrl+C`.

On Windows, use `gradlew.bat` in place of `./gradlew`.

If port 8080 is already in use, choose another port:

```bash
./gradlew bootRun --args='--server.port=8081'
```

For this command, use `http://localhost:8081` in the request examples below.

## API

```http
GET /api/students/{id}/theory-progress
```

The student ID is a string. No authentication or request body is required.

### Successful response

With the application running, execute this in another terminal:

```bash
curl -i -H 'Accept: application/json' \
  http://localhost:8080/api/students/1/theory-progress
```

The endpoint returns `200 OK` with `Content-Type: application/json`:

```json
{
  "studentId": "1",
  "licenseClass": "B",
  "basicTopics": { "attended": 8, "required": 12 },
  "specialTopics": { "attended": 1, "required": 2 },
  "completed": false
}
```

Completion requires both `basicTopics.attended >= 12` and `specialTopics.attended >= 2`. Reaching only one requirement leaves `completed` false. Extra attendance keeps progress complete once both requirements are reached; the response preserves the actual counts.

### Mock students

| Student ID | Basic topics | Special topics | Completed |
| --- | --- | --- | --- |
| `1` | 8 / 12 | 1 / 2 | `false` |
| `2` | 0 / 12 | 0 / 2 | `false` |
| `3` | 12 / 12 | 2 / 2 | `true` |

The mock data is initialized at application startup. There are no write endpoints.

### Unknown student

```bash
curl -i -H 'Accept: application/json' \
  http://localhost:8080/api/students/999/theory-progress
```

An unknown ID returns `404 Not Found` with `Content-Type: application/json`:

```json
{
  "message": "Student '999' not found"
}
```

An existing student with incomplete or zero attendance still returns `200 OK`.

## Tests

```bash
./gradlew test
```

The suite covers:

- Domain completion rules: partial attendance, either requirement missing, exact completion, and attendance beyond the requirements.
- Zero attendance defaults and rejection of negative attendance counts.
- API status codes and exact JSON contracts for all three mock students and an unknown student.
- Spring application-context startup.

Domain tests run without Spring. API tests use `@SpringBootTest` and MockMvc with the real service and in-memory repository; they exercise Spring MVC without opening a network port.

The HTML test report is generated at `build/reports/tests/test/index.html`.

To compile, test, and package the application from a clean build:

```bash
./gradlew clean build
```

## Architecture

The application uses a single-module layered architecture, organized around the `theoryprogress` feature:

```text
src/main/kotlin/com/theory/demo/
├── DemoApplication.kt
└── theoryprogress/
    ├── api/          # Controller, response DTOs, and exception handling
    ├── service/      # Student lookup coordination and missing-student exception
    ├── repository/   # In-memory mock data
    └── domain/       # Progress model and completion rule
```

Request flow:

```text
HTTP request → Controller → Service → Repository
                              ↓
                         Domain object
                              ↓
                  Response DTO → JSON → HTTP response
```

- **Controller:** binds the student ID from the URL, calls the service, and maps the domain object to the response DTO.
- **Service:** retrieves progress or raises `StudentNotFoundException`.
- **Repository:** stores a private map of mock students and performs ID lookups.
- **Domain:** holds read-only attendance counts, validates non-negative counts, and derives completion from the Class B requirements.
- **Response DTOs:** define the public JSON shape separately from the domain model.
- **Exception handler:** translates a missing student into the short JSON response with HTTP status 404.

Spring supplies dependencies through constructors and uses Jackson to serialize response objects. Completion is calculated rather than stored separately, preventing a completion flag from disagreeing with attendance counts.

The concrete in-memory repository and one Gradle module keep the implementation proportional to the exercise. There is currently one data source, so no repository interface is needed.

## Scope

This repository implements the backend ticket only. Database persistence, authentication, other license classes, and the Flutter frontend are outside its scope.
