# Habit Tracker Backend

REST API for the Habit Tracker app (Spring Boot, Java 17).

v1 product/domain rules live in [docs/domain.md](docs/domain.md). That file is the source of truth; the API must enforce it.

## Related repositories

| Repo | Role |
|------|------|
| [habit-tracker-angular](https://github.com/crraay/habit-tracker-angular) | Frontend |
| [habit-tracker-compose](https://github.com/crraay/habit-tracker-compose) | Production Docker Compose stack |

Sibling clones: `../habit-tracker-angular`, `../habit-tracker-compose`.

## Prerequisites

- Java 17
- PostgreSQL (local default: `localhost:5432/habit_tracker`)
- Maven Wrapper in this repo (`./mvnw`, on Windows `mvnw.cmd`) — a global Maven install is not required

## Configuration

Runtime settings: `src/main/resources/application.properties`. Do not commit real secrets.

**Required** for a normal run (no defaults in properties):

- `JWT_SECRET`
- `CORS_ALLOWED_ORIGINS`

**Optional** (local defaults in parentheses):

- `SPRING_DATASOURCE_URL` (`jdbc:postgresql://localhost:5432/habit_tracker?currentSchema=public`)
- `SPRING_DATASOURCE_USERNAME` (`postgres`)
- `SPRING_DATASOURCE_PASSWORD` (`postgres`)
- `SERVER_PORT` (`8080`)

## Development

```bash
git clone https://github.com/crraay/habit-tracker-springboot.git
cd habit-tracker-springboot
./mvnw spring-boot:run
```

API: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Test and package

```bash
./mvnw test
./mvnw -f pom.xml package
```

## Docker / production

Production Compose, `.env`, and deploy live in [habit-tracker-compose](https://github.com/crraay/habit-tracker-compose). This repo builds and pushes the backend image — see [DOCKER.md](DOCKER.md).

The Elastic Beanstalk workflow (`.github/workflows/deploy-to-elastic-beanstalk.yml`) is **legacy**. Do not use it for new deploys.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
