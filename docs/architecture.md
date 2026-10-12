# Architecture (one-page)

Distilled from the Habit Tracker codebase scan. **Domain law** remains in [domain.md](./domain.md) — read that before changing habits, *today*, or streaks.

## Role in the system

| Repo | Responsibility |
|------|----------------|
| **This repo (U1)** | REST API, JWT auth, persistence, streak scheduler — **system of record** |
| [habit-tracker-angular](../habit-tracker-angular) | SPA consumer (OpenAPI-generated client) |
| [habit-tracker-compose](../habit-tracker-compose) | Production Compose + deploy (pulls Hub images) |

Production: images from **`build-image`** on `main` → Docker Hub → compose droplet. Elastic Beanstalk workflow here is **legacy** — do not extend.

## Style

**Layered monolith** — one Maven module, one JAR/Docker image. Synchronous HTTP only; no message bus or read replicas.

```
Client → Spring Security + JWT filter → Controller → Service → Repository → PostgreSQL
                                              ↑
                                    StreakResetScheduler (cron)
```

| Layer | Package / area |
|-------|----------------|
| HTTP | `controller/` |
| Rules | `service/`, `service/impl/` |
| Persistence | `repository/`, `entity/` |
| API shapes | `dto/`, `mapper/` |
| Cross-cutting | `config/`, `filter/`, `exception/`, `GlobalExceptionHandler` |

## Stack (pinned in `pom.xml`)

Java **17**, Spring Boot **3.5.x**, Spring Data JPA, Spring Security, **Flyway** (`ddl-auto=validate`), **PostgreSQL** (H2 in tests), **JJWT**, **springdoc-openapi**.

## Security & API surface

- Stateless **JWT** on `/api/**`; login via `/api/auth/login`.
- Errors: typed exceptions → **`ErrorResponse`** (status, message, path, **correlationId**, timestamp).
- OpenAPI/Swagger in dev; export snapshot for Angular **`npm run generate-api`**.

## Data & scheduling

- Schema: Flyway SQL under `src/main/resources/db/migration/`.
- **User clock**: timezone on user; `/api/me` exposes civil **today** (ISO date).
- **Streak reset**: `@Scheduled` job uses **`app.scheduling.timezone`** (global cron “today”) — may diverge from per-user clock; treat as known design tension vs [domain.md](./domain.md).

## Key integrations

| Consumer | Contract |
|----------|----------|
| Angular | REST + JWT; DTOs from checked-in OpenAPI |
| Compose | `SERVER_PORT` 8080, JDBC to `postgres`, env: `JWT_SECRET`, `CORS_*`, datasource |

## Health & ops

- **`GET /actuator/health`** — used by container HEALTHCHECK in production image.
- Logs: Logback to stdout; do not log JWT or passwords.

## Where to go deeper

- Layout and commands: [AGENTS.md](../AGENTS.md), [README.md](../README.md).
- Full scan notes: AI-DLC CodeKB `habit-tracker-springboot` (workspace `habit-tracker-ai-dlc/aidlc/.../codekb/`).
