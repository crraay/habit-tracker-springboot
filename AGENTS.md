# Agent instructions (Habit Tracker Spring Boot)

This file gives coding agents and contributors quick, project-specific context. For user-facing setup, see [README.md](README.md).

## Domain contract

Canonical product/domain law: [docs/domain.md](docs/domain.md).

Read it before changing habits, check-ins, streaks, pause/archive/delete, timezone, or the meaning of *today*. Use names from the Glossary in that file; do not invent a third synonym (`status` is not habit lifecycle and not “day done”). If implementation and that file disagree, stop and resolve the contract first — do not silently invent a new rule. Backend is the system of record; the Angular UI must follow the same law.

## What this repository is

- **Backend** for a Habit Tracker app: REST API built with **Spring Boot** and **Java 17**.
- **Client**: sibling repo [habit-tracker-angular](https://github.com/crraay/habit-tracker-angular) (`../habit-tracker-angular`). This repo is API-only.
- **Deploy**: [habit-tracker-compose](https://github.com/crraay/habit-tracker-compose) is the production path. See [DOCKER.md](DOCKER.md). Elastic Beanstalk (`.github/workflows/deploy-to-elastic-beanstalk.yml`) is **legacy** — do not extend or use it for new deploys.
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

- **Database**: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (defaults point at local PostgreSQL `habit_tracker`).
- **JWT**: `JWT_SECRET` (required; no default).
- **CORS**: `CORS_ALLOWED_ORIGINS` (required; no default) and related `cors.*` properties.
- **Server**: `SERVER_PORT` (default `8080`).
- **Flyway**: `FLYWAY_*` flags as in `application.properties`.

Test profile: `src/test/resources/application-test.properties` (H2, etc.).

## What agents should prioritize

- **Match existing style**: Lombok usage, package structure, exception types, and REST patterns already in controllers.
- **Database changes**: Prefer Flyway migrations; keep `validate` and migration order consistent with existing data.
- **API compatibility**: If changing DTOs or status codes, consider consumers (Angular) and document breaking changes in PRs.
- **Security**: Do not log tokens or passwords; be careful with default credentials in examples.

## Out of scope for this repo

- Frontend (Angular) code and UI assets.
- Elastic Beanstalk (legacy). Do not extend `.github/workflows/deploy-to-elastic-beanstalk.yml`.
- Unless explicitly requested, do not add unrelated refactors or large new docs beyond what the task needs. The exception is [docs/domain.md](docs/domain.md): keep it in sync when domain rules change.
