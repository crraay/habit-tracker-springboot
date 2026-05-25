# Docker Compose (local + DigitalOcean)

## Layout

This repo’s [`docker-compose.yml`](docker-compose.yml) orchestrates the full stack on the droplet:

- **Backend:** `docker.io/crraay/habit-tracker-springboot:latest` — built by **`build-image`** in this repo.
- **Frontend:** `docker.io/crraay/habit-tracker-angular:latest` — built by **`build-image`** in [habit-tracker-angular](https://github.com/crraay/habit-tracker-angular).

The server only needs **this repo clone** (compose + `.env`). No frontend source clone is required for deploy.

## Local quick start

1. Copy `.env.example` to `.env` and set real values.
2. Pull or build both images:

   ```bash
   docker pull docker.io/crraay/habit-tracker-springboot:latest
   docker pull docker.io/crraay/habit-tracker-angular:latest
   ```

   Or build locally from each repo’s Dockerfile with the same tag names.

3. `docker compose up -d`
4. App on port **80**; API at **`/api/`** via nginx.

**Local dev overrides:** gitignored `docker-compose.override.yml` with `build.context` for either service if you prefer Compose to build locally instead of pulling.

## CI/CD (Docker Hub + DigitalOcean)

Each app repo has its own **`build-image`** and **`deploy-image-digital-ocean`** workflows (same names; independent chains).

### This repo (backend)

| Workflow | Trigger | Action |
|----------|---------|--------|
| **`build-image`** | Push to `main`, manual | Maven → push **`docker.io/crraay/habit-tracker-springboot:latest`** |
| **`deploy-image-digital-ocean`** | After **`build-image`** on `main`, manual | SSH → `git pull` → `compose pull/up backend` |

### Frontend repo

| Workflow | Trigger | Action |
|----------|---------|--------|
| **`build-image`** | Push to `main`, manual | `npm ci` → `test:ci` → push **`docker.io/crraay/habit-tracker-angular:latest`** |
| **`deploy-image-digital-ocean`** | After **`build-image`** on `main`, manual | SSH → `git pull` here → `compose pull/up frontend` |

You manage backend/frontend version matching manually (both use **`latest`**).

Images are **public** on Docker Hub — no `docker login` on the droplet for pull.

## GitHub Actions secrets and variables

### Backend repo secrets

| Secret | Used by |
|--------|---------|
| `DOCKER_HUB_USERNAME`, `DOCKER_HUB_TOKEN` | `build-image` |
| `SERVER_HOST`, `SERVER_USER`, `SERVER_PASSWORD` | `deploy-image-digital-ocean` |

### Frontend repo secrets

Same names — configure in **habit-tracker-angular** for its deploy workflow.

### Variables (optional, set in each repo that deploys)

| Variable | Default | Purpose |
|----------|---------|--------|
| `DEPLOY_BACKEND_DIR` | `/app/habit-tracker-springboot` | Backend clone on droplet (`docker-compose.yml` + `.env`) |
| `SERVER_SSH_PORT` | `22` | SSH port |
| `DEPLOY_SSH_COMMAND_TIMEOUT` | `15m` | Remote script timeout |

## Compose CLI vs `docker-compose.yml`

Use **Docker Compose v2** (`docker compose`). **`docker-compose-plugin`** is not in Ubuntu default repos — add [Docker’s official repository](https://docs.docker.com/engine/install/ubuntu/) first:

```bash
sudo apt-get install -y ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
docker compose version
sudo usermod -aG docker YOUR_USER
```

## Initial server setup (DigitalOcean droplet)

1. Install Docker Engine + Compose v2 (above); ensure deploy user is in **`docker`** group and **`docker ps`** works without sudo.
2. Clone **backend repo only:**

   ```bash
   sudo mkdir -p /app && sudo chown deploy:deploy /app
   git clone https://github.com/crraay/habit-tracker-springboot.git /app/habit-tracker-springboot
   ```

3. Copy `.env.example` to `.env`; set `POSTGRES_*`, `JWT_SECRET`, **`CORS_ALLOWED_ORIGINS`** (include your public URL, e.g. `http://YOUR_DROPLET_IP`).
4. **First start:**

   ```bash
   cd /app/habit-tracker-springboot
   docker compose pull
   docker compose up -d postgres
   docker compose ps   # wait until postgres is healthy
   docker compose up -d backend frontend
   ```

5. **Later deploys:** push to `main` in each repo (build → chained deploy), or run **deploy-image-digital-ocean** manually in GitHub Actions.

## Data safety (do not destroy the database volume)

- Postgres data: named volume **`postgres_data`**.
- **Never:** `docker compose down -v`, `docker volume rm`, `docker system prune --volumes`.
- Service deploys only **pull** and **restart** `backend` or `frontend` — Postgres unchanged.

## Troubleshooting

- **Backend fails healthcheck:** ensure `postgres` is healthy; check `.env` datasource settings.
- **Frontend 502 on `/api/`:** backend must be healthy; nginx proxies to `http://backend:8080`.
- **Deploy: Cannot connect to Docker daemon:** user must be in `docker` group; `docker` service running.
- **Deploy: `KeyError: ContainerConfig`:** legacy `docker-compose` v1 — install Compose v2 plugin; workflows stop/rm service before `up`.
- **Pull fails:** run **`build-image`** in the relevant repo at least once; confirm Hub repos are public.
