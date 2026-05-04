# Docker Compose (local + DigitalOcean)

## Layout

This repo’s [`docker-compose.yml`](docker-compose.yml) expects:

- **Backend**: current directory (repo root containing this file and the backend `Dockerfile`).
- **Frontend**: sibling directory `../habit-tracker-angular` (clone the frontend repo next to this one on your machine or server).

Example:

```text
/some/parent/
  habit-tracker-springboot/    # this repo; contains docker-compose.yml and .env
  habit-tracker-angular/       # frontend repo (sibling)
```

**Monorepo:** If both apps live under one root (e.g. `repo/habit-tracker-springboot`, `repo/habit-tracker-angular`), change `build.context` in `docker-compose.yml` to `./habit-tracker-springboot` and `./habit-tracker-angular`, run Compose from that root, and adjust GitHub Actions / server paths to a single `git pull` under `/app`.

## Local quick start

1. Copy env template: copy `.env.example` to `.env` in this directory and set real values (never commit `.env`).
2. From this directory: `docker compose up -d --build`.
3. App: frontend on port **80**, API via `/api/` through nginx to the backend.

## GitHub Actions secrets

Configure in the backend repo: **Settings → Secrets and variables → Actions**.

| Secret | Purpose |
|--------|--------|
| `SERVER_HOST` | Droplet hostname or IP |
| `SERVER_USER` | SSH user (must be able to run `docker compose` — typically in the `docker` group) |
| `SERVER_PASSWORD` | SSH password for that user |

Optional:

- Non-default SSH port: edit [`.github/workflows/deploy.yml`](.github/workflows/deploy.yml) `port:` (default `22`).
- Strict host keys: see [appleboy/ssh-action](https://github.com/appleboy/ssh-action) options such as `key` / fingerprint-related inputs if you add a known-hosts secret.

The workflow runs `mvn -B -DskipTests package` before SSH so a broken backend build never triggers a deploy.

## Initial server setup (DigitalOcean droplet)

1. **Install Docker Engine** and the **Docker Compose v2** plugin (`docker compose version`).
2. **Deploy user:** create a non-root user (e.g. `deploy`), add to group `docker`, use that user for SSH and for git/docker on the server.
3. **Directories (two-repo layout, matches the default workflow):**
   - `sudo mkdir -p /app && sudo chown deploy:deploy /app`
   - `git clone <backend-url> /app/habit-tracker-springboot`
   - `git clone <frontend-url> /app/habit-tracker-angular`
4. **Git credentials on the server** so `git pull --ff-only` works (deploy key read-only to both repos, or cached HTTPS credentials). This is independent of GitHub Actions SSH secrets.
5. **Environment file:** copy `.env.example` to `/app/habit-tracker-springboot/.env` and set production values (`POSTGRES_*`, Base64 `JWT_SECRET`, `CORS_ALLOWED_ORIGINS` with your public origin).
6. **First start (Postgres + volume):**

   ```bash
   cd /app/habit-tracker-springboot
   docker compose up -d postgres
   docker compose ps   # wait until postgres is healthy
   docker compose up -d backend frontend
   ```

7. **Later deploys:** push to `main` runs CI + SSH deploy, or manually:

   ```bash
   cd /app/habit-tracker-springboot && git pull --ff-only
   cd /app/habit-tracker-angular && git pull --ff-only
   cd /app/habit-tracker-springboot
   docker compose build backend frontend
   docker compose up -d --no-deps backend frontend
   ```

   This rebuilds only **backend** and **frontend**; the **postgres** container and the **named volume** `postgres_data` stay in place.

## Data safety (do not destroy the database volume)

- Postgres data lives in the **named volume** `postgres_data` (`docker volume ls`).
- **Never** on production: `docker compose down -v`, `docker volume rm postgres_data`, or `docker system prune --volumes`.
- Changing `POSTGRES_PASSWORD` in `.env` after the cluster was initialized **does not** change the password inside an existing volume; you must align credentials or recreate the volume (losing data) after backup.

## Troubleshooting

- **Backend fails healthcheck / DB connection:** ensure `postgres` is healthy first; check `SPRING_DATASOURCE_*` matches the running Postgres instance and database name.
- **Frontend 502** on `/api/`: confirm `backend` container is healthy and nginx proxies to `http://backend:8080` (see frontend `nginx.conf`).
- **Compose can’t find frontend build context:** confirm `habit-tracker-angular` exists next to this repo with the expected folder name.
