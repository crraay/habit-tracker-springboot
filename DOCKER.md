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

## GitHub Actions secrets and variables

Configure in the backend repo: **Settings → Secrets and variables → Actions**.

### Secrets

| Secret | Purpose |
|--------|--------|
| `SERVER_HOST` | Droplet hostname or IP |
| `SERVER_USER` | SSH user (must be able to run `docker compose` — typically in the `docker` group) |
| `SERVER_PASSWORD` | SSH password for that user |

### Variables (optional)

Use **Variables** (not secrets) when your paths on the server differ from the defaults.

| Variable | Default | Purpose |
|----------|---------|--------|
| `DEPLOY_BACKEND_DIR` | `/app/habit-tracker-springboot` | Absolute path to the **backend** git clone (must contain `docker-compose.yml`) |
| `DEPLOY_FRONTEND_DIR` | `/app/habit-tracker-angular` | Absolute path to the **frontend** git clone (sibling of backend per `docker-compose.yml`) |
| `SERVER_SSH_PORT` | `22` | SSH port |

If deploy fails with **`No such file or directory`** on `cd`, the clone is not at the default path: either create those directories (see below) or set `DEPLOY_BACKEND_DIR` / `DEPLOY_FRONTEND_DIR` to match where you actually cloned the repos (e.g. `/home/deploy/habit-tracker-springboot`).

**Note:** [appleboy/ssh-action](https://github.com/appleboy/ssh-action) v1.2.x does not support a `script_stop` input; the workflow uses `set -euo pipefail` in the remote script instead.

Optional hardening: strict host keys — see the ssh-action README (`fingerprint`, `key`, etc.).

The workflow runs `mvn -B -DskipTests package` before SSH so a broken backend build never triggers a deploy.

## Compose CLI vs `docker-compose.yml`

[`docker-compose.yml`](docker-compose.yml) only lives in the **git repo**. The machine still needs a **Compose implementation** on the PATH:

- **Recommended:** Docker Compose **v2 plugin** → command is `docker compose` (after `sudo apt-get install -y docker-compose-plugin`).
- **Legacy:** standalone **v1** → command is `docker-compose` (`sudo apt-get install -y docker-compose`).

The [deploy workflow](.github/workflows/deploy.yml) tries **`docker compose` first**, then **`docker-compose`**, so either works once installed.

## Initial server setup (DigitalOcean droplet)

1. **Install Docker Engine** and **Compose** (pick one):
   - **Plugin (preferred):** `sudo apt-get install -y docker-compose-plugin` then verify `docker compose version`.
   - **Or legacy:** `sudo apt-get install -y docker-compose` then verify `docker-compose version`.
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

   Use `docker-compose` instead of `docker compose` if you only installed the legacy v1 binary.

## Data safety (do not destroy the database volume)

- Postgres data lives in the **named volume** `postgres_data` (`docker volume ls`).
- **Never** on production: `docker compose down -v`, `docker volume rm postgres_data`, or `docker system prune --volumes`.
- Changing `POSTGRES_PASSWORD` in `.env` after the cluster was initialized **does not** change the password inside an existing volume; you must align credentials or recreate the volume (losing data) after backup.

## Troubleshooting

- **Backend fails healthcheck / DB connection:** ensure `postgres` is healthy first; check `SPRING_DATASOURCE_*` matches the running Postgres instance and database name.
- **Frontend 502** on `/api/`: confirm `backend` container is healthy and nginx proxies to `http://backend:8080` (see frontend `nginx.conf`).
- **Compose can’t find frontend build context:** confirm `habit-tracker-angular` exists next to this repo with the expected folder name.
