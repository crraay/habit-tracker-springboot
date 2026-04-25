# Agent instructions

This repository is the **Habit Tracker** Spring Boot backend. Persistent, scoped guidance for Cursor and other agents lives in **`.cursor/rules/*.mdc`** (ten topic rules plus an always-on project brief). Read those when editing matching paths; this file is the short operational map.

## Stack

- Java **17**, Spring Boot **3.5.x**, Maven.
- Spring Web, Data JPA, Security, Validation, Actuator, Flyway, PostgreSQL (runtime), **H2** (tests), Springdoc OpenAPI, JJWT, Lombok.
- Base package: `com.kostya.habittracker`.

## Commands

- Run: `mvn spring-boot:run`
- Test: `mvn test`
- Package: `mvn clean package` (CI also runs `clean compile`, `test`, then `package -DskipTests`)

## Configuration and secrets

- Committed defaults: `src/main/resources/application.properties`.
- **`src/main/resources/application-*.properties` is gitignored** — use env vars or local overrides for secrets (e.g. `JWT_SECRET`, `DATABASE_URL`, `CORS_ALLOWED_ORIGINS`). Never commit production credentials or suggest hardcoding them.

## Layout (where to change what)

| Area        | Location |
|------------|----------|
| REST API   | `controller/`, `dto/`, `mapper/` |
| Business   | `service/`, `service/impl/` |
| Persistence| `entity/`, `repository/` |
| Security   | `config/WebSecurityConfig.java`, `filter/JwtRequestFilter.java`, `security/` |
| Migrations | `src/main/resources/db/migration/` |
| API docs   | `config/OpenAPIConfig.java`, `@Tag` on controllers |
| CI         | `.github/workflows/` |

## Testing

- Integration smoke: `@SpringBootTest` + `@ActiveProfiles("test")` in `HabittrackerApplicationTests`.
- For secured endpoints, use `spring-security-test` patterns consistent with the codebase.

## When the server runs

- Swagger UI: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

## Legacy note

- **`.cursorrules`** in the repo root points agents at `.cursor/rules/` for detailed conventions.
