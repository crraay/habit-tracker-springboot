# Agent instructions (Habit Tracker Spring Boot)

This file gives coding agents and contributors quick, project-specific context. For user-facing setup, see [README.md](README.md).

## What this repository is

- **Backend** for a Habit Tracker app: REST API built with **Spring Boot** and **Java 17**.
- **Client**: an Angular frontend exists elsewhere; this repo is API-only.
- **Group / package root**: `com.kostya.habittracker` (see `src/main/java`).

## Tech stack (high level)

- **Build**: Maven (`pom.xml`), Java **17** (enforced in POM; CI uses the same).
- **Framework**: Spring Boot **3.5.x** (parent in `pom.xml`), Spring Web, Spring Data JPA, Spring Security.
- **Database**: **PostgreSQL** in normal runs; **H2** for tests (`scope=test` in POM).
- **Migrations**: **Flyway** (SQL under `src/main/resources/db/migration` when present); `spring.jpa.hibernate.ddl-auto=validate` in `application.properties`.
- **API docs**: **Springdoc OpenAPI** — Swagger UI when the app is up (path documented in [README.md](README.md)).
- **Auth**: JWT (JJWT) + Spring Security filters; see `config/`, `filter/`, `util/JwtUtil.java`.

## Project layout (where to change things)

| Area | Typical location |
|------|------------------|
| HTTP API | `controller/*.java` |
| Business logic | `service/`, `service/impl/` |
| Persistence | `repository/*.java`, `entity/*.java` |
| API shapes | `dto/*.java` |
| Mapping (entity ↔ DTO) | `mapper/*.java` |
| Security & cross-cutting | `config/`, `filter/`, `security/`, `aspect/` |
| Exceptions & handling | `exception/`, `GlobalExceptionHandler` |
| Scheduled jobs | `scheduled/`, `SchedulingConfig` |

Prefer extending existing patterns (service interfaces + impl, mappers, DTOs) over new parallel conventions.

## Commands (verify locally)

From the repository root:

```bash
./mvnw -f pom.xml clean test
./mvnw -f pom.xml package
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` if needed. CI compiles, tests, and packages via Maven (see `.github/workflows/build-and-test.yml`).

## Configuration and secrets

Runtime settings live in `src/main/resources/application.properties`. **Do not** commit real production secrets. Common environment-driven values:

- **Database**: `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` (defaults point at local PostgreSQL; adjust for your machine).
- **JWT**: `JWT_SECRET` (required for meaningful auth in a real run).
- **CORS**: `CORS_ALLOWED_ORIGINS` and related `cors.*` properties.
- **Server**: `SERVER_PORT` (default in properties aligns with common Elastic Beanstalk use).
- **Flyway**: `FLYWAY_*` flags as in `application.properties`.

Test profile: `src/test/resources/application-test.properties` (H2, etc.).

## What agents should prioritize

- **Match existing style**: Lombok usage, package structure, exception types, and REST patterns already in controllers.
- **Database changes**: Prefer Flyway migrations; keep `validate` and migration order consistent with existing data.
- **API compatibility**: If changing DTOs or status codes, consider consumers (Angular) and document breaking changes in PRs.
- **Security**: Do not log tokens or passwords; be careful with default credentials in examples.

## Out of scope for this repo

- Frontend (Angular) code and UI assets.
- Unless explicitly requested, do not add unrelated refactors or large new docs beyond what the task needs.
