# Docker Compose (local + DigitalOcean)

## Layout

This repo’s [`docker-compose.yml`](docker-compose.yml) expects:

- **Backend**: pre-built image from Docker Hub (`docker.io/crraay/habit-tracker-springboot:latest`), pushed by the **`build-image`** workflow.
- **Frontend**: sibling directory `../habit-tracker-angular` (clone the frontend repo next to this one on your machine or server); still built from source via Compose until an Angular Hub pipeline exists.

Example:

```text
/some/parent/
  habit-tracker-springboot/    # this repo; contains docker-compose.yml and .env
  habit-tracker-angular/       # frontend repo (sibling)
```

**Monorepo:** If both apps live under one root, adjust `frontend.build.context` and server paths accordingly.

## Local quick start

1. Copy env template: copy `.env.example` to `.env` in this directory and set real values (never commit `.env`).
2. Pull or build the backend image:
   - **Pull from Hub:** `docker pull docker.io/crraay/habit-tracker-springboot:latest`
   - **Or build locally:** `docker build -t docker.io/crraay/habit-tracker-springboot:latest .`
3. From this directory: `docker compose up -d --build` (frontend still builds; backend uses the image above).
4. App: frontend on port **80**, API via `/api/` through nginx to the backend.

**Local backend development without Hub:** use a gitignored `docker-compose.override.yml` with `backend.build.context: .` if you prefer Compose to build the backend locally instead of pulling.

## CI/CD (Docker Hub + DigitalOcean)

Two workflows in [`.github/workflows/`](.github/workflows/):

| Workflow | File | Trigger | What it does |
|----------|------|---------|--------------|
| **`build-image`** | [`build-image.yml`](.github/workflows/build-image.yml) | Push to `main`, manual | Maven gate → `docker build` → push **`docker.io/crraay/habit-tracker-springboot:latest`** |
| **`deploy-image-digital-ocean`** | [`deploy-image-digital-ocean.yml`](.github/workflows/deploy-image-digital-ocean.yml) | After **`build-image`** succeeds on `main`, manual | SSH to droplet → `git pull` → `docker compose pull backend` → `docker compose up -d backend` |

Push to **`main`** chains: **`build-image`** → **`deploy-image-digital-ocean`**. Re-run deploy only via **Actions → deploy-image-digital-ocean → Run workflow**.

Images are **public** on Docker Hub — the droplet does **not** need `docker login` to pull.

## GitHub Actions secrets and variables

Configure in the backend repo: **Settings → Secrets and variables → Actions**.

### Secrets

| Secret | Used by | Purpose |
|--------|---------|--------|
| `DOCKER_HUB_USERNAME` | `build-image` | Docker Hub login |
| `DOCKER_HUB_TOKEN` | `build-image` | Docker Hub access token (push) |
| `SERVER_HOST` | `deploy-image-digital-ocean` | Droplet hostname or IP |
| `SERVER_USER` | `deploy-image-digital-ocean` | SSH user (in `docker` group) |
| `SERVER_PASSWORD` | `deploy-image-digital-ocean` | SSH password |

### Variables (optional)

| Variable | Default | Purpose |
|----------|---------|--------|
| `DEPLOY_BACKEND_DIR` | `/app/habit-tracker-springboot` | Backend clone path on the droplet (must contain `docker-compose.yml` and `.env`) |
| `SERVER_SSH_PORT` | `22` | SSH port |
| `DEPLOY_SSH_COMMAND_TIMEOUT` | `15m` | Max time for remote deploy script (pull + up only; no image build on VPS) |

## Compose CLI vs `docker-compose.yml`

The machine still needs **Docker Compose v2** (`docker compose`) or legacy **`docker-compose`** on the PATH. Deploy workflows try **`docker compose` first**, then **`docker-compose`**.

## Initial server setup (DigitalOcean droplet)

1. **Install Docker Engine** and **Compose** (`sudo apt-get install -y docker-compose-plugin`; verify `docker compose version`).
2. **Deploy user:** create a user, add to group `docker`.
3. **Clone backend repo** (and frontend if you run the full stack with local frontend builds):

   ```bash
   sudo mkdir -p /app && sudo chown deploy:deploy /app
   git clone https://github.com/crraay/habit-tracker-springboot.git /app/habit-tracker-springboot
   git clone https://github.com/crraay/habit-tracker-angular.git /app/habit-tracker-angular
   ```

4. **Environment file:** copy `.env.example` to `/app/habit-tracker-springboot/.env` and set production values.
5. **First start (Postgres + volume, then apps):**

   ```bash
   cd /app/habit-tracker-springboot
   docker compose pull backend
   docker compose up -d postgres
   docker compose ps   # wait until postgres is healthy
   docker compose up -d backend frontend
   ```

   Frontend may still **build on the server** until you migrate it to Docker Hub. On a **1GB** droplet, prefer building the frontend image in CI or locally and pushing to Hub later.

6. **Later backend deploys:** automatic on push to `main`, or **Actions → deploy-image-digital-ocean → Run workflow**, or on the server:

   ```bash
   cd /app/habit-tracker-springboot
   git pull --ff-only
   docker compose pull backend
   docker compose up -d backend
   ```

## Data safety (do not destroy the database volume)

- Postgres data lives in the **named volume** `postgres_data` (`docker volume ls`).
- **Never** on production: `docker compose down -v`, `docker volume rm postgres_data`, or `docker system prune --volumes`.
- Backend deploy only **pulls** and **restarts** the `backend` service — Postgres is not rebuilt or removed.

## Troubleshooting

- **`ng build` / frontend: `Killed` or exit 137 on VPS:** OOM on small droplets. Add swap, build frontend in CI/Hub, or use a larger instance — see frontend repo when its Hub pipeline exists.
- **Backend fails healthcheck / DB connection:** ensure `postgres` is healthy; check `SPRING_DATASOURCE_*` in `.env`.
- **Frontend 502 on `/api/`:** confirm `backend` is healthy; nginx proxies to `http://backend:8080`.
- **Deploy: directory not found:** set `DEPLOY_BACKEND_DIR` or clone to `/app/habit-tracker-springboot`.
- **Pull fails for backend image:** confirm Hub repo `crraay/habit-tracker-springboot` is public and **`build-image`** has run at least once.
