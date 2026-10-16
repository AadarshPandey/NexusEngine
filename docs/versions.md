# Canonical Version Manifest

This document records the authoritative Generally Available (GA) versions across the NexusEngine repository (Java 25 ecosystem, Node.js 22 ecosystem, and container infrastructure).

---

## 1. Backend Runtime & Libraries (Java 25 / Spring Boot)

| Component / Library | Version | Classification | Rationale / Notes |
|---|---|---|---|
| **Java JDK** | `25` | LTS / Target | OpenJDK 25 (Eclipse Temurin) |
| **Spring Boot Parent** | `4.1.1` | Core Framework | Spring Boot 4 baseline |
| **Spring AI** | `2.0.1` | AI Integration | Official Google GenAI & PgVector vectorstore integration |
| **Spring Cloud AWS** | `3.3.1` | Cloud / S3 | LocalStack S3 and AWS S3 storage client |
| **SpringDoc OpenAPI** | `3.1.1` | API Documentation | Spring Boot 4 native OpenAPI 3.0 & Swagger UI |
| **PgVector** | `0.1.6` | Vector DB Driver | Vector similarity functions & embeddings |
| **Flyway** | Managed by Boot | DB Migrations | Baseline migrations (V1, V2, V3) |
| **Redisson** | `4.7.0` | Redis Driver | High-concurrency caching & atomic operations |
| **Resilience4j** | `2.4.0` | Fault Tolerance | Circuit breakers, retry, and rate limiters |
| **Bucket4j** | `8.10.0` | Rate Limiting | Token bucket rate limiting on sensitive endpoints |
| **MapStruct** | `1.6.3` | Mapping | Compile-time DTO / entity mapping |
| **Razorpay Java SDK** | `1.4.6` | Payments | Webhook verification and order payment transactions |
| **Testcontainers BOM** | `1.20.4` | Integration Testing | Disposable PostgreSQL, Redis, and RabbitMQ test containers |
| **JaCoCo** | `0.8.14` | Code Coverage | Full Java 25 bytecode support |
| **Spotless Maven Plugin** | `2.44.3` | Code Formatting | Enforced formatting gate (4-space indent, import cleanup) |

---

## 2. Storefront Web (`NexusFrontendWeb`)

| Dependency | Version | Notes |
|---|---|---|
| **Node.js** | `^20.19.0 \|\| >=22.12.0` | Node LTS |
| **React** | `^19.2.8` | React 19 Concurrent Root |
| **React DOM** | `^19.2.8` | React 19 DOM Renderer |
| **MUI Material** | `^9.4.0` | Material UI component library |
| **Redux Toolkit** | `^2.13.0` | State management & slice architecture |
| **React Router** | `^8.3.1` | Modern client-side routing |
| **Vite** | `^8.3.2` | Build tool & HMR dev server |
| **TypeScript** | `^7.0.2` | Strict mode typing |
| **Vitest** | `^5.0.3` | Unit & integration testing runner |
| **Axios** | `^1.20.0` | HTTP client with JWT interceptor |

---

## 3. Admin Panel (`NexusAdminPanel`)

| Dependency | Version | Notes |
|---|---|---|
| **Node.js** | `^20.19.0 \|\| >=22.12.0` | Node LTS |
| **React** | `^19.1.0` | React 19 Concurrent Root |
| **React DOM** | `^19.1.0` | React 19 DOM Renderer |
| **MUI Material** | `^7.3.11` | Enterprise Material UI components |
| **MUI Date Pickers** | `^8.29.3` | Flash sale & promotion schedule date pickers |
| **Redux Toolkit** | `^2.13.0` | Global state management |
| **React Router** | `^8.4.0` | Admin layout & nested routing |
| **Recharts** | `^3.10.1` | Analytics dashboard visualization |
| **Vite** | `^8.3.2` | Production bundler & fast HMR |
| **TypeScript** | `^7.0.2` | Type-safe bounded context contracts |
| **Dayjs** | `^1.11.13` | Lightweight immutable date utility |

---

## 4. Container Infrastructure & Observability

| Service / Image | Version | Role / Description |
|---|---|---|
| **PostgreSQL + pgvector** | `pgvector/pgvector:pg17` | Primary relational database with vector search |
| **Redis** | `redis:8-alpine` | In-memory cache, token storage, and rate limiting |
| **RabbitMQ** | `rabbitmq:4-management` | Async event broker with management UI |
| **Elasticsearch** | `elasticsearch:9.4.5` | Search engine for full-text catalog queries |
| **LocalStack** | `localstack/localstack:3.8.0` | AWS S3 compatible mock storage |
| **Prometheus** | `prom/prometheus:v3.1.0` | Metrics scraping & time-series storage |
| **Grafana** | `grafana/grafana:11.5.2` | Open Source metrics visualization & dashboards |
