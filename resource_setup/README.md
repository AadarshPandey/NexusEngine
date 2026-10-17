# NexusEngine Resource Setup & Infrastructure Automation

Enterprise-grade infrastructure provisioning and automatic data seeding for NexusEngine.

## Overview

In the modernized architecture, **all resources and containers are populated with realistic data automatically**:
- **Zero manual database dumps**: PostgreSQL runs 8 domain schemas and 50 tables auto-migrated by Flyway (`V1__baseline.sql`, `V2__reference_data.sql`, `V3__demo_catalog.sql`).
- **No MinIO dependencies**: MinIO was deprecated in favor of **LocalStack S3** (`http://localhost:4566/nexus-media`), initialized via `s3-init/01-init-s3.sh`.
- **Automated Search Indexing**: Elasticsearch 9.1.0 auto-indexes all 152 products upon backend application boot via `ProductSearchIndexInitializer`.
- **Declarative Message Broker**: RabbitMQ 4 boots with `/nexuscore` virtual host and production routing topology via declarative `rabbitmq/definitions.json`.

---

## One-Command Quickstart

To spin up the entire cluster and seed all data:

```bash
# Option 1: Via Makefile
make setup

# Option 2: Via setup script directly
./resource_setup/setup.sh

# Option 3: Standard Docker Compose (all seed hooks trigger automatically!)
docker compose up -d --build
```

---

## Directory Structure

```text
resource_setup/
├── setup.sh                 # Single-command orchestrator & health verification script
├── rabbitmq/
│   ├── rabbitmq.conf        # Config enabling declarative definition loading
│   └── definitions.json     # Declarative vhosts (/nexuscore), queues, exchanges, permissions
├── s3-init/
│   └── 01-init-s3.sh        # LocalStack container hook creating buckets, CORS, and seeding media
├── s3-data/
│   └── nexus-media/         # Flat extracted public media assets (banners, category icons, SKUs)
└── README.md                # Infrastructure documentation
```

---

## Service Endpoints & Default Credentials

| Service | Endpoint | Credentials / Details |
|---|---|---|
| **Web Storefront** | http://localhost:5174 | Vite + React + Material UI 6 + TypeScript |
| **Admin Panel** | http://localhost:5173 | `admin` / `macro123` |
| **Backend API** | http://localhost:8080 | Swagger UI: `http://localhost:8080/swagger-ui.html` |
| **LocalStack S3 Console** | http://localhost:9001 | S3 bucket: `nexus-media` (Endpoint: `:4566`) |
| **RabbitMQ Management** | http://localhost:15672 | `guest` / `guest` (VHost: `/nexuscore`) |
| **Elasticsearch** | http://localhost:9200 | Index: `pms` |
| **PostgreSQL** | localhost:5433 | `postgres` / `postgres` (DB: `nexuscore`) |
| **Redis** | localhost:6379 | DB: `0` |
| **Prometheus** | http://localhost:9090 | Metrics scraper |
| **Grafana** | http://localhost:3000 | `admin` / `admin` |

### Additional Pre-Configured Test Accounts
- **Customer User**: `customer1` / `macro123` (Name: Aadarsh Pandey, pre-seeded with orders & addresses)
- **Brand Admin (Samsung)**: `samsung_admin` / `macro123`
- **Brand Admin (Nike)**: `nike_admin` / `macro123`

---

## Advanced Operations

- **Reset all state and volumes from scratch:**
  ```bash
  ./resource_setup/setup.sh --reset
  ```

- **Start infrastructure only (for local IDE development):**
  ```bash
  make infra-up
  # or: ./resource_setup/setup.sh --infra-only
  ```

- **Stop all services:**
  ```bash
  make down
  ```
