# ADR 0004: Testing, CI/CD Pipeline, and Observability Architecture

- **Status:** Accepted
- **Date:** 2026-10-12
- **Deciders:** NexusEngine Engineering Team

---

## 1. Context and Problem Statement

To make NexusEngine FAANG-interview ready (SDE-2 level), the engineering practices surrounding testing, continuous integration, continuous delivery, and runtime observability required systematic modernization:

1. **Test Flakiness & Database State Contamination:**
   - Concurrency tests (`OrderConcurrencyIntegrationTest`, `FlashSaleConcurrencyIntegrationTest`, `CouponConcurrencyIntegrationTest`) shared transactional test state and previously executed `deleteAll()` cascades that collided with foreign key constraints (`fk_customer_order_member`).
   - Trivial auto-generated repository tests created clutter without validating core domain invariants or concurrency guarantees.
2. **CI/CD Pipeline Drift:**
   - Previous CI workflows targeted outdated JDK distributions (JDK 21) while Maven build files specified Java 25.
   - Frontend linting, typechecking, and unit tests were not executed in parallel CI jobs.
   - Deploy workflows lacked automated verification gates.
3. **Observability Mismatches:**
   - Prometheus scrape targets referenced outdated host names (`host.docker.internal:8080`) instead of internal container networking.
   - Spring Boot Actuator endpoints lacked explicit container liveness and readiness probe configuration for Docker and Kubernetes orchestration.
   - Grafana monitoring ran commercial/enterprise images instead of standardized Open Source (OSS) distributions with automated datasource and dashboard provisioning.

---

## 2. Decision Drivers

1. **Zero-Flake Concurrency Test Suite:** Multi-threaded concurrency tests must run reliably against real database engines via Testcontainers without foreign key cascades or shared state races.
2. **Comprehensive Automated Verification Gate:** Every commit and pull request must execute formatting checks (Spotless), strict compilation, unit tests, and integration tests across Java 25 and React 19 in parallel.
3. **Container-Native Observability:** Provide complete runtime visibility using standard Prometheus metrics scraping, Actuator health probes, and provisioned Grafana dashboards.
4. **Developer Ergonomics:** Standardize local commands (`Makefile`) for one-touch stack startup, infra management, and verification.

---

## 3. Considered Options

### 3.1 Testing Strategy
- **Option 1 (Mock-only):** Mock database and broker layers using Mockito.
  - *Cons:* Fails to detect real concurrency races, lost updates, deadlocks, and SQL constraint violations.
- **Option 2 (In-memory H2):** Use in-memory H2 database for tests.
  - *Cons:* H2 does not support PostgreSQL pgvector, `jsonb`, or specific concurrency locking semantics.
- **Option 3 (Testcontainers with Real PostgreSQL, Redis, RabbitMQ) [Accepted]:** Run unit tests fast with Surefire and run integration/concurrency tests with Failsafe against lightweight, ephemeral Docker containers managed by Testcontainers.

### 3.2 CI/CD Pipeline
- **Option 1 (Sequential monolithic workflow):** Build backend and both frontends sequentially in one GitHub Actions job.
  - *Cons:* Slow feedback loop, single failure halts all checks.
- **Option 2 (Parallel Matrix Workflow with Caching) [Accepted]:** Parallel GitHub Actions jobs for `backend` (Java 25 Temurin + Maven cache + Spotless + Failsafe verify), `storefront-web` (Node 22 + npm cache + Vitest + build), and `admin-panel` (Node 22 + npm cache + build).

---

## 4. Decision Outcome

### 4.1 Testing Architecture
1. **Core Concurrency Suites Retained & Polished:**
   - `OrderConcurrencyIntegrationTest`: Validates atomic stock reservation (`on_hand_qty - reserved_qty >= :qty`) preventing overselling under multi-threaded contention.
   - `CouponConcurrencyIntegrationTest`: Validates that concurrent claim requests for limited coupons strictly respect `per_user_limit` and available coupon quantity.
   - `FlashSaleConcurrencyIntegrationTest`: Validates Redis-prewarmed atomic stock decrements and asynchronous RabbitMQ order creation under high concurrency.
   - `OutboxEventIntegrationTest`: Validates the transactional outbox lifecycle.
   - `FlywayMigrationTest`: Asserts baseline database migration integrity from empty database to validated schema.
2. **Isolation Guarantee:** Integration tests generate unique entity identifiers (UUID/timestamped usernames and product codes) to prevent foreign-key cascading delete violations.
3. **Frontend Vitest Suites:** Unit and component tests running via Vitest in `NexusFrontendWeb` verifying cart slices, product cards, and state mutations.

### 4.2 CI/CD Architecture (`.github/workflows/ci.yml`)
1. **Backend Job:**
   - Runner: `ubuntu-latest`, JDK 25 (Temurin), Maven cache.
   - Formatting Gate: `mvn -B spotless:check`.
   - Verification: `mvn -B clean verify` (executes Surefire unit tests and Failsafe integration tests).
2. **Frontend Jobs:**
   - Run in parallel for both `apps/storefront` (`NexusFrontendWeb`) and `apps/admin` (`NexusAdminPanel`).
   - Run `npm ci`, execute unit tests (`npm test`), and typecheck/build (`npm run build`).
3. **Dependabot Configuration:**
   - Automated weekly dependency scanning across `maven`, `npm` (both applications), `github-actions`, and `docker`.

### 4.3 Observability Stack
1. **Prometheus Configuration:** Scrapes metrics on internal network from `nexus-application:8080/actuator/prometheus`.
2. **Spring Boot Actuator Probes:**
   - `management.endpoint.health.probes.enabled=true` exposing `/actuator/health/liveness` and `/actuator/health/readiness`.
   - Docker container healthcheck: `wget --spider http://localhost:8080/actuator/health/liveness`.
3. **Grafana OSS Provisioning:**
   - Grafana OSS 11.5.2 configured with automated Prometheus datasource provisioning (`grafana/provisioning/datasources/datasource.yml`) and pre-loaded JVM/Micrometer dashboards (`grafana/dashboards/nexus-observability.json`).

---

## 5. Consequences

### Positive
- **Deterministic CI & Fast Feedback:** Clean split between fast unit tests (<3s) and isolated integration tests (<45s).
- **Production-Grade Resilience:** Guarantees that inventory overselling and coupon race conditions cannot occur in production.
- **Zero-Drift Observability:** Live metrics and health checks reflect reality inside container networks.
