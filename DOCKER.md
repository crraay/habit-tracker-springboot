# Docker / production deploy

Production stack (Compose, `.env`, DigitalOcean deploy) lives in **[habit-tracker-compose](https://github.com/crraay/habit-tracker-compose)**.

This repo only builds and pushes the backend image:

- Workflow **`build-image`** → `docker.io/crraay/habit-tracker-springboot:latest`
- Deploy: run **`deploy-image-digital-ocean`** in the compose repo (manual)

Local backend-only Docker build: `docker build -t docker.io/crraay/habit-tracker-springboot:latest .`
