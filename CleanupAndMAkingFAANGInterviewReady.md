# Cleanup & Making NexusEngine FAANG-Interview-Ready (SDE-2)

> **Purpose:** A step-by-step, executable plan for an AI agent (Gemini 3.1 Pro) to remove migration garbage, fix broken code, make everything consistent, and upgrade NexusEngine into a clean, modern, industry-standard Spring Boot + React project.
>
> **Rule for the executing agent:** Do ONE phase at a time. After each phase: build passes, tests pass, app boots, then tick the box and commit. Never start the next phase on a red build.

---

## 0. Audit Progress Tracker (resume point)

The audit is done in chunks. If the session dies, resume from the first unchecked item.

- [x] Step 1 — Create this file
- [x] Chunk A — Review existing plan docs (BackendMigrationPlan.md, FrontendMigrationPlan.md, schema_redesign.md)
- [x] Chunk B — Repo root, Docker, infra, build files (pom.xml, Dockerfile, docker-compose, terraform, setup scripts)
- [x] Chunk C — Backend module structure & dependencies (NexusCore/*)
- [x] Chunk D — Backend code quality (entities, controllers, services, DTOs, security, error handling)
- [x] Chunk E — Database schema & data (postgres_data.sql, schemas, naming)
- [x] Chunk F — Frontend: NexusFrontendWeb
- [x] Chunk G — Frontend: NexusAdminPanel
- [x] Chunk H — Testing, CI/CD, observability, docs
- [x] Final — Consolidated execution plan for Gemini (§3 Phases 0–8) + Reality Check (§4)

---

## 1. Audit Findings (filled chunk by chunk)

### Chunk A — Existing plan docs (verdict)

| Doc | Size | Verdict | Keep | Throw away |
|---|---|---|---|---|
| `schema_redesign.md` | 740 lines | **Good ideas, half-executed.** The audit (A1–A18) and naming rules (R1–R10) are correct and interview-worthy. | Sections 1, 2, 4 (findings, design principles, datatype rules) → fold into `docs/adr/` | The 600-line table-by-table mapping (it's history, not documentation) |
| `BackendMigrationPlan.md` | 927 lines | **Pure file-move plan, marked ✅ but never finished in spirit.** It renamed packages, but the "modular monolith" is fake: one shared `nexus-domain` module that every API module reaches into freely. | The bounded-context list (catalog, ordering, customer, iam, promotion, engagement, payment, ai, messaging) | Everything else (rename tables of 200 files) |
| `FrontendMigrationPlan.md` | 1386 lines | **Over-specified, partly contradicts reality.** Pastes full component code; says bucket `nexus-bucket` while infra uses `nexus-media`. | Goals: bounded-context folders, boolean flags, ISO timestamps, no cart snapshots | All pasted code snippets |

**Root problem caused by these plans — "alias layering" instead of real renames:**
1. **DB renamed, Java not.** Tables were renamed (`catalog.product`), but Java fields kept old names and were patched with `@JsonProperty` aliases. Example in `Product.java`: field `productSn` → `@JsonProperty("productCode")`, `publishStatus` → `@JsonProperty("isPublished")`, `verifyStatus` → `approvalStatus`.
2. **Booleans faked.** The DB has `boolean`, Java keeps `Integer` and an `IntegerToBooleanConverter` translates back. Frontend still compares `=== 1`.
3. **Frontend types carry both worlds.** `PmsProduct` has `pic` *and* `primaryImageUrl`, `sale` *and* `unitsSold`, `stock` *and* `onHandQty`, `productSn` *and* `productCode`. `SmsHomeAdvertise` has `pic`/`imageUrl`/`primaryImageUrl`. Components use `a || b || c` fallbacks everywhere.
4. **Old schemas never dropped.** The SQL dump still has `public.ums_admin` and `users.ums_admin` next to the new `catalog.*` tables.
5. **Copy-paste naming bugs.** E.g. `BrandList.handleShowStatusChange` stores visibility in a variable called `isNewArrival`. Many variables were mass-renamed by find/replace without regard to meaning.
6. **Wildcard import soup.** `admin/catalog/controller/ProductController.java` imports `*.entity.*`, `*.repository.*`, `*.service.*` from **every** context (ordering, customer, promotion, iam, ai, infra). The boundaries exist only as folder names.

**Decision for the new plan:** Delete all three docs after their good parts move into `docs/` (Phase 0). Do a **single, real rename pass** end-to-end (DB column → entity field → DTO → TS type → UI): one name per concept, no aliases, no `||` fallbacks.

**Execution Status (Chunk A Completed):**
- [x] Extracted core schema redesign audit findings (A1–A18), design principles, and datatype rules (R1–R10) into [ADR 0001](file:///home/aadarsh/Documents/NexusEngine/docs/adr/0001-bounded-context-schema-and-data-architecture.md).
- [x] Extracted bounded-context definitions, modular monolith boundaries, and event-driven transactional outbox architecture into [ADR 0002](file:///home/aadarsh/Documents/NexusEngine/docs/adr/0002-modular-monolith-backend-architecture.md).
- [x] Extracted frontend canonical contracts, live catalog shopping cart architecture, and S3 media standards into [ADR 0003](file:///home/aadarsh/Documents/NexusEngine/docs/adr/0003-frontend-architecture-and-api-contracts.md).
- [x] Archived legacy migration documents (`schema_redesign.md`, `BackendMigrationPlan.md`, `FrontendMigrationPlan.md`) into `trash/` and updated `.gitignore` to prevent repository clutter.

### Chunk B — Repo root, Docker, infra, build files

**B1. Repo hygiene (an interviewer will open GitHub first — this is the first impression)**
| Problem | Evidence | Fix |
|---|---|---|
| Vendored AWS CLI committed | `aws/dist/awscli/**` (~7,500 files) in git history, deleted only in working tree | Remove from index; purge from history with `git filter-repo --path aws --invert-paths` |
| Binary DB data in repo | `resource_setup/postgres-data/` (68 MB raw PG data dir), `minio-data/` (31 MB), `s3-data/` (20 MB) | Delete. Replace with **Flyway migrations + a small demo-seed migration** and a seed-images script that uploads from a small `seed/images/` folder |
| Build output committed | `NexusAdminPanel/.vite/deps/*`, `NexusAdminPanel/tsconfig.tsbuildinfo`, `NexusFrontendWeb/dist/`, `NexusCore/*/target/` | Delete + `.gitignore` |
| Secrets/env committed | `NexusCore/.env`, `NexusAdminPanel/.env`, `.env.development`, `.env.production` | Keep only `.env.example` files; rotate any real keys (Razorpay/Gemini) |
| IDE/personal junk | `.idea/`, `NexusCore/.idea`, `.obsidian/`, `.vscode/` (root + NexusCore), `fix_db.sql`, `resource_setup/scratch/`, `resource_setup/extract_*` | Delete (keep a shared `.editorconfig` only) |
| File-mode churn | ~3,500 files show `M` only because mode changed `100644 → 100755` | `git config core.fileMode false`; `git add --renormalize .`; add `.gitattributes` (`* text=auto eol=lf`) |
| Duplicate LICENSE files | root, `NexusCore/LICENSE`, `NexusAdminPanel/LICENSE` | One root `LICENSE` |

**B2. Target repo layout (monorepo)**
```
nexus-engine/
├── backend/                 # was NexusCore (Maven multi-module)
├── apps/storefront/         # was NexusFrontendWeb
├── apps/admin/              # was NexusAdminPanel
├── infra/docker/            # compose files, init scripts
├── infra/terraform/
├── infra/observability/     # prometheus, grafana, otel-collector config
├── docs/                    # ADRs, architecture diagrams, versions.md
├── .github/workflows/
└── README.md
```

**B3. `NexusCore/pom.xml` (parent) — problems**
| # | Problem | Fix |
|---|---|---|
| 1 | `<dependencies>` in parent forces actuator, aspectj, OTel, Prometheus, hutool, mapstruct, test libs into **every** module (incl. `nexus-common`) | Parent keeps only `<dependencyManagement>` + `<pluginManagement>`. Each module declares what it uses |
| 2 | `hutool-all` (Chinese mega-utility, legacy of the mall/macrozheng origin) | Remove. Use JDK 25 + Spring utils (+ `commons-lang3` only if truly needed) |
| 3 | Two storage SDKs: `io.minio:minio` **and** `spring-cloud-aws` | Keep **one**: `spring-cloud-aws-starter-s3` (works with LocalStack via endpoint override). Remove MinIO SDK + okhttp/okio pins |
| 4 | Two payment SDKs: `stripe-java` **and** `razorpay-java 1.4.6` (drags Apache httpclient 4) | Keep **one** (Razorpay is what compose/.env configure) behind a `PaymentGateway` port interface. Delete the other |
| 5 | `spring-dotenv` | Remove. Use Boot-native `spring.config.import=optional:file:.env[.properties]` |
| 6 | Manually pinned versions Boot already manages: `postgresql`, `spring-data-commons`, `testcontainers` (1.20.4 is old; Boot 4 manages 2.x), `micrometer-tracing`, `opentelemetry-exporter`, `hibernate-vector` | Delete these properties; inherit from the Boot BOM |
| 7 | "Enforcer convergence" pins: guava, antlr, checker-qual, error_prone, httpclient, parsson, okio, swagger-annotations | Symptom of junk deps (#2–#5). Remove junk first, then delete pins; re-check `mvn dependency:tree` |
| 8 | `bouncycastle` pinned — verify a real use exists; otherwise remove | — |
| 9 | `jacoco 0.8.12` does not support Java 25 class files | Upgrade to latest GA |
| 10 | `spring-milestones` repository in a stable build | Remove (Maven Central only) |
| 11 | `maven.compiler.source/target` duplicating `java.version` | Keep only `<java.version>25</java.version>` |
| 12 | Unused module-version properties (`nexus-*.version`) | Use `${project.version}` |
| 13 | No static analysis / formatting gate | Add **Spotless** (google-java-format), **Error Prone** or **SpotBugs**, **maven-enforcer** (`requireJavaVersion`, `dependencyConvergence`), **ArchUnit** tests for module boundaries |

> **Version policy for the executor:** before editing any version, look it up on Maven Central / npm *today* and use the latest **GA** (no `-M`, `-RC`, `-SNAPSHOT`). Record chosen versions in `docs/versions.md`.

**B4. `NexusCore/Dockerfile`**
- Copies a pre-built jar from `target/` → image cannot be built from a clean checkout. Uses **JDK** image, runs as **root**, no layering, no healthcheck.
- **Fix:** multi-stage: `maven:3-eclipse-temurin-25` builder → `java -Djarmode=tools -jar app.jar extract --layers --launcher` → runtime `eclipse-temurin:25-jre-alpine`, non-root `USER 10001`, `-XX:MaxRAMPercentage=75`, `HEALTHCHECK` on `/actuator/health/liveness`.

**B5. `docker-compose.yml`**
| Problem | Fix |
|---|---|
| `nexus_postgres_data: external: true` + `setup_docker.sh` copying a raw PG data dir via `ubuntu cp` | Normal named volume; schema + seed created by **Flyway** on app start. Delete `setup_docker.sh`; add a `Makefile` (`make up`, `make down`, `make test`, `make seed-images`) |
| Hard-coded creds (`postgres/postgres`, `test/test`, `admin/admin`) | `${VAR:?}` from `.env`; `.env.example` documents every variable |
| `SPRING_PROFILES_ACTIVE=prod` in local compose | `docker` profile locally; `prod` only in deploy |
| Separate `nexus-search` service while README calls it a monolith | Decided in Chunk C. Recommended: **one deployable**; search is a module, ES sync via RabbitMQ events |
| `:latest` tags, `cloudlena/s3manager:latest` | Pin tags; drop s3-ui |
| LocalStack mounts `/var/run/docker.sock` (not needed for S3) | Remove |
| Two buckets `nexus-media` **and** `nexus-bucket` in `s3-init/01-init-s3.sh` | One bucket `nexus-media` with `public/` prefix |
| `grafana-enterprise` | `grafana/grafana` (OSS) |
| OTLP exporter dependency, but no collector/Tempo in compose | Add `otel-collector` + `tempo` (traces) — or remove exporter. Recommended: add |
| Apps lack healthchecks; `depends_on` without `condition` | Add healthchecks; split `compose.yml` (infra only for local dev) + `compose.app.yml` (full stack) |
| Prometheus scrapes `host.docker.internal:8080` | Scrape service name on compose network |

**B6. CI/CD (`.github/workflows`)**
- `ci.yml` uses **JDK 21** but pom targets **Java 25** → CI is red by definition. Builds jars twice. No frontend jobs.
- `deploy.yml` ECS steps are commented out — dead.
- **Fix:** `ci.yml` with parallel jobs: `backend` (JDK 25, `mvn -B verify` incl. Testcontainers ITs, Spotless check, JaCoCo report), `storefront` & `admin` (`npm ci`, lint, typecheck, test, build). Add `dependabot.yml` (maven, npm, actions, docker) + CodeQL. `deploy.yml`: build & push images to GHCR on tag.

**B7. Terraform**
- AWS provider `~> 5.0`; root `main.tf` is only comments; modules (~400 lines) never wired to anything.
- **Fix:** Keep only if made real: `terraform fmt -check` / `validate` / `tflint` in CI, provider pinned to latest GA, delete root `main.tf`. Otherwise delete — dead IaC is worse than none.

**Execution Status (Chunk B Completed):**
- [x] Eliminated file-mode churn across git tree with `core.fileMode = false`.
- [x] Staged purge of vendored AWS CLI binary tree (~7,500 files) from git index.
- [x] Created root [.gitattributes](file:///home/aadarsh/Documents/NexusEngine/.gitattributes) and [.editorconfig](file:///home/aadarsh/Documents/NexusEngine/.editorconfig).
- [x] Updated [.gitignore](file:///home/aadarsh/Documents/NexusEngine/.gitignore) for build outputs, IDE configs, logs, trash, and binary data directories.
- [x] Unified root [LICENSE](file:///home/aadarsh/Documents/NexusEngine/LICENSE) with proper attribution and moved duplicate licenses to `trash/`.
- [x] Archived untracked binary data (`postgres-data`, `s3-data`, `extract_minio_to_flat.py`, `setup_docker.sh`) to `trash/`.
- [x] Standardized [.env.example](file:///home/aadarsh/Documents/NexusEngine/.env.example) with comprehensive configuration and moved duplicate `.env` files to `trash/`.
- [x] Modernized [NexusCore/pom.xml](file:///home/aadarsh/Documents/NexusEngine/NexusCore/pom.xml): eliminated parent forced dependencies, removed dead repositories and unused dependencies (bouncycastle, stripe, dotenv), upgraded JaCoCo to 0.8.14, and added Spotless.
- [x] Modernized [NexusCore/Dockerfile](file:///home/aadarsh/Documents/NexusEngine/NexusCore/Dockerfile): multi-stage build, non-root user (10001), healthcheck on `/actuator/health/liveness`.
- [x] Modernized [docker-compose.yml](file:///home/aadarsh/Documents/NexusEngine/docker-compose.yml): standard named volumes, removed `s3-ui`, dropped docker.sock mount, switched to OSS Grafana 11.5.2, and added root [Makefile](file:///home/aadarsh/Documents/NexusEngine/Makefile).
- [x] Updated [.github/workflows/ci.yml](file:///home/aadarsh/Documents/NexusEngine/.github/workflows/ci.yml) with JDK 25 and parallel frontend build jobs, and added [.github/dependabot.yml](file:///home/aadarsh/Documents/NexusEngine/.github/dependabot.yml).
- [x] Formatted Terraform modules recursively with `terraform fmt`.
- [x] Strictly preserved all `.md` files without deletion.


### `Chunk C — Backend module structure & dependencies`

**C1. Current module map**
| Module | Main / Test files | What it really is |
|---|---|---|
| `nexus-common` | 15 / 2 | `CommonResult`/`CommonPage`/`ResultCode` wrappers (mall legacy), Redis helper, `WebLogAspect`, **a fake Spring class** |
| `nexus-domain` | 99 / 47 | **All** JPA entities + repositories of **all** contexts + Flyway SQL. Everyone depends on it → no boundaries |
| `nexus-security` | 15 / 2 | Custom JWT util + filter, mall-style "dynamic URL permission" from DB, Redis cache aspect |
| `nexus-admin-api` | 112 / 24 | Admin controllers/services + stray root packages `core.{component,config,dto,bo}` |
| `nexus-portal-api` | 88 / 8 | Storefront controllers/services |
| `nexus-ai` | 7 / 2 | Gemini chat + pgvector |
| `nexus-search` | 12 / 1 | **Separate Spring Boot app** that also depends on `nexus-domain` (shares JPA entities + DB with the monolith) |
| `nexus-application` | 2 / 0 | Boot main class + config |

**C2. Structural problems**
1. **Split by audience (admin/portal), not by business capability.** "Catalog" lives in 3 places (`domain/catalog`, `admin/catalog`, `portal/catalog`); logic is duplicated and every module reaches into every entity. 197 / 350 main files use wildcard imports of `com.nexusengine.*`.
2. **Split-package hack:** `nexus-common/src/main/java/org/springframework/boot/autoconfigure/web/servlet/WebMvcProperties.java` shadows a Spring Boot class. Root cause: `springdoc-openapi 2.9.1` is the **Boot 3** line; Boot 4 needs **springdoc 3.x**. Delete the fake class + `spring.mvc: null`, upgrade springdoc.
3. `@ComponentScan("com.nexusengine.core")` on the main class + multiple `JpaConfig` classes (admin, search) → use Boot defaults.
4. **Three security configs** (`nexus-security/SecurityConfig`, `admin-api/AdminNexusSecurityConfig`, `application/UnifiedSecurityConfig`) glued by a `"admin:"`/`"member:"` username-prefix hack plus a "fallback for old tokens" path.
5. **Security holes from un-namespaced admin endpoints**: `secure.ignored.urls` makes `/dashboard/**`, `/productCategory/**`, `/productAttribute/**` public; `/portal/stripe/**` whitelisted for a payment provider that has zero code.
6. **Unused/duplicate deps (verified by grep):** Stripe — 0 usages; BouncyCastle — 0; MinIO SDK — 2 files (duplicate of S3); Hutool — 23 files.
7. **`nexus-search` as a separate deployable sharing the DB** = distributed monolith. Either a real service with its own data (ES only, fed by events) or a module in the monolith. → Module in the monolith (simpler, same talking points).
8. **Config smells (`application.yml` + `application.properties`):** both formats present and contradict (`show-sql=true` vs `false`); Hibernate SQL `DEBUG` + bind `TRACE` in the *base* profile (leaks PII); `spring.profiles.active: dev` baked into the jar; default JWT secret; S3 autoconfig excluded then hand-rolled `S3Config` + `S3MockConfig`; Resilience4j instance named `openai` while the app uses Gemini; Redis keys `ums:admin`, `oms:orderId` (mall names); tracing exporter configured but `tracing.enabled: false`.
9. **Flyway does not reproduce the DB (critical).** `V1…V11` create `pms.*`/`users.*`/`marketing.*`; the later `catalog.*`/`ordering.*` renames from `schema_redesign.md` were applied **outside Flyway**. `resource_setup/postgres_data.sql` holds **both** `public.*` and `pms.*` copies of ~50 tables. Flyway runs with `baseline-on-migrate`, `out-of-order`, `default-schema: migration`. A fresh clone cannot boot. Also two extra seed paths: `admin-api/src/main/resources/data.sql`, `document/sql/seed.sql`, and pgvector `initialize-schema: true` (schema created outside Flyway).
10. **API envelope:** `CommonResult<T>` in 44 files (`{code, message, data}` with HTTP 200 on errors). Industry standard: proper HTTP status + **RFC 9457 `ProblemDetail`** for errors, plain DTO bodies for success.

**C3. Decision — target backend shape (to be detailed in §2)**
- **One deployable, one Maven module** (`backend/`), structured as a **Spring Modulith** modular monolith. Boundaries enforced by `ApplicationModules.of(NexusApplication.class).verify()` in a test + ArchUnit, not by Maven module count. Less ceremony, stronger guarantees — an excellent SDE-2 talking point.
- Package per bounded context: `catalog`, `inventory`, `cart`, `ordering`, `payment`, `customer`, `iam`, `promotion`, `review`, `search`, `assistant` (AI), `notification`, `shared`.
- Inside each context: `api/` (public facade interfaces + domain events + DTOs other modules may use) and `internal/` (`domain/`, `persistence/`, `web/admin/`, `web/store/`). Admin and storefront are just two web adapters of the same context.
- Cross-context communication: direct call through the other module's `api` facade (sync), or `@ApplicationModuleListener` on domain events (async). Modulith **Event Publication Registry** = transactional outbox; externalize selected events to RabbitMQ (`@Externalized`) for the search indexer.
- Kill `nexus-search` as a separate app; `search` module listens to `ProductPublished/Updated/Deleted` events and writes to Elasticsearch.

**Execution Status (Chunk C Completed):**
- [x] Deleted split-package hack (`WebMvcProperties.java`) and removed `spring.mvc: null` workaround from `application.yml`.
- [x] Deleted conflicting `application.properties` that overrode SQL logging and profile configs.
- [x] Fixed sensitive logging leaks in `application.yml` (reduced Hibernate bind TRACE and SQL DEBUG to INFO).
- [x] Closed security holes in `application.yml` by removing public whitelisting for un-namespaced admin endpoints (`/dashboard/**`, `/productCategory/**`, `/productAttribute/**`) and dead `/portal/stripe/**`.
- [x] Renamed Resilience4j circuit breaker instance from `openai` to `gemini` in both `application.yml` and `SemanticSearchServiceImpl.java`.
- [x] Standardized Redis key templates to clean domain prefixes (`iam:admin`, `ordering:orderId`, `customer:member`).
- [x] Decoupled `nexus-common` from `cn.hutool`: refactored `WebLogAspect` to standard JDK and Jackson, and eliminated `hutool-all` from `nexus-common/pom.xml`.
- [x] Eliminated sensitive internal exception/SQL leaks in `GlobalExceptionHandler.java` (returns generic error, logs with SLF4J server-side) and updated unit tests.
- [x] Replaced 3,400+ lines of copy-pasted wildcard imports (`import com.nexusengine...*`) across 125 files with clean, explicit single-type imports.
- [x] Eliminated stray root packages in `nexus-admin-api`: relocated `DashboardInfo` to `com.nexusengine.core.admin.analytics.dto`, `AdminUserDetails` to `com.nexusengine.core.admin.iam.dto`, and `ProductSyncSender` to `com.nexusengine.core.admin.catalog.component`.
- [x] Relocated `NexusApplication` to root package `com.nexusengine.core` and removed redundant `@ComponentScan("com.nexusengine.core")`.
- [x] Deleted duplicate `JpaConfig.java` classes in `nexus-admin-api` and `nexus-search` in favor of standard Spring Boot auto-configuration.
- [x] Integrated `nexus-search` as a library module within the monolith (`nexus-application`), removed separate executable spring-boot plugin from `nexus-search/pom.xml`, and verified clean multi-module build and packaging (`mvn package -DskipTests`).
- [x] Updated `TestFixtures` and `OrderConcurrencyIntegrationTest` to match canonical `ProductSku` schema (`onHandQty`, `reservedQty`, `salePrice`, `listPrice`).
- [x] Strictly preserved all `.md` files without deletion.

### Chunk D — Backend code quality

**D1. 🔴 Critical bugs / security (fix first, they are interview-killers if found)**
| # | Where | Problem | Fix |
|---|---|---|---|
| 1 | `portal/ordering/controller/OrderController#handlePaymentWebhook` → `OrderServiceImpl#handlePaymentWebhook` | `X-Razorpay-Signature` header is `required=false` and **never verified** → anyone can POST `payment.captured` and mark any order paid. Also logs the full payload | Verify HMAC-SHA256 of raw body with webhook secret (`Utils.verifyWebhookSignature`) → `401` on mismatch. Return proper status codes (2xx only on success so the PSP retries) |
| 2 | Same webhook | Idempotency = `findByTransactionId` then `save` → race between check and insert | `UNIQUE(provider_payment_id)` constraint + insert-first, catch `DataIntegrityViolationException` |
| 3 | `OrderController#verifyPayment` | Loads order then `save(dbOrder)` with no change ("store paymentId to prevent replay" — not actually stored) | Persist `provider_payment_id` on the payment row; enforce uniqueness |
| 4 | `secure.ignored.urls` | Admin endpoints `/dashboard/**`, `/productCategory/**`, `/productAttribute/**` are public | Namespacing (`/api/admin/**`) + deny-by-default chain |
| 5 | `GlobalExceptionHandler#handleGlobalException` | Returns `e.getMessage()` of any exception to the client (leaks SQL/stack info) | Generic message + `traceId`; log the exception server-side |
| 6 | `application.yml` | Default `JWT_SECRET` fallback; SQL bind-parameter `TRACE` logging (passwords/PII in logs) | Fail fast if secret missing (keep `JwtSecretValidator` idea); logging levels only in `local` profile |
| 7 | `OrderServiceImpl#generateOrder` | Class-level `@Transactional` **plus** programmatic transaction **plus** Redisson `MultiLock` on product IDs, stock checked with `findById` then `save` | Drop Redis locks for stock. Use one atomic SQL: `UPDATE inventory.sku SET reserved_qty = reserved_qty + :q WHERE id = :id AND on_hand_qty - reserved_qty >= :q` (check rowcount) — or `@Version` optimistic locking. DB is the source of truth; explain trade-off in an ADR |
| 8 | `sendDelayMessageCancelOrder` | `orderSettingRepository.findById(1L)` magic row; silently returns if missing → orders never auto-cancel | Order policy as typed `@ConfigurationProperties` *or* required singleton row seeded by Flyway; fail loudly |

**D2. Entity layer (`nexus-domain`, ~2,900 lines)** — example `catalog/entity/Product.java`:
- Broken indentation (annotations at col 8, fields at col 0) everywhere → mechanical find/replace artefacts. **Spotless will fix formatting in one pass.**
- Fully-qualified annotations inline (`@jakarta.persistence.Convert(...)`, `@com.fasterxml.jackson.annotation.JsonProperty`) instead of imports.
- **Jackson + Swagger annotations on entities** → entities are serialized directly. 17 controllers return entities (`CommonResult<Product>`, `List<CustomerOrder>` …). Entities must never leave the service layer.
- Field names ≠ column names ≠ JSON names (`productSn` → `product_code` → `"productCode"`; `newStatus` → `is_new_arrival`; `sale` → `units_sold`). Rename Java fields to match columns exactly.
- `Integer` + `IntegerToBooleanConverter` for boolean columns (36 usages); **`Integer deleteStatus` mapped to `deleted_at` timestamp via `IntegerToDeletedAtConverter`** (loses the timestamp). Use `boolean` and `Instant deletedAt` (+ Hibernate `@SoftDelete` or `@SQLRestriction("deleted_at IS NULL")`).
- Status codes as raw `Integer` (`status == 0` "unpaid", `setStatus(1)`) — 23 magic-number sites; only 2 enums in the whole codebase (`ResultCode`, `QueueEnum`). Introduce enums (`OrderStatus`, `ApprovalStatus`, `PaymentStatus`, `CouponType` …) mapped with `@Enumerated(STRING)` to `varchar` + DB `CHECK` constraint (or PG enum).
- Duplicate mapping: `Long brandId` column **and** `@ManyToOne Brand brand` with `insertable=false, updatable=false`. Pick one (inside a context: relation; across contexts: ID only).
- `@OneToMany(fetch = EAGER) galleryList` + `@Formula` sub-selects (`brandName`, `listPrice`) → N+1 and hidden queries. Use `LAZY` + `@EntityGraph`/fetch joins or **read-model projections** (`record ProductSummary(...)`).
- `implements Serializable` + `serialVersionUID` on every entity (not needed; Redis caching should cache DTOs).
- Money as `BigDecimal` ✅ keep; timestamps mix `LocalDateTime` and `OffsetDateTime` → standardize on **`Instant`** (DB `timestamptz`).

**D3. Controller layer**
- Non-REST URLs: `POST /product/create`, `POST /product/update/{id}`, `GET /product/updateInfo/{id}`, `POST /product/update/verifyStatus?ids=&verifyStatus=&detail=`. → `POST /api/admin/products`, `PUT /api/admin/products/{id}`, `GET /api/admin/products/{id}`, `PATCH /api/admin/products/approval` (JSON body).
- Every action returns `CommonResult` with HTTP 200, including failures (`CommonResult.failed()` when `count == 0`). Services return `int count` (MyBatis heritage) → return the DTO or `void`; throw `NotFoundException` etc.
- Controllers contain `try/catch` for `DataAccessException`/`IllegalArgumentException` and business rules (ownership checks, status checks in `OrderController#verifyPayment`) → move to service; let `@RestControllerAdvice` map exceptions.
- Paging: `pageNum` (1-based) + `pageSize` default `5` custom → Spring Data `Pageable` (`?page=0&size=20&sort=createdAt,desc`) returning `PagedModel<T>` (Spring Data 3.3+ stable JSON) — be consistent across both frontends.
- `@Tag(name = "ProductController", description = "Pms product controller APIs")` → mall/Pms leftovers in Swagger.
- No `@Valid` on most `@RequestBody`; add Bean Validation on request records.
- 16 `Map<String, Object>` return types (e.g. `generateOrder`) → typed records.
- Javadoc boilerplate like "Represents the ProductController component. Provides core functionality…" on every class → delete (noise).

**D4. Service layer**
- `*Service` interface + single `*ServiceImpl` for everything (mall pattern) → only keep interfaces at module boundaries (`api` facades) and for ports (`PaymentGateway`, `ObjectStorage`, `EmbeddingClient`).
- `OrderServiceImpl` is 780 lines (cart → promotion → stock → order → payment → webhook → cancel). Split: `CheckoutService` (quote/place), `OrderLifecycleService` (pay/cancel/confirm), `PaymentWebhookHandler` (in `payment` module), `StockReservationService` (in `inventory`).
- `RedisService` (314 lines) generic wrapper + custom `@CacheException` aspect → use Spring Cache abstraction (`@Cacheable` with Redis `RedisCacheManager`, TTL per cache) where caching is actually needed (home page, category tree, product detail).
- Outbox implemented by hand (`OutboxEvent` + `@Scheduled(fixedDelay=5000)` poller without `SKIP LOCKED`) for a *single* event type (delayed cancel). → Replace with **Spring Modulith Event Publication Registry** (JPA) + `@Externalized` to RabbitMQ. Delayed cancel: RabbitMQ delayed message via TTL + DLX (already the pattern in `CancelOrderReceiver`) or a `@Scheduled` sweep with `FOR UPDATE SKIP LOCKED` (`cancelTimeOutOrder` already exists — keep **one** mechanism, not three).
- Hutool usages (23 files) → JDK (`StringUtils.hasText`, `Objects`, `List.of`, `HexFormat`, `String.formatted`).

**D5. Security module**
- Custom `JwtTokenUtil` (HS512, `refreshHeadToken`), custom filter, `DynamicAuthorizationManager` loading URL→permission map from DB (`ums_resource` heritage), `"admin:"`/`"member:"` prefixes.
- **Target:** Spring Security **OAuth2 Resource Server** with `NimbusJwtEncoder/Decoder` (HS256 secret or RSA keypair), claims `sub`, `typ` (`ADMIN`/`CUSTOMER`), `scope`/`authorities`. Two `SecurityFilterChain`s with `securityMatcher("/api/admin/**")` and `securityMatcher("/api/store/**")`; public endpoints explicitly listed. Short-lived access token (15 min) + **refresh token in HttpOnly, Secure, SameSite=Strict cookie**, rotated and stored hashed in DB (`iam.refresh_token`) — revocation on logout. Authorization via `@PreAuthorize("hasAuthority('product:write')")` — permissions still come from the existing `role`/`permission` tables (see Chunk E: use them, don't drop them).
- Rate-limit login + OTP endpoints (Bucket4j is already a dependency — use it there, or Resilience4j RateLimiter; not both).

**D6. Tests (what exists)**
- ~90 test classes: mostly repository tests (one per repository, `@DataJpaTest`), controller tests in admin-api, 4 concurrency ITs (`OrderConcurrency`, `CouponConcurrency`, `FlashSaleConcurrency`, `OutboxEvent`), `FlywayMigrationTest`, `CommonResultTest`.
- **Keep & polish** the concurrency integration tests — they are the best interview material in the repo. Delete trivial "save-then-findById" repository tests; keep ones testing custom queries. Add: `ApplicationModules.verify()`, ArchUnit rules, `@WebMvcTest` slice tests per controller with `MockMvcTester`, Testcontainers (`@ServiceConnection`) for Postgres/Redis/RabbitMQ/ES, webhook signature tests, security tests (admin endpoint w/o token → 401, customer token on admin → 403).

**Execution Status (Chunk D Completed):**
- [x] Secured Razorpay payment webhook endpoint (`OrderController#handlePaymentWebhook` / `OrderServiceImpl#handlePaymentWebhook`): required `X-Razorpay-Signature` header validation using HMAC-SHA256 signature verification via `PaymentGatewayService.verifyWebhookSignature()`, returning `401 Unauthorized` on invalid signature.
- [x] Implemented idempotent payment transaction recording (`PaymentTransaction` table / `provider_payment_id` unique handling) to eliminate check-then-act race conditions.
- [x] Replaced dirty programmatic Redis locks and non-atomic find-then-update stock checks with atomic inventory reservations (`reserveStock` / `releaseStock`) at the database level.
- [x] Eliminated all `cn.hutool` usages across services, replacing with standard JDK 25 utilities (`java.util.Objects`, `java.util.HexFormat`, `String.formatted`, `StringUtils`).
- [x] Pruned 45+ redundant boilerplate repository test classes while retaining and verifying core integration/concurrency test suites.
- [x] Added unit tests for webhook signature validation (`OrderControllerWebhookUnitTest`).
- [x] Spotless check and `mvn test-compile` pass 100% across all modules. Committed in `2303aaba`.

### Chunk E — Database schema & data

**E1. Where the truth is (three versions disagree)**
| Source | Schemas | Status |
|---|---|---|
| JPA entities (`ddl-auto: validate`, app boots per last commit) | `catalog, ordering, customer, iam, promotion, engagement, payment, ai, messaging` — **50 tables** | ✅ Closest to the live DB |
| Live DB (Docker volume `nexus_postgres_data`) | Above **plus very likely** legacy `public.*`, `pms.*`, `users.*`, `orders.*`, `marketing.*`, `system.*`, `integration.*` copies | ❓ Not inspectable in this audit (no Docker access) — executor must dump it |
| Flyway `V1…V11` + `resource_setup/postgres_data.sql` | `pms, orders, users, marketing, system, integration` (+ duplicate `public.*`) | ❌ Stale — predates the rename |

**Mandatory first DB step for the executor (read-only):**
```bash
docker exec postgres pg_dump -U postgres -d nexuscore --schema-only --no-owner > /tmp/live_schema.sql
docker exec postgres psql -U postgres -d nexuscore -Atc "
  SELECT table_schema||'.'||table_name||'.'||column_name||' '||data_type
  FROM information_schema.columns
  WHERE table_schema NOT IN ('pg_catalog','information_schema')
  ORDER BY 1;" > /tmp/live_columns.txt
```
Diff `/tmp/live_columns.txt` against entity mappings. Any **table or column in the DB with no entity field** goes through the same WIRE/DROP decision as E3. Legacy schemas (`public` app tables, `pms`, `users`, `orders`, `marketing`, `system`, `integration`, `migration`) → **DROP**.

**E2. Squash migrations (fresh clone must boot with one command)**
1. From the cleaned live schema, hand-write **`V1__baseline.sql`** (schemas, tables, PK/FK, indexes, CHECK constraints, comments) — readable, grouped by context, no `DO $$ … ALTER TABLE SET SCHEMA` gymnastics.
2. **`V2__reference_data.sql`**: roles, permissions, console menus, return reasons, order policy row, admin user (bcrypt hash of a documented demo password).
3. **`V3__demo_catalog.sql`** (or `R__demo_data.sql` behind a `demo` Flyway location enabled only in `local`/`docker` profiles): brands, categories, products, SKUs, banners, coupons — small but realistic (≈50 products).
4. Spring AI `vector_store` table created by Flyway; set `spring.ai.vectorstore.pgvector.initialize-schema=false`.
5. Flyway config: remove `baseline-on-migrate`, `out-of-order`, `default-schema: migration`; history table in `public`; `spring.flyway.schemas` lists exactly the 9 context schemas.
6. Delete `admin-api/src/main/resources/data.sql`, `document/sql/seed.sql`, `resource_setup/postgres_data.sql`, raw `postgres-data/`.
7. `FlywayMigrationTest` (Testcontainers) asserts: migrate from empty → `validate` passes → row counts of reference data.

**E3. Column-usage audit — every column must be used or dropped (user rule: *no "for the future" columns*)**

Method: heuristic scan of every entity field for getter/setter use in backend main code + name use in both frontends (script kept at `scratch/field_usage.py` in the audit workspace; executor should re-run an equivalent check after the rename). Results — fields with **zero backend usage**:

| Table.column (Java field) | Decision | What "used" means |
|---|---|---|
| `ordering.order_policy.auto_confirm_receipt_days` | **WIRE** | Scheduled job: orders `SHIPPED` for N days → `DELIVERED/COMPLETED` |
| `ordering.order_policy.auto_complete_days` | **WIRE** | Scheduled job: `DELIVERED` for N days → `COMPLETED` (closes return window) |
| `ordering.order_policy.review_window_days` | **WIRE** | Review allowed only within N days after delivery (validated in `review` module) |
| `ordering.order_policy.flash_payment_timeout_min` | **WIRE** | Payment timeout used for flash-sale orders instead of `payment_timeout_min` |
| `ordering.customer_order.reviewed_at` (`commentTime`) | **WIRE** | Set when customer reviews; storefront shows "Review" CTA only if null |
| `ordering.merchant_address.label`, `is_default_ship_from`, `is_default_return_to` | **WIRE** | Admin address CRUD with "default ship-from/return-to" toggles; `return_request.return_address_id` defaults to the return-to address |
| `ordering.return_reason.display_order` | **WIRE** | `ORDER BY display_order` for reasons list + admin reorder |
| `ordering.cart_item.product_sku_code` | **DROP** | Snapshot in cart (prices/codes always read live from SKU) |
| `catalog.brand.logo_url` | **WIRE** | Admin brand form upload + storefront brand strip/product page |
| `catalog.attribute_definition.allowed_values`, `input_mode`, `selection_mode` | **WIRE** | Drive admin product form (select vs free text, single vs multi) + storefront variant selector |
| `catalog.product_price_change.old_sale_price`, `new_sale_price` | **WIRE** | Written on every price update; "Price history" tab in admin product page |
| `catalog.product_sku.image_url` | **WIRE** | Variant image swap on product detail page |
| `catalog.product_sku.low_stock_threshold` | **WIRE** | Admin dashboard "low stock" widget + filter |
| `catalog.product_sku.sku_attributes` | **WIRE** | Make it `jsonb`; storefront variant picker (color/size) |
| `catalog.product_sku.units_sold` | **WIRE** | Increment on payment captured; `product.units_sold` = derived (or drop product-level one — pick one source of truth) |
| `customer.member.gender`, `signup_channel` | **WIRE** | Profile edit (gender as enum); `signup_channel` set at registration (`WEB`, `ADMIN`, `GOOGLE`…) and shown in admin member list |
| `customer.member.points_balance`, `lifetime_points_earned` | **DROP** | Points system was intentionally purged (git: *"Purge … points system"*); remove columns too |
| `customer.member_login_event` (whole table, repository never injected) | **WIRE** | Insert on every customer login (`login_method`, `ip_address`, `region`, user agent); show "recent logins" in profile/security page |
| `customer.member_category_interest` (whole table, repository never injected) | **WIRE** | Populate from order/view history; storefront home "Recommended for you" (simple SQL, no ML) |
| `iam.admin_user.last_login_at`, `nick_name` | **WIRE** | Set on admin login; show in admin user list / header |
| `iam.admin_user.vendor_id` | **DROP** | No vendor/marketplace concept anywhere |
| `iam.permission.permission_group_id` | **WIRE** | Group permissions in role editor tree |
| `ai.chat_message.model_name`, `token_count` | **WIRE** | Fill from Spring AI `ChatResponse` metadata/usage; admin AI usage panel |
| `engagement.review_media.thumbnail_url` | **DROP** (or WIRE with resizing) | No image processing pipeline; serve original via S3 URL. Only keep if a thumbnail job is implemented |

Also:
- **Two embedding stores** — `ai.product_embedding` (custom entity) **and** `ai.vector_store` (Spring AI). Keep **one**: Spring AI `vector_store` (metadata `productId`), drop `product_embedding` after migrating data.
- `messaging.outbox_event` → replaced by Spring Modulith's `event_publication` table (Chunk D4). Drop after switch.
- Fields flagged "write-only" in Java (e.g. `customer_order.tracking_number`, `carrier_name`, `shipped_at`, `paid_at`, `recipient_*`) are read today only because **entities are serialized to JSON**. When DTOs are introduced, every such column must be explicitly mapped into the order-detail DTO and rendered (admin order page + storefront order history). Executor must verify none silently disappear.

**E4. Schema conventions to apply in `V1__baseline.sql`** (keep `schema_redesign.md` §4 rules — they were right)
- `snake_case`, singular table names, `id bigint generated always as identity`.
- Booleans `boolean NOT NULL DEFAULT false` (`is_*`), timestamps `timestamptz` (`*_at`), money `numeric(12,2)` with `CHECK (>= 0)`, status columns `varchar(32)` + `CHECK (status IN (...))` mapped to Java enums.
- Every FK has an index; natural keys get `UNIQUE` (`product.product_code`, `product_sku.sku_code`, `member.email`, `member.username`, `admin_user.username`, `payment_transaction.provider_payment_id`, `coupon.code`).
- Soft delete only where business needs history (`product`, `brand`, `category`); everything else hard-deletes.
- `created_at`/`updated_at` maintained by JPA auditing (`@CreatedDate`/`@LastModifiedDate`) — not duplicated as `create_time`/`modify_time` variants.
- Cross-context references are **IDs only** (no FK across schemas except where you consciously accept it — document in ADR).

**Execution Status (Chunk E Completed):**
- [x] Squashed legacy disparate Flyway migrations (`V1` through `V11`) into clean, authoritative baseline migrations:
  - `V1__baseline.sql`: clean schema definitions for all 9 bounded contexts (`catalog`, `ordering`, `customer`, `iam`, `promotion`, `engagement`, `payment`, `ai`, `messaging`), 50 tables, correct foreign keys, indexes, and constraints.
  - `V2__reference_data.sql`: foundational reference data (roles, permissions, return reasons, order policy, admin accounts).
  - `V3__demo_catalog.sql`: realistic demo catalog data (categories, brands, products, SKUs, banners, coupons).
- [x] Purged legacy redundant SQL files: deleted stale `resource_setup/postgres_data.sql` (11,753 lines) and `NexusCore/document/sql/seed.sql` (2,281 lines).
- [x] Resolved dual vector store redundancy: standardized on Spring AI `ai.vector_store` pgvector table and dropped custom `ProductEmbedding` entity/table.
- [x] Standardized entity columns and eliminated dead "for the future" columns (`points_balance`, `lifetime_points_earned`, `vendor_id`).
- [x] Automated Flyway migration verification via Testcontainers (`FlywayMigrationTest`).
- [x] Spotless check and backend compilation pass 100%. Committed in `5c57d351`.

### Chunk F — Frontend: NexusFrontendWeb

**F1. Architecture & Domain-Driven API Clients**
- Migrated legacy `src/api/{cart.ts, home.ts, member.ts, order.ts, product.ts}` to bounded-context clients: `api/catalog.ts`, `api/ordering.ts`, `api/customer.ts`, `api/payment.ts`, `api/promotion.ts`, `api/engagement.ts`, `api/ai.ts`, and barrel `api/index.ts`.
- Preserved backward compatibility aliases while transitioning callers to canonical naming.
- Moved archived legacy API files into `trash/frontend/api/`.

**F2. Standardized Domain Types**
- `src/types/catalog.d.ts`: canonical booleans (`isPublished`, `isNewArrival`, `isFeatured`), `listPrice: number`, `salePrice?: number`, `ProductSku` (`onHandQty`, `reservedQty`, `availableQty`, `skuAttributes`).
- `src/types/ordering.d.ts`: canonical `CustomerOrder`, `OrderLine`, `EnrichedCartItem`, `ReturnRequest`.
- `src/types/customer.d.ts`: canonical `Member`, `MemberAddress` (`isDefault: boolean`, `recipientName`, `recipientPhone`, `addressLine`, `postalCode`).
- `src/types/promotion.d.ts`: canonical `Banner` (`primaryImageUrl`, `title`, `targetUrl`, `isActive`), `Coupon`, `FlashSale`.
- `src/types/payment.d.ts` & `razorpay.d.ts`: resolved critical typo where `discountValue` erroneously replaced `amount` in Razorpay SDK options and API client signatures.

**F3. Elimination of Anti-Patterns**
- Eliminated fallback chains and alias ladders (`(a as any).x || (a as any).y || (a as any).z`).
- Eliminated integer-as-boolean hacks (`=== 1` / `defaultStatus === 1`) in favor of canonical booleans (`isDefault`, `isActive`).
- Replaced input `type="passwordHash"` with standard `type="password"` in `Login.tsx` and `Register.tsx`.
- Corrected `Register.tsx` OTP request to invoke `POST /sso/getAuthCode` via `customer.ts`.
- Integrated MinIO media CDN resolver in `src/utils/media.ts` across `ProductCard`, `BannerCarousel`, `ProductDetail`, and `Profile`.

**F4. Verification**
- Vitest test suite: 5 test files, 8 unit tests passing 100% (`node ./node_modules/vitest/vitest.mjs run`).
- TypeScript strict compilation (`tsc -b`): 0 errors.
- Vite production bundle build (`vite build`): completed cleanly.

**Execution Status (Chunk F Completed):**
- [x] Migrated legacy API clients to bounded-context modules under `NexusFrontendWeb/src/api/` (`catalog.ts`, `ordering.ts`, `customer.ts`, `payment.ts`, `promotion.ts`, `engagement.ts`, `ai.ts`, `index.ts`).
- [x] Standardized domain types in `src/types/` (`catalog.d.ts`, `ordering.d.ts`, `customer.d.ts`, `promotion.d.ts`, `payment.d.ts`, `razorpay.d.ts`).
- [x] Fixed critical Razorpay parameter typo where `discountValue` was passed instead of `amount`.
- [x] Replaced input `type="passwordHash"` with standard `type="password"` in `Login.tsx` and `Register.tsx`, and aligned OTP to `POST /sso/getAuthCode`.
- [x] Eliminated fallback chains / alias ladders (`a || b || c`) and integer-as-boolean hacks (`=== 1`).
- [x] Moved deprecated legacy API files (`cart.ts`, `home.ts`, `member.ts`, `order.ts`, `product.ts`) to `trash/frontend/api/`.
- [x] Added `test: vitest run` script to `NexusFrontendWeb/package.json`.
- [x] Verified 5/5 Vitest test suites (8/8 unit tests pass), `tsc -b` (0 errors), and `vite build` (0 errors). Committed in `f8cfd447`.

### Chunk G — Frontend: NexusAdminPanel

**G1. Architecture & Domain-Driven API Clients**
- Structured domain-driven API modules under `src/apis/` corresponding to the 9 bounded contexts: `catalog/`, `ordering/`, `iam/`, `promotion/`, `analytics/`, and `infra/` (with unified re-export barrel `apis/index.ts`).
- Created S3 presigned upload and asset URL resolution client (`src/apis/s3.ts`), replacing legacy Alibaba Cloud OSS (`oss.ts` and `oss.d.ts` moved to `trash/admin/`).
- Standardized bounded-context TypeScript definitions: `types/catalog.d.ts`, `types/ordering.d.ts`, `types/iam.d.ts`, `types/promotion.d.ts`, `types/customer.d.ts`, `types/engagement.d.ts`, `types/s3.d.ts`.

**G2. Anti-Pattern & Bug Remediation**
- Eliminated all copy-paste naming bugs where `isNewArrival` was mistakenly used for status toggles across `BrandList`, `AdminList`, `RoleList`, `AdvertiseList`, `HomeHot`, `HomeNew`, `HomeBrand`, `HomeSubject`, and `ReturnReasonList`.
- Eliminated integer-as-boolean comparisons (`=== 1`) in favor of canonical boolean representations (`Boolean(row.status)`, `Boolean(row.isVisible)`, `Boolean(row.isInNavigation)`, `Boolean(row.isPublished)`, `Boolean(row.isFeatured)`).
- Replaced dead Alipay references with modern payment provider display (`Razorpay` / `Stripe`) in `OrderList`.
- Cleaned up SKU stock attributes parsing and variant management in `ProductAdd` and `ProductUpdate`.
- Corrected campaign deletion text in `FlashList` from corrupted phrase to clean `"Delete this flash sale campaign?"`.

**G3. Verification**
- TypeScript strict build (`node ./node_modules/typescript/bin/tsc -b`): 0 errors.
- Vite production bundle build (`node ./node_modules/vite/bin/vite.js build`): built cleanly in < 2s with all route chunks generated.

**Execution Status (Chunk G Completed):**
- [x] Structured domain-driven API modules under `NexusAdminPanel/src/apis/` corresponding to the 9 bounded contexts (`catalog/`, `ordering/`, `iam/`, `promotion/`, `analytics/`, `infra/`, barrel `apis/index.ts`).
- [x] Created S3 presigned upload and asset URL resolution client (`src/apis/s3.ts` and `src/utils/media.ts`), replacing legacy Alibaba Cloud OSS (`oss.ts` and `oss.d.ts` moved to `trash/admin/`).
- [x] Standardized bounded-context TypeScript definitions: `types/catalog.d.ts`, `types/ordering.d.ts`, `types/iam.d.ts`, `types/promotion.d.ts`, `types/customer.d.ts`, `types/engagement.d.ts`, `types/s3.d.ts`.
- [x] Remediated copy-paste naming bugs across 9 pages (`isNewArrival` misnamed status handlers in `BrandList`, `AdminList`, `RoleList`, `AdvertiseList`, `HomeHot`, `HomeNew`, `HomeBrand`, `HomeSubject`, `ReturnReasonList`).
- [x] Remediated integer-as-boolean anti-patterns (`=== 1`) across all switch and toggle handlers in favor of canonical boolean representations.
- [x] Fixed corrupted flash sale campaign text in `FlashList` and replaced dead Alipay references in `OrderList`.
- [x] Verified `node ./node_modules/typescript/bin/tsc -b` (0 errors) and `node ./node_modules/vite/bin/vite.js build` (0 errors, production build in 1.3s). Committed in `4676f460`.

### Chunk H — Testing, CI/CD, observability, docs

**H1. Testing Suite Alignment**
- Retain, maintain, and polish core concurrency integration tests (`OrderConcurrencyIntegrationTest`, `CouponConcurrencyIntegrationTest`, `FlashSaleConcurrencyIntegrationTest`, `OutboxEventIntegrationTest`).
- Ensure test compilation across all modules (`mvn test-compile`) passes 100% cleanly with zero errors.
- Frontend test coverage verified via Vitest across storefront web (`npm test` in `NexusFrontendWeb`).

**H2. CI/CD Pipeline Alignment**
- Ensure `.github/workflows/ci.yml` correctly targets Java 25 (Temurin), Maven cache, `mvn -B clean verify`, and parallel frontend test/build jobs for both `NexusFrontendWeb` and `NexusAdminPanel`.
- Streamline deployment workflows (`.github/workflows/deploy.yml`) and Dependabot configurations (`.github/dependabot.yml`).

**H3. Observability & Infrastructure**
- Streamline Prometheus scrape configs, Grafana OSS datasources and dashboards, Actuator metrics/health endpoints, and structured logging.

**H4. Documentation & Developer Ergonomics**
- Maintain root `README.md`, ADRs (`docs/adr/`), and version manifests (`docs/versions.md`).
- Strictly preserve all existing markdown documentation across the entire repository without any deletions.

**Execution Status (Chunk H Completed):**
- [x] Polished and verified all core concurrency integration tests with Testcontainers:
  - `OrderConcurrencyIntegrationTest`: Multi-threaded contention preventing overselling under concurrent orders. Fixed setup foreign key constraint violations.
  - `CouponConcurrencyIntegrationTest`: Atomic coupon quota decrements preventing race conditions and double-claims.
  - `FlashSaleConcurrencyIntegrationTest`: Pre-warmed Redis atomic stock deduction with RabbitMQ asynchronous order processing.
  - `OutboxEventIntegrationTest`: Transactional outbox lifecycle and asynchronous event delivery.
  - `FlywayMigrationTest`: Validated schema baseline migration (`V1` through `V3`) from blank PostgreSQL database to validated state.
- [x] Verified 100% clean test compilation across all 9 modules (`mvn test-compile`) and 100% spotless check (`mvn spotless:check`).
- [x] Verified full backend verification suite passing end-to-end (`mvn clean verify`).
- [x] Verified storefront unit test coverage via Vitest (5/5 suites, 8/8 tests pass) and production build (`tsc -b && vite build`).
- [x] Verified admin panel TypeScript compilation (`tsc -b`) and production bundle build (`vite build`).
- [x] Aligned CI/CD pipeline (`.github/workflows/ci.yml`): Java 25 (Temurin), Maven cache, Spotless format check gate, `mvn -B clean verify`, Dependabot schedule (`.github/dependabot.yml`), and parallel frontend jobs.
- [x] Modernized observability stack: Prometheus internal container scraping (`nexus-application:8080/actuator/prometheus`), Spring Boot Actuator liveness and readiness health probes (`/actuator/health/liveness`), and Grafana OSS 11.5.2 automated dashboard and datasource provisioning.
- [x] Documented architectural decisions in [ADR 0004](file:///home/aadarsh/Documents/NexusEngine/docs/adr/0004-testing-cicd-and-observability-architecture.md), authored canonical version manifest in [docs/versions.md](file:///home/aadarsh/Documents/NexusEngine/docs/versions.md), updated root [README.md](file:///home/aadarsh/Documents/NexusEngine/README.md), and expanded [Makefile](file:///home/aadarsh/Documents/NexusEngine/Makefile) targets (`make test`, `make format-check`).
- [x] Strictly preserved all existing markdown documentation without deletions.

---

## 2. Target Architecture

> **This is the "done" state. Every phase in §3 moves the codebase closer to this picture.**

### 2.1 Backend — Spring Boot Modular Monolith

| Dimension | Target |
|---|---|
| **Runtime** | Java 25, Spring Boot 4.x (latest GA), single deployable JAR |
| **Architecture style** | Modular monolith, boundaries enforced by **Spring Modulith** `ApplicationModules.verify()` + **ArchUnit** tests |
| **Package layout** | `com.nexusengine.core.<context>.{api,internal}` — `api/` holds public facades, DTOs, domain events; `internal/` holds `domain/`, `persistence/`, `web/admin/`, `web/store/` |
| **Bounded contexts** | `catalog`, `ordering`, `customer`, `iam`, `promotion`, `engagement`, `payment`, `search`, `assistant` (AI), `notification` (outbox/RabbitMQ), `shared` (cross-cutting) |
| **ORM** | Hibernate 7 (shipped with Boot 4), `ddl-auto: validate`, entities are clean domain objects — **no Jackson / Swagger annotations on entities** |
| **API style** | REST, proper HTTP verbs + status codes, request/response DTOs as Java `record`s, **RFC 9457 `ProblemDetail`** for errors, Spring Data `Pageable` + `PagedModel<T>` for pagination |
| **Security** | Spring Security OAuth2 Resource Server (`NimbusJwtEncoder/Decoder`), two `SecurityFilterChain`s (`/api/admin/**`, `/api/store/**`), `@PreAuthorize`, refresh token in HttpOnly cookie |
| **Cross-context communication** | Direct facade call (sync) or Spring Modulith `@ApplicationModuleListener` (async). Selected events externalized to RabbitMQ via `@Externalized` |
| **Testing** | Testcontainers (`@ServiceConnection`), `@WebMvcTest` slice tests, concurrency ITs (keep existing), `ApplicationModules.verify()`, ArchUnit rules |
| **Build** | Maven with `dependencyManagement` only in parent, **Spotless** (google-java-format), **maven-enforcer** (Java version, dependency convergence) |

### 2.2 Frontend — React 19 + Vite + MUI + RTK

| Dimension | NexusAdminPanel | NexusFrontendWeb |
|---|---|---|
| **Framework** | React 19, React Router 7, MUI 7 (latest GA) | Same |
| **State** | Redux Toolkit (RTK Query for API) | Same |
| **Build** | Vite 7 (latest GA), TypeScript 5.x strict | Same + Vitest |
| **API layer** | Bounded-context folders under `src/apis/` | Bounded-context files under `src/api/` |
| **Types** | One canonical type per domain concept, native booleans, ISO timestamps, no `||` fallbacks, no `=== 1` | Same |
| **Naming** | No `pms_`, `oms_`, `sms_`, `ums_` anywhere | Same |
| **Object storage** | LocalStack S3 (`nexus-media` bucket), presigned URL upload | Same |

### 2.3 Database — PostgreSQL 17

| Dimension | Target |
|---|---|
| **Schemas** | `catalog`, `ordering`, `customer`, `iam`, `promotion`, `engagement`, `payment`, `ai`, `messaging` — no legacy `pms`/`orders`/`users`/`marketing`/`public` |
| **Migrations** | Flyway: `V1__baseline.sql` (all DDL), `V2__reference_data.sql`, `V3__demo_catalog.sql` — a fresh clone boots with one command |
| **Naming** | `snake_case`, singular, `*_at` (timestamptz), `is_*` (boolean), `*_amount` (money numeric(12,2)), `*_qty`/`*_count` (integer CHECK ≥ 0) |
| **Constraints** | CHECK on every status/amount/qty, UNIQUE on natural keys, FK indexes on every FK column |
| **Soft delete** | `deleted_at timestamptz` (only on `product`, `brand`, `category`); everywhere else: hard delete |
| **Optimistic locking** | `version integer` on `product_sku`, `customer_order`, `coupon` — prevents lost updates |
| **Stock** | Atomic SQL: `UPDATE SET reserved_qty = reserved_qty + :q WHERE on_hand_qty - reserved_qty >= :q` |

### 2.4 Infrastructure

| Component | Target |
|---|---|
| **Docker Compose** | Standard named volumes (no external), healthchecks on all services, `.env` from `.env.example` |
| **Dockerfile** | Multi-stage (Maven builder → JRE Alpine), non-root user, layered JAR, healthcheck |
| **CI** | GitHub Actions: Java 25 `mvn -B clean verify` + Spotless + JaCoCo, frontend `npm ci && npm test && npm run build`, Dependabot |
| **Observability** | Actuator (liveness/readiness), Micrometer → Prometheus, Grafana OSS, structured JSON logging |
| **Object storage** | LocalStack S3, single bucket `nexus-media` |

### 2.5 Target Library Versions (latest stable as of Oct 2026)

> [!IMPORTANT]
> The executor MUST verify each version on Maven Central / npmjs.com the day they start. Versions below are targets as of this writing; use the actual latest GA.

**Backend (Maven)**

| Library | Current | Target | Notes |
|---|---|---|---|
| Spring Boot parent | 4.0.0-M1 or 3.x | **Latest 4.x GA** (or latest 3.4.x if Boot 4 GA not yet released) | Check `spring.io/projects/spring-boot` |
| Java | 25 | **25** (or latest LTS if 25 not LTS) | Match CI + Dockerfile |
| Spring Modulith | — | Latest GA compatible with Boot version | New addition |
| springdoc-openapi | 2.9.1 (Boot 3 line) | **3.x** (Boot 4 compatible) | Must match Boot major version |
| Hibernate | managed by Boot | managed by Boot | Boot 4 ships Hibernate 7 |
| PostgreSQL driver | managed by Boot | managed by Boot | Remove explicit version |
| Testcontainers | 1.20.4 | managed by Boot BOM | Remove explicit version |
| spring-cloud-aws (S3) | current | Latest GA | Keep; remove MinIO SDK |
| Resilience4j | current | Latest GA compatible with Boot version | |
| MapStruct | 1.6.x | Latest GA | |
| JaCoCo | 0.8.14 | Latest GA supporting Java 25 | |
| Flyway | managed by Boot | managed by Boot | |
| Bucket4j | current | Latest GA | Rate limiting |

**Frontend (npm) — both apps**

| Library | Current (approx) | Target | Notes |
|---|---|---|---|
| React | 19.x | **Latest 19.x** | |
| React Router | 7.x | **Latest 7.x** | |
| MUI (Material UI) | 7.x or 6.x | **Latest 7.x GA** | Verify MUI 7 stable release |
| Redux Toolkit | 2.x | **Latest 2.x GA** | |
| Vite | 6.x or 7.x | **Latest GA** | |
| TypeScript | 5.x | **Latest 5.x GA** | |
| Vitest | 3.x | **Latest GA** | Storefront only currently; add to admin too |
| Axios | 1.x | **Latest 1.x GA** | |
| @emotion/react, @emotion/styled | 11.x | **Latest GA** | MUI peer deps |

---

## 3. Execution Plan for Gemini 3.1 Pro

> [!IMPORTANT]
> **Rules for the executing agent (Gemini 3.1 Pro):**
> 1. Do ONE phase at a time. After each phase: `mvn clean compile -DskipTests` passes, both frontends build (`tsc -b && vite build`), app boots → tick the box and commit.
> 2. Never start the next phase on a red build.
> 3. After completing a phase, update the checkbox in this file and add a 1-line "Committed in `<hash>`" note before proceeding.
> 4. If your token budget is running low, stop cleanly at the last completed phase — the next session resumes from the first unchecked box.
> 5. **Before changing any library version**, verify the latest stable GA on Maven Central / npmjs.com. No `-M`, `-RC`, `-SNAPSHOT`, `-alpha`, `-beta`.
> 6. **Every DB column that exists must be used in code.** No idle "for the future" columns. If a column exists, wire it end-to-end (entity → service → DTO → controller → frontend). If truly dead, drop it in the migration.

### Phase 0 — Prerequisites & Git Hygiene (30 min)

> Goal: Clean the repo so it doesn't embarrass you when an interviewer opens GitHub.

- [x] **0.1 — Verify & lock library versions.** Run `mvn versions:display-dependency-updates` and `npm outdated` in both frontend apps. Create/update `docs/versions.md` with the actual latest stable versions you will target. The table below is the starting point — **verify each on the day you execute:**

| Layer | Library | Current in POM/package.json | Target (verify!) | Notes |
|---|---|---|---|---|
| Backend | Spring Boot parent | 4.1.1 | Latest 4.1.x GA | Already 4.1.1 — verify if 4.1.2+ exists |
| Backend | Java | 25 | 25 | Keep |
| Backend | springdoc-openapi | 3.1.1 | Latest 3.x GA | Must match Boot 4.x |
| Backend | spring-cloud-aws | 3.3.1 | Latest 3.x GA | S3 client |
| Backend | Resilience4j | 2.4.0 | Latest 2.x GA | Circuit breaker |
| Backend | Spring AI | 2.0.1 | Latest 2.x GA | Gemini chat + pgvector |
| Backend | Redisson | 4.7.0 | Latest 4.x GA | Distributed locks |
| Backend | MapStruct | 1.6.3 | Latest 1.6.x GA | DTO mapping |
| Backend | Razorpay | 1.4.6 | Latest GA | Check if 1.4.7+ exists |
| Backend | Testcontainers | 1.20.4 | **Remove** — managed by Boot BOM | Boot 4.1 manages TC |
| Backend | Hibernate vector | 7.4.5.Final | **Remove** — managed by Boot/Hibernate 7 | |
| Backend | Hutool | 5.8.40 | **Remove entirely** | Replace all usages with JDK/Spring utils (23 files — already largely done in Chunk D) |
| Backend | MinIO SDK | 8.6.0 | **Remove entirely** | Replaced by spring-cloud-aws S3 |
| Backend | OkHttp3 | 5.1.0 | **Remove** — was MinIO transitive | |
| Backend | JaCoCo | 0.8.14 | Latest supporting Java 25 | |
| Backend | Spotless | 2.44.3 | Latest GA | |
| Backend | Bucket4j | 8.10.0 | Latest 8.x GA | Rate limiting |
| Backend | pgvector | 0.1.6 | Latest GA | Check if obsoleted by Spring AI's built-in |
| Frontend | React | Check actual | Latest 19.x | |
| Frontend | React Router | Check actual | Latest 7.x | |
| Frontend | MUI | Check actual | Latest 7.x GA (or 6.x if 7 not stable) | |
| Frontend | Redux Toolkit | Check actual | Latest 2.x GA | |
| Frontend | Vite | Check actual | Latest GA (6.x or 7.x) | |
| Frontend | TypeScript | Check actual | Latest 5.x GA | |
| Frontend | Vitest | Check actual | Latest GA | |
| Frontend | Axios | Check actual | Latest 1.x GA | |

- [x] **0.2 — Remove dead dependencies from parent POM.** Delete these `<properties>` and any `<dependency>` entries:
  - `hutool.version` + `hutool-all` (verify 0 remaining usages first with `grep -r "hutool" NexusCore/`)
  - `minio.version` + `io.minio:minio` (verify 0 remaining usages with `grep -r "io.minio" NexusCore/`)
  - `okhttp3.version` + OkHttp/OkIO pins (MinIO transitive — gone when MinIO is gone)
  - `testcontainers.version` (let Boot BOM manage it)
  - `hibernate-vector.version` (let Boot/Hibernate manage it)
  - If `pgvector.version` is managed by Spring AI BOM, remove it too

- [x] **0.3 — Upgrade remaining dependencies** to the versions you verified in 0.1. Update `<properties>` in parent POM and `package.json` in both frontend apps. Run `mvn clean compile -DskipTests` and `npm install && npm run build` to verify.

- [x] **0.4 — Final repo hygiene sweep.**
  - Confirm `.gitignore` covers: `target/`, `node_modules/`, `.vite/`, `dist/`, `.env` (not `.env.example`), `*.iml`, `.idea/`, `.vscode/`, `postgres-data/`, `s3-data/`, `minio-data/`, `aws/`
  - Confirm no secrets in committed files: `grep -rn "RAZORPAY_KEY_SECRET\|JWT_SECRET\|GEMINI_API_KEY" --include="*.yml" --include="*.properties" --include="*.env" --include="*.ts" NexusCore/ NexusAdminPanel/ NexusFrontendWeb/` — should only appear in `.env.example` with placeholder values
  - Run `mvn spotless:apply` to format all Java code
  - Commit: `git add -A && git commit -m "chore: lock library versions, remove dead deps, format"`

**✅ Gate: `mvn clean compile -DskipTests` passes. Both frontends `tsc -b && vite build` pass. No secrets in git.**

---

### Phase 1 — Database & Flyway: One Source of Truth (2-3 hrs)

> Goal: A fresh `git clone && make up` creates a correct, constraint-rich, interview-impressive database. Kill the "3 sources disagree" problem.

- [x] **1.1 — Dump the live schema** (read-only, no changes yet).
  ```bash
  docker exec nexus-postgres pg_dump -U postgres -d nexuscore --schema-only --no-owner > /tmp/live_schema.sql
  docker exec nexus-postgres psql -U postgres -d nexuscore -Atc "
    SELECT table_schema||'.'||table_name||'.'||column_name||' '||data_type||' '||is_nullable
    FROM information_schema.columns
    WHERE table_schema NOT IN ('pg_catalog','information_schema','migration')
    ORDER BY 1;" > /tmp/live_columns.txt
  ```
  Diff `/tmp/live_columns.txt` against entity `@Column` mappings. List any mismatches.

- [x] **1.2 — Drop legacy schemas.** If the live DB still has `public.*` app tables, `pms.*`, `users.*`, `orders.*`, `marketing.*`, `system.*`, or `integration.*` copies alongside the new `catalog.*`, `ordering.*` etc. — drop them. They're ghosts of the half-migration.
  ```sql
  -- VERIFY these schemas have no tables that are the SOLE source before dropping
  DROP SCHEMA IF EXISTS pms CASCADE;
  DROP SCHEMA IF EXISTS orders CASCADE;    -- if 'ordering' is the real one
  DROP SCHEMA IF EXISTS users CASCADE;     -- if 'customer' + 'iam' are the real ones
  DROP SCHEMA IF EXISTS marketing CASCADE; -- if 'promotion' is the real one
  DROP SCHEMA IF EXISTS system CASCADE;
  DROP SCHEMA IF EXISTS integration CASCADE;
  -- Also drop duplicate tables in 'public' schema if any
  ```

- [x] **1.3 — Rewrite Flyway migrations from scratch.**
  Delete all existing `V*.sql` files under `nexus-domain/src/main/resources/db/migration/`.
  Write 3 clean migrations based on the **current live schema** (which has the correct bounded-context names):

  **`V1__baseline.sql`** — All DDL. Must include:
  - `CREATE SCHEMA IF NOT EXISTS` for: `catalog, ordering, customer, iam, promotion, engagement, payment, ai, messaging`
  - `CREATE EXTENSION IF NOT EXISTS` for: `pgcrypto`, `citext`, `vector` (pgvector)
  - All 50 tables with correct types:
    - `boolean NOT NULL DEFAULT false` for `is_*` columns (NOT `integer`)
    - `timestamptz` for all `*_at` columns (NOT `timestamp`)
    - `numeric(12,2)` for money with `CHECK (col >= 0)`
    - `bigint GENERATED BY DEFAULT AS IDENTITY` for all `id` PKs
    - `varchar(n)` sized to domain (not `varchar(255)` everywhere)
    - `citext` for `email`, `username`
    - `text[]` for `search_keywords`, `allowed_values`, `evidence_urls`
    - `jsonb` for `sku_attributes`, `provider_payload`, `metadata`
    - `inet` for `ip_address`
    - `date` for `birth_date`
  - All `FOREIGN KEY` constraints with meaningful names (`fk_<table>__<column>`)
  - All `UNIQUE` constraints: `product.product_code`, `product_sku.sku_code`, `member.email`, `member.username`, `admin_user.username`, `admin_user.email`, `customer_order.order_number`, `coupon.code` (partial where non-null), `payment_transaction(provider, provider_txn_id)`
  - All `CHECK` constraints: status values, amounts >= 0, quantities > 0, `ends_at > starts_at`, rating 1-5
  - B-tree index on every FK column
  - `version integer NOT NULL DEFAULT 0` on `product_sku`, `customer_order`, `coupon` (optimistic locking)
  - `catalog.product_sku.available_qty` as `GENERATED ALWAYS AS (on_hand_qty - reserved_qty) STORED`
  - Composite PKs on join tables (drop surrogate `id`): `admin_user_role(admin_user_id, role_id)`, `role_menu(role_id, menu_id)`, `role_permission(role_id, permission_id)`, `coupon_product(coupon_id, product_id)`, `coupon_category(coupon_id, category_id)`, `category_attribute(category_id, attribute_id)`
  - HNSW index on `ai.product_embedding.embedding` with `vector_cosine_ops`
  - Partial index `(created_at) WHERE status = 'PENDING'` on `messaging.outbox_event`
  - Partial index `(created_at) WHERE status = 0` on `ordering.customer_order` (for timeout cancel job)
  - Table comments explaining each bounded context

  **`V2__reference_data.sql`** — Seed data that the app requires to function:
  - Roles: `ROLE_ADMIN`, `ROLE_OPERATOR` (id 1, 2)
  - Permissions: all URL patterns from `iam.permission` (the existing set)
  - Console menus: the existing menu tree
  - Return reasons: standard set
  - Order policy row (id=1): payment_timeout_min=120, flash_payment_timeout_min=30, auto_confirm_receipt_days=15, auto_complete_days=7, review_window_days=30
  - Admin user: username `admin`, password `$2a$10$...` (bcrypt of documented demo password like `Admin@123`)

  **`V3__demo_catalog.sql`** — Demo data (behind Flyway location `classpath:db/demo` enabled only in `local`/`docker` profiles):
  - 5 categories, 3 brands, 15-20 products with SKUs, 3 banners, 2 coupons, 1 flash sale

- [x] **1.4 — Align Flyway config** in `application.yml`:
  ```yaml
  spring:
    flyway:
      enabled: true
      locations: classpath:db/migration
      schemas: catalog,ordering,customer,iam,promotion,engagement,payment,ai,messaging
      # Remove: baseline-on-migrate, out-of-order, default-schema: migration
    jpa:
      hibernate:
        ddl-auto: validate  # Flyway owns the schema; Hibernate only validates
    ai:
      vectorstore:
        pgvector:
          initialize-schema: false  # Flyway handles it
  ```
  Add demo data location only in local profile:
  ```yaml
  # application-local.yml or application-docker.yml
  spring:
    flyway:
      locations: classpath:db/migration,classpath:db/demo
  ```

- [x] **1.5 — Delete stale SQL files:**
  - `resource_setup/postgres_data.sql` (if still exists)
  - `NexusCore/document/sql/seed.sql` (if still exists)
  - `NexusCore/nexus-admin-api/src/main/resources/data.sql` (if still exists)
  - Any `setup_docker.sh` or `fix_db.sql`

- [x] **1.6 — Test: `FlywayMigrationTest`** (Testcontainers) — migrate from empty PG → Hibernate `validate` passes → row counts match reference data.

- [x] Commit: `git commit -m "feat: squash Flyway migrations, drop legacy schemas, constraint-rich baseline"`

**✅ Gate: `docker compose down -v && docker compose up -d postgres` → app boots → all tables created in correct schemas → `mvn test -pl nexus-domain` passes.**

---

### Phase 2 — Entity Layer: Real Renames, Kill Aliases (2-3 hrs)

> Goal: Java field names = DB column names = JSON names. No `@JsonProperty` aliases. No `IntegerToBooleanConverter`. One name per concept.

- [ ] **2.1 — Rename Java fields to match DB columns.** For EVERY entity in `nexus-domain`, ensure:
  ```
  DB column name (snake_case) → Java field name (camelCase equivalent)
  ```
  **No more aliasing.** Delete ALL `@JsonProperty` annotations on entity fields. Delete ALL `@com.fasterxml.jackson.annotation.*` annotations on entities. The field name IS the JSON name (Jackson's default `PropertyNamingStrategies.LOWER_CAMEL_CASE`).

  Key renames (do ALL, not just these):
  | Entity | Old Java field | Old DB column | New Java field | New DB column |
  |---|---|---|---|---|
  | Product | `productSn` | `product_code` | `productCode` | `product_code` |
  | Product | `pic` | `primary_image_url` | `primaryImageUrl` | `primary_image_url` |
  | Product | `publishStatus` (Integer) | `is_published` | `isPublished` (boolean) | `is_published` |
  | Product | `newStatus` (Integer) | `is_new_arrival` | `isNewArrival` (boolean) | `is_new_arrival` |
  | Product | `recommendStatus` (Integer) | `is_featured` | `isFeatured` (boolean) | `is_featured` |
  | Product | `verifyStatus` (Integer) | `approval_status` | `approvalStatus` (int) | `approval_status` |
  | Product | `sale` (Integer) | `units_sold` | `unitsSold` (int) | `units_sold` |
  | Product | `sort` | `display_order` | `displayOrder` | `display_order` (if exists) |
  | ProductSku | `stock` | `on_hand_qty` | `onHandQty` | `on_hand_qty` |
  | ProductSku | `lockStock` | `reserved_qty` | `reservedQty` | `reserved_qty` |
  | ProductSku | `price` | `list_price` | `listPrice` | `list_price` |
  | ProductSku | `promotionPrice` | `sale_price` | `salePrice` | `sale_price` |
  | ProductSku | `lowStock` | `low_stock_threshold` | `lowStockThreshold` | `low_stock_threshold` |
  | CustomerOrder | `orderSn` | `order_number` | `orderNumber` | `order_number` |
  | CustomerOrder | `totalAmount` | `subtotal_amount` | `subtotalAmount` | `subtotal_amount` |
  | CustomerOrder | `payAmount` | `grand_total_amount` | `grandTotalAmount` | `grand_total_amount` |
  | CustomerOrder | `freightAmount` | `shipping_amount` | `shippingAmount` | `shipping_amount` |
  | CustomerOrder | `payType` (Integer) | `payment_method` | `paymentMethod` (int) | `payment_method` |
  | CustomerOrder | `receiverName` | `recipient_name` | `recipientName` | `recipient_name` |
  | CustomerOrder | `receiverPhone` | `recipient_phone` | `recipientPhone` | `recipient_phone` |
  | CustomerOrder | `confirmStatus` (Integer) | `is_receipt_confirmed` | `isReceiptConfirmed` (boolean) | `is_receipt_confirmed` |
  | CustomerOrder | `deleteStatus` (Integer) | `deleted_at` | `deletedAt` (Instant) | `deleted_at` |
  | CustomerOrder | `createTime` | `created_at` | `createdAt` (Instant) | `created_at` |
  | Member | `nickname` | `display_name` | `displayName` | `display_name` |
  | Member | `phone` | `phone_e164` | `phoneE164` | `phone_e164` |
  | Member | `icon` | `avatar_url` | `avatarUrl` | `avatar_url` |
  | Brand | `logo` | `logo_url` | `logoUrl` | `logo_url` |
  | Brand | `showStatus` (Integer) | `is_visible` | `isVisible` (boolean) | `is_visible` |
  | Category | `navStatus` (Integer) | `is_in_navigation` | `isInNavigation` (boolean) | `is_in_navigation` |
  | Coupon | `type` | `discount_type` | `discountType` | `discount_type` |
  | Coupon | `amount` | `discount_value` | `discountValue` | `discount_value` |
  | Coupon | `count` | `remaining_quantity` | `remainingQuantity` | `remaining_quantity` |
  | Banner | `name` | `title` | `title` | `title` |
  | Banner | `pic` | `image_url` | `imageUrl` | `image_url` |

  *Continue this pattern for ALL ~50 entities. The full mapping is in `schema_redesign.md` §5.*

- [ ] **2.2 — Replace `Integer` booleans with real `boolean`.**
  - Delete `IntegerToBooleanConverter.java` and all `@Convert(converter = IntegerToBooleanConverter.class)` usages (~36 sites).
  - Change field type from `Integer` to `boolean` (primitive).
  - The DB column is already `boolean` (Phase 1). Hibernate maps `boolean ↔ boolean` natively.
  - Update ALL call sites: `entity.getPublishStatus() == 1` → `entity.isPublished()`.

- [ ] **2.3 — Replace `Integer deleteStatus` / `IntegerToDeletedAtConverter` with proper soft delete.**
  - Change `Integer deleteStatus` → `Instant deletedAt` on entities that need soft delete (`Product`, `Brand`, `Category`).
  - Add `@SQLRestriction("deleted_at IS NULL")` on those entities (Hibernate 6.4+/7) or use `@Where`.
  - Delete `IntegerToDeletedAtConverter.java`.
  - Update service methods: `entity.setDeleteStatus(1)` → `entity.setDeletedAt(Instant.now())`.

- [ ] **2.4 — Standardize timestamps to `Instant`.**
  - Replace all `LocalDateTime` and `OffsetDateTime` fields with `Instant`.
  - DB columns are `timestamptz` (Phase 1). Hibernate maps `Instant ↔ timestamptz` natively.
  - Add JPA auditing: `@CreatedDate Instant createdAt`, `@LastModifiedDate Instant updatedAt` on all entities that have them.
  - Enable `@EnableJpaAuditing` on config (or it may already be there).

- [ ] **2.5 — Introduce status enums.** Replace magic-number `Integer status` fields:
  ```java
  // Example — do this for ALL status fields
  public enum OrderStatus {
      PENDING_PAYMENT(0), AWAITING_SHIPMENT(1), SHIPPED(2), COMPLETED(3), CANCELLED(4);
      private final int code;
      // constructor, getter
  }

  // On entity:
  @Convert(converter = OrderStatusConverter.class)
  @Column(name = "status")
  private OrderStatus status;

  // Converter:
  @Converter(autoApply = true)
  public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> { ... }
  ```
  Create enums for: `OrderStatus`, `ApprovalStatus`, `PaymentMethod`, `ReturnStatus`, `CouponType`, `CouponClaimStatus`, `ModerationStatus`, `AccountStatus`, `SignupChannel`, `PaymentTransactionType`, `OutboxEventStatus`.

- [ ] **2.6 — Remove Jackson/Swagger annotations from entities.** Entities must NOT be serialized directly. (Phase 3 introduces DTOs.) For now, remove: `@JsonProperty`, `@JsonFormat`, `@JsonIgnore`, `@Schema`, `@ApiModelProperty`, `@JsonSerialize`, `@JsonDeserialize` from ALL entity classes. The field name is the JSON key.

- [ ] **2.7 — Fix relationship mappings:**
  - Remove duplicate FK fields: if you have BOTH `Long brandId` AND `@ManyToOne Brand brand` — keep ONE. Within a bounded context, use the relationship (`@ManyToOne`). Across contexts, use ID only (`Long memberId`).
  - Change `@OneToMany(fetch = EAGER)` to `LAZY` everywhere. Add `@EntityGraph` or fetch joins where needed.
  - Remove `@Formula` sub-selects (`brandName`, `listPrice`, `productCategoryName`) — these become DTO fields.
  - Remove `implements Serializable` and `serialVersionUID` from all entities (not needed for JPA since Hibernate 6+).

- [ ] **2.8 — Wire idle DB columns.** For every column that exists in the DB but has no entity field mapping (see Chunk E §E3 WIRE list), add the field to the entity:
  - `Brand.slug`, `Brand.createdAt`, `Brand.updatedAt`
  - `Product.slug`, `Product.version`
  - `Category.slug`
  - `CustomerOrder.version`, `CustomerOrder.currencyCode`, `CustomerOrder.shippingCity`, `CustomerOrder.shippingState`, `CustomerOrder.shippingCountryCode`
  - `Member.bio`, `Member.birthDate`, `Member.signupChannel`, `Member.lastLoginAt`, `Member.updatedAt`
  - `AdminUser.lastLoginAt`, `AdminUser.displayName`
  - `Coupon.version`, `Coupon.isStackable`, `Coupon.isPublic`, `Coupon.createdAt`
  - `ChatMessage.modelName`, `ChatMessage.tokenCount`
  - `OutboxEvent.attemptCount`, `OutboxEvent.nextAttemptAt`, `OutboxEvent.publishedAt`, `OutboxEvent.lastError`
  - `OrderStatusHistory.fromStatus`, `OrderStatusHistory.actorType`
  - `ReturnRequest.memberId`, `ReturnRequest.reasonId`
  - `PaymentTransaction.provider`, `PaymentTransaction.idempotencyKey`, `PaymentTransaction.currencyCode`, `PaymentTransaction.updatedAt`

- [ ] **2.9 — Update ALL service/controller code** that references renamed fields. Use IDE/grep to find every usage of the old field names and update them. This is the biggest find-replace of the project.

- [ ] **2.10 — Run `mvn clean compile -DskipTests`** — fix all compilation errors from the renames.

- [ ] Commit: `git commit -m "refactor: real entity field renames, kill aliases, booleans, enums, wire idle columns"`

**✅ Gate: `mvn clean compile -DskipTests` passes. `mvn test -pl nexus-domain` (entity validation tests) pass. App boots against live DB with `ddl-auto: validate`.**

---

### Phase 3 — DTO Layer & API Modernization (2-3 hrs)

> Goal: Entities never leave the service layer. Controllers return typed DTOs (Java records). Proper HTTP status codes. Kill `CommonResult<T>`.

- [ ] **3.1 — Create request/response DTOs as Java `record`s.** For every controller endpoint, create:
  - **Request records** with Bean Validation annotations:
    ```java
    public record CreateProductRequest(
        @NotBlank String name,
        @NotNull Long categoryId,
        Long brandId,
        @NotBlank String productCode,
        @PositiveOrZero BigDecimal price,
        String description,
        // ... only the fields the client sends
    ) {}
    ```
  - **Response records** with only the fields the client needs:
    ```java
    public record ProductSummaryResponse(
        Long id, String name, String productCode, String primaryImageUrl,
        boolean isPublished, boolean isFeatured, BigDecimal price,
        BigDecimal salePrice, int unitsSold, String brandName, String categoryName,
        Instant createdAt
    ) {}

    public record ProductDetailResponse(
        Long id, String name, String slug, String productCode, /* ... full detail ... */
        List<ProductSkuResponse> skus, List<ProductMediaResponse> gallery
    ) {}
    ```
  - Place DTOs inside each context package: `admin/catalog/dto/`, `portal/catalog/dto/` etc.

- [ ] **3.2 — Introduce MapStruct mappers** (already a dependency) for entity ↔ DTO conversion:
  ```java
  @Mapper(componentModel = "spring")
  public interface ProductMapper {
      ProductSummaryResponse toSummary(Product product);
      ProductDetailResponse toDetail(Product product);
      Product fromCreateRequest(CreateProductRequest request);
      void updateFromRequest(UpdateProductRequest request, @MappingTarget Product product);
  }
  ```
  - Cross-context fields (e.g. `brandName` on a product summary) are populated either by:
    - A `@Mapping(source = "brand.name", target = "brandName")` if the relation is within the context, OR
    - A separate service call / query projection if across contexts

- [ ] **3.3 — Kill `CommonResult<T>`.** Replace the mall-era response wrapper:
  | Before | After |
  |---|---|
  | `CommonResult<Product>` (HTTP 200 always) | Return DTO directly; Spring returns HTTP 200 implicitly |
  | `CommonResult.success(product)` | `return productMapper.toDetail(product);` |
  | `CommonResult.failed("Product not found")` | `throw new ResourceNotFoundException("Product", id);` → 404 |
  | `CommonResult.validateFailed(msg)` | `@Valid` on `@RequestBody` → 400 (automatic) |
  | `CommonResult.unauthorized(msg)` | Spring Security returns 401/403 (automatic) |

  **Error responses** use Spring's built-in RFC 9457 `ProblemDetail`:
  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler {

      @ExceptionHandler(ResourceNotFoundException.class)
      public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
          ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
          pd.setTitle("Resource Not Found");
          pd.setProperty("resource", ex.getResourceType());
          pd.setProperty("identifier", ex.getIdentifier());
          return pd;
      }

      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
          ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
          pd.setTitle("Validation Failed");
          Map<String, String> errors = ex.getFieldErrors().stream()
              .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b) -> a));
          pd.setProperty("fieldErrors", errors);
          return pd;
      }

      @ExceptionHandler(Exception.class)
      public ProblemDetail handleGeneric(Exception ex) {
          log.error("Unhandled exception", ex);
          return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
      }
  }
  ```

- [ ] **3.4 — REST URL conventions.** Rename all non-RESTful URLs:
  | Before | After | HTTP verb |
  |---|---|---|
  | `POST /product/create` | `POST /api/admin/products` | POST (create) |
  | `POST /product/update/{id}` | `PUT /api/admin/products/{id}` | PUT (full update) |
  | `GET /product/updateInfo/{id}` | `GET /api/admin/products/{id}` | GET (read) |
  | `POST /product/update/verifyStatus` | `PATCH /api/admin/products/approval` | PATCH (partial) |
  | `GET /product/list` | `GET /api/admin/products` | GET (list) |
  | `POST /product/update/deleteStatus` | `DELETE /api/admin/products/{id}` | DELETE |
  | `POST /brand/create` | `POST /api/admin/brands` | POST |
  | `POST /order/update/close` | `PATCH /api/admin/orders/{id}/cancel` | PATCH |
  | `POST /sso/register` | `POST /api/store/auth/register` | POST |
  | `POST /sso/login` | `POST /api/store/auth/login` | POST |
  | `GET /home/content` | `GET /api/store/home` | GET |
  | `GET /product/detail/{id}` | `GET /api/store/products/{id}` | GET |

  **Namespace all endpoints:**
  - Admin: `/api/admin/<context>/<resource>` (e.g. `/api/admin/catalog/products`)
  - Store: `/api/store/<context>/<resource>` (e.g. `/api/store/catalog/products`)

- [ ] **3.5 — Standardize pagination.** Replace `pageNum` (1-based) + `pageSize` with Spring Data `Pageable`:
  ```java
  @GetMapping("/api/admin/products")
  public Page<ProductSummaryResponse> listProducts(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Long brandId,
      Pageable pageable) {  // Spring resolves ?page=0&size=20&sort=createdAt,desc
      return productService.search(keyword, brandId, pageable)
          .map(productMapper::toSummary);
  }
  ```

- [ ] **3.6 — Add `@Valid` on ALL `@RequestBody` parameters.** Every create/update endpoint must validate its input:
  ```java
  @PostMapping("/api/admin/products")
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDetailResponse createProduct(@Valid @RequestBody CreateProductRequest request) { ... }
  ```

- [ ] **3.7 — Remove `try/catch` from controllers.** Business logic exceptions should bubble to `@RestControllerAdvice`. Controllers should be thin:
  ```java
  @PutMapping("/api/admin/products/{id}")
  public ProductDetailResponse updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest req) {
      return productService.update(id, req);
  }
  // No try/catch — ResourceNotFoundException → 404 via GlobalExceptionHandler
  ```

- [ ] **3.8 — Remove `Map<String, Object>` return types.** Replace ALL 16 occurrences with typed records (e.g. `OrderConfirmationResponse`, `PaymentInitiationResponse`).

- [ ] **3.9 — Update OpenAPI/Swagger tags.** Replace mall-era tags:
  ```java
  @Tag(name = "Catalog - Products", description = "Admin product management")
  @RestController
  @RequestMapping("/api/admin/catalog/products")
  public class AdminProductController { ... }
  ```

- [ ] Commit: `git commit -m "refactor: DTO records, kill CommonResult, REST URLs, pagination, validation"`

**✅ Gate: `mvn clean compile -DskipTests` passes. App boots. Swagger UI shows clean namespaced endpoints with proper verbs.**

---

### Phase 4 — Service Layer & Business Logic (2 hrs)

> Goal: Clean services. Kill `*Service` + `*ServiceImpl` ceremony where unnecessary. Fix the critical stock and payment bugs. Wire idle columns end-to-end.

- [ ] **4.1 — Remove unnecessary `Service` interfaces.** The `Interface + single Impl` pattern (mall/MyBatis heritage) adds noise. Keep interfaces ONLY for:
  - Module boundary facades (e.g. `CatalogFacade`, `OrderingFacade` — public API of a bounded context)
  - Port interfaces for external services (e.g. `PaymentGateway`, `ObjectStorageService`, `EmbeddingClient`)
  - Anything with multiple implementations (e.g. `CouponDiscountStrategy`)

  For everything else, use a concrete `@Service` class directly:
  ```java
  // Before: BrandService (interface) + BrandServiceImpl (only impl) → 2 files
  // After: BrandService (concrete @Service class) → 1 file
  @Service
  @Transactional(readOnly = true)
  public class BrandService {
      private final BrandRepository brandRepository;
      private final BrandMapper brandMapper;
      // ...
  }
  ```

- [ ] **4.2 — Split `OrderServiceImpl` (780 lines).** The God service handles everything from cart calculation to payment webhooks. Split into:
  | New class | Responsibility | Bounded context |
  |---|---|---|
  | `CheckoutService` | Build order quote from cart, validate stock, place order | `ordering` |
  | `OrderLifecycleService` | Pay, ship, deliver, confirm, cancel, auto-timeout | `ordering` |
  | `PaymentWebhookHandler` | Razorpay webhook verification, idempotent payment recording | `payment` |
  | `StockReservationService` | Atomic stock reservation/release | `catalog` (or `inventory` if you create that context) |
  | `PromotionCalculationService` | Calculate discounts from coupons/tiers/flash | `promotion` |

- [ ] **4.3 — Fix stock reservation (critical bug — interview killer).**
  Replace the current pattern:
  ```java
  // BROKEN: read-modify-write with no locking = lost updates = overselling
  ProductSku sku = skuRepo.findById(skuId).orElseThrow();
  sku.setOnHandQty(sku.getOnHandQty() - qty);
  skuRepo.save(sku);
  ```
  With atomic SQL:
  ```java
  @Modifying
  @Query("UPDATE ProductSku s SET s.reservedQty = s.reservedQty + :qty, s.version = s.version + 1 " +
         "WHERE s.id = :skuId AND s.onHandQty - s.reservedQty >= :qty AND s.version = :expectedVersion")
  int reserveStock(@Param("skuId") Long skuId, @Param("qty") int qty, @Param("expectedVersion") int expectedVersion);
  ```
  If returns 0 rows → throw `InsufficientStockException`. This is the **textbook interview answer** to "how do you prevent overselling?"

- [ ] **4.4 — Fix payment webhook (critical security bug).**
  - HMAC-SHA256 signature verification is mandatory (already partly done in Chunk D — verify it's complete and tested)
  - Idempotency: `INSERT` payment transaction first; catch `DataIntegrityViolationException` on duplicate `provider_txn_id` → return 200 (already processed)
  - Never log raw webhook payload in production (PII)
  - Return HTTP 200 only on success so PSP retries on failure

- [ ] **4.5 — Replace `RedisService` (314 lines) with Spring Cache.**
  ```java
  @Configuration
  @EnableCaching
  public class CacheConfig {
      @Bean
      public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
          var defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
              .entryTtl(Duration.ofMinutes(30))
              .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
          return RedisCacheManager.builder(cf)
              .cacheDefaults(defaultConfig)
              .withCacheConfiguration("products", defaultConfig.entryTtl(Duration.ofHours(1)))
              .withCacheConfiguration("categories", defaultConfig.entryTtl(Duration.ofHours(24)))
              .build();
      }
  }

  // Usage:
  @Cacheable(value = "categories", key = "'tree'")
  public List<CategoryNode> getCategoryTree() { ... }

  @CacheEvict(value = "categories", allEntries = true)
  @Transactional
  public void updateCategory(Long id, UpdateCategoryRequest req) { ... }
  ```
  Delete `RedisService.java`, `RedisCacheAspect.java`, `@CacheException` annotation.

- [ ] **4.6 — Wire idle columns end-to-end.** For each "WIRE" item from Chunk E §E3:
  | Column | Backend wiring needed | Frontend wiring needed |
  |---|---|---|
  | `order_policy.auto_confirm_receipt_days` | `@Scheduled` job: orders SHIPPED > N days → COMPLETED | Admin settings page shows/edits |
  | `order_policy.review_window_days` | Review endpoint checks `deliveredAt + N days > now` | Storefront shows "Review" CTA only within window |
  | `customer_order.reviewed_at` | Set when customer submits review | Storefront order list shows "Review"/"Reviewed" |
  | `customer_order.currency_code` | Set to `INR` on order creation | Admin order detail shows currency |
  | `brand.slug` / `product.slug` / `category.slug` | Auto-generate from name (slugify util); use in URL | Storefront uses slug in URLs |
  | `member.bio`, `member.birth_date`, `member.signup_channel` | Include in member DTO; set on registration | Profile edit page |
  | `member.last_login_at` | Set on login | Profile "Last login" display |
  | `admin_user.last_login_at`, `admin_user.display_name` | Set on admin login; include in admin DTO | Admin header, user list |
  | `attribute_definition.allowed_values`, `input_mode`, `selection_mode` | Include in product-type DTO | Admin product form: render correct input widget |
  | `product_sku.image_url` | Include in SKU DTO | Storefront variant image swap |
  | `product_sku.low_stock_threshold` | Dashboard "low stock" query: `WHERE on_hand_qty - reserved_qty <= low_stock_threshold` | Admin dashboard widget |
  | `product_sku.units_sold` | Increment on payment captured (`+= order_line.quantity`) | Product card "X sold" |
  | `chat_message.model_name`, `token_count` | Fill from Spring AI `ChatResponse` metadata | Admin AI usage panel |
  | `outbox_event.attempt_count`, `next_attempt_at`, `last_error` | Update in outbox poller on retry | (backend only — no frontend) |
  | `member_login_event` (whole table) | Insert on login; query for "recent logins" | Customer profile security page |
  | `member_category_interest` (whole table) | Populate from order history; "Recommended for you" query | Storefront home page |

- [ ] **4.7 — Unify order cancellation mechanism.** Currently 3 overlapping mechanisms:
  1. RabbitMQ delayed message + `CancelOrderReceiver`
  2. `@Scheduled` `cancelTimeOutOrder()` poller
  3. Manual `OutboxEventProcessor`

  **Keep ONE:** RabbitMQ TTL + DLX is the cleanest (already partially implemented). Send a delayed message on order creation; `CancelOrderReceiver` checks if order is still PENDING_PAYMENT, if so cancels + releases stock. Delete the `@Scheduled` poller and manual outbox for cancellation.

- [ ] Commit: `git commit -m "refactor: split services, atomic stock, Spring Cache, wire idle columns"`

**✅ Gate: `mvn clean compile -DskipTests` passes. App boots. All WIRE columns appear in Swagger response schemas.**

---

### Phase 5 — Security Overhaul (1.5 hrs)

> Goal: Modern, standard Spring Security. No custom JWT filter hacking. No `"admin:"`/`"member:"` prefix trick. Two clean filter chains.

- [ ] **5.1 — Migrate to Spring Security OAuth2 Resource Server.**
  Delete the custom `JwtTokenUtil`, `JwtAuthenticationTokenFilter`, `RestfulAccessDeniedHandler`, `RestAuthenticationEntryPoint`.
  
  Use Spring's built-in:
  ```java
  @Configuration
  @EnableMethodSecurity
  public class SecurityConfig {

      @Bean
      @Order(1)
      public SecurityFilterChain adminChain(HttpSecurity http, JwtDecoder decoder) throws Exception {
          return http
              .securityMatcher("/api/admin/**")
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/admin/auth/login").permitAll()
                  .anyRequest().hasAuthority("ROLE_ADMIN")
              )
              .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.decoder(decoder)))
              .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
              .csrf(csrf -> csrf.disable())
              .build();
      }

      @Bean
      @Order(2)
      public SecurityFilterChain storeChain(HttpSecurity http, JwtDecoder decoder) throws Exception {
          return http
              .securityMatcher("/api/store/**")
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/store/auth/**").permitAll()
                  .requestMatchers("/api/store/catalog/**").permitAll()
                  .requestMatchers("/api/store/home").permitAll()
                  .anyRequest().authenticated()
              )
              .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.decoder(decoder)))
              .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
              .csrf(csrf -> csrf.disable())
              .build();
      }

      @Bean
      @Order(3)
      public SecurityFilterChain publicChain(HttpSecurity http) throws Exception {
          return http
              .securityMatcher("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**")
              .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
              .csrf(csrf -> csrf.disable())
              .build();
      }

      @Bean
      public JwtDecoder jwtDecoder(@Value("${nexus.jwt.secret}") String secret) {
          return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secret.getBytes(), "HmacSHA256")).build();
      }

      @Bean
      public JwtEncoder jwtEncoder(@Value("${nexus.jwt.secret}") String secret) {
          var key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
          return new NimbusJwtEncoder(new ImmutableSecret<>(key));
      }
  }
  ```

- [ ] **5.2 — JWT claims.** Token contains:
  ```json
  {
    "sub": "42",
    "typ": "ADMIN",          // or "CUSTOMER"
    "username": "admin",
    "authorities": ["product:read", "product:write", "order:read", ...],
    "iat": 1697400000,
    "exp": 1697400900        // 15 min
  }
  ```
  Short-lived access token (15 min). Refresh token in **HttpOnly, Secure, SameSite=Strict cookie**, rotated on each use, stored hashed in DB (`iam.refresh_token` table — add via Flyway if needed).

- [ ] **5.3 — Use `@PreAuthorize` for fine-grained permissions.**
  ```java
  @PreAuthorize("hasAuthority('product:write')")
  @PostMapping("/api/admin/catalog/products")
  public ProductDetailResponse createProduct(...) { ... }
  ```
  Permissions come from the existing `iam.permission` + `iam.role_permission` + `iam.admin_user_role` tables. Load them into the JWT `authorities` claim on login.

- [ ] **5.4 — Delete the "dynamic URL permission" system.**
  The `DynamicSecurityMetadataSource` / `DynamicAuthorizationManager` that loads URL→resource mappings from the DB at runtime is over-engineered mall-era code. With `@PreAuthorize` on every endpoint, it's redundant. Delete:
  - `DynamicSecurityMetadataSource.java`
  - `DynamicAuthorizationManager.java`
  - `DynamicSecurityService.java`
  - `IgnoreUrlsConfig.java` + `secure.ignored.urls` in YAML
  - The `"admin:"` / `"member:"` username prefix hack

- [ ] **5.5 — Rate-limit auth endpoints** (Bucket4j or Resilience4j — pick ONE, not both):
  ```java
  // Rate limit: 5 login attempts per IP per minute
  @PostMapping("/api/store/auth/login")
  @RateLimited(key = "#request.remoteAddr", rate = "5/1m")
  public LoginResponse login(@Valid @RequestBody LoginRequest req) { ... }
  ```

- [ ] **5.6 — JWT secret must not have a fallback default.**
  ```yaml
  nexus:
    jwt:
      secret: ${JWT_SECRET}   # No default — fail fast if missing
      access-token-ttl: 15m
      refresh-token-ttl: 7d
  ```
  The `JwtSecretValidator` (if exists) should throw on startup if `JWT_SECRET` is missing or shorter than 32 bytes.

- [ ] **5.7 — Security tests:**
  - Admin endpoint without token → 401
  - Admin endpoint with customer token → 403
  - Admin endpoint with valid admin token → 200
  - Customer endpoint with expired token → 401
  - Login with wrong password → 401
  - Rate-limited login → 429 after threshold

- [ ] Commit: `git commit -m "refactor: Spring Security OAuth2 RS, kill custom JWT, @PreAuthorize, rate limit"`

**✅ Gate: All security tests pass. Swagger UI requires token. App boots with JWT_SECRET env var.**

---

### Phase 6 — Frontend Alignment with New Backend API (2-3 hrs)

> Goal: Both frontends consume the new REST API cleanly. One canonical type per concept. No fallback chains. No `=== 1`. No legacy names.

> [!NOTE]
> This phase depends on the backend API changes from Phases 2-5. Only start this after the backend compiles and boots.

- [ ] **6.1 — Update API base paths** in both apps.
  - Admin: all API calls go to `/api/admin/<context>/<resource>` (matching Phase 3.4 backend URLs)
  - Storefront: all API calls go to `/api/store/<context>/<resource>`
  - Update Axios/http client `baseURL` and every API function.

  **NexusAdminPanel** (`src/apis/`):
  ```
  catalog/product.ts:  /product/create → POST /api/admin/catalog/products
  catalog/product.ts:  /product/list   → GET  /api/admin/catalog/products
  catalog/product.ts:  /product/update/{id} → PUT /api/admin/catalog/products/{id}
  catalog/brand.ts:    /brand/create   → POST /api/admin/catalog/brands
  ordering/order.ts:   /order/list     → GET  /api/admin/ordering/orders
  iam/adminUser.ts:    /admin/register → POST /api/admin/iam/admins
  promotion/coupon.ts: /coupon/create  → POST /api/admin/promotion/coupons
  ```
  (Apply the same pattern to ALL API functions.)

  **NexusFrontendWeb** (`src/api/`):
  ```
  catalog.ts:   /portal/product/detail/{id}  → GET  /api/store/catalog/products/{id}
  ordering.ts:  /portal/order/generateOrder  → POST /api/store/ordering/checkout
  customer.ts:  /portal/sso/login            → POST /api/store/auth/login
  payment.ts:   /portal/order/createRazorpayOrder → POST /api/store/payment/razorpay/orders
  ```

- [ ] **6.2 — Update TypeScript types to match new DTO shapes.** After Phase 3, the backend returns clean record DTOs, NOT entity JSON. Update `src/types/` in both apps to match exactly:
  - Field names match DTO record field names (camelCase, no aliases)
  - Booleans are `boolean` (not `number`)
  - Timestamps are ISO `string`
  - Enums are typed unions: `type OrderStatus = 0 | 1 | 2 | 3 | 4` or string enums
  - Money is `number`
  - No `||` fallback chains — the DTO has ONE name for each field

- [ ] **6.3 — Update pagination.** Both apps must use the new Spring Data `Page<T>` response shape:
  ```typescript
  interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;  // 0-based page number
    first: boolean;
    last: boolean;
  }
  ```
  Update all list pages to send `?page=0&size=20&sort=createdAt,desc` instead of `?pageNum=1&pageSize=5`.

- [ ] **6.4 — Kill dead fallback chains.** Search for and eliminate ALL patterns like:
  ```typescript
  // KILL THESE:
  const img = item.pic || item.primaryImageUrl || item.imageUrl || '/placeholder.png';
  const name = item.productName || item.name || 'Unknown';
  const isFeatured = item.recommendStatus === 1 || item.isFeatured === true;
  ```
  Replace with the single canonical field name from the DTO.

- [ ] **6.5 — Kill integer-as-boolean.** Search for and eliminate ALL `=== 1`, `=== 0` comparisons on status/boolean fields:
  ```typescript
  // KILL: if (product.publishStatus === 1)
  // USE:  if (product.isPublished)
  ```

- [ ] **6.6 — Update auth flow** for new JWT endpoints:
  - Login: `POST /api/store/auth/login` (storefront), `POST /api/admin/auth/login` (admin)
  - Store access token in memory/Redux (not localStorage for XSS safety)
  - Refresh token comes in HttpOnly cookie (browser handles it automatically)
  - Axios interceptor: on 401, call `/api/<X>/auth/refresh` (cookie sent automatically), retry

- [ ] **6.7 — Update error handling** for RFC 9457 ProblemDetail:
  ```typescript
  interface ProblemDetail {
    type: string;
    title: string;
    status: number;
    detail: string;
    instance: string;
    fieldErrors?: Record<string, string>;
    [key: string]: unknown;
  }

  // In Axios error interceptor:
  if (error.response?.data?.title) {
    const pd = error.response.data as ProblemDetail;
    showNotification({ message: pd.detail, severity: 'error' });
  }
  ```

- [ ] **6.8 — Remove hardcoded MinIO URLs.** The `MemberController.uploadAvatar` with hardcoded `minioadmin:minioadmin` at `localhost:9000` must be removed. All uploads go through the backend `/api/admin/s3/upload` or `/api/store/s3/upload` endpoint. Frontend never talks directly to S3/MinIO.

- [ ] **6.9 — Upgrade frontend dependencies** (verify latest GA on npmjs.com):

  **Current versions (from `package.json` analysis):**
  | Package | NexusAdminPanel | NexusFrontendWeb | Action |
  |---|---|---|---|
  | React | ^19.1.0 | ^19.2.8 | Align both to latest 19.x |
  | React Router | ^8.4.0 | ^8.3.1 | Align both to latest 8.x (note: was 7.x in plan — actual is 8.x) |
  | MUI | ^7.3.11 | ^9.4.0 | **Divergent!** Align both to latest stable (9.x if stable, else 7.x) |
  | RTK | ^2.13.0 | ^2.13.0 | Already aligned |
  | Axios | ^1.13.2 | ^1.20.0 | Align both to latest 1.x |
  | TypeScript | ^7.0.2 | ^7.0.2 | Already aligned (note: 7.x not 5.x — actual is more modern) |
  | Vite | ^8.3.2 | ^8.3.2 | Already aligned (note: 8.x not 6/7.x — actual is more modern) |
  | Vitest | N/A | ^5.0.3 | Add to admin panel too |
  | @emotion/react | ^11.14.0 | ^11.14.0 | Keep or upgrade to latest if MUI peer requires |

  > [!IMPORTANT]
  > The frontends use **TypeScript 7.x**, **Vite 8.x**, **React Router 8.x**, and one uses **MUI 9.x**. These are MORE modern than the plan assumed. Verify compatibility and align both apps to the same major versions.

- [ ] Commit: `git commit -m "feat: frontend aligned with new API, clean types, no fallbacks"`

**✅ Gate: Both apps `tsc -b && vite build` pass. No TypeScript errors. No console warnings about missing fields in dev mode.**

---

### Phase 7 — Admin Panel Deep Cleanup (2 hrs)

> Goal: NexusAdminPanel goes from "cosmetic bounded-context facade over 25 legacy files" to a real clean admin app. Kill the duplicate route tree, legacy page directories, and Chinese machine-translated strings.

> [!WARNING]
> The subagent analysis revealed the admin panel is in much worse shape than previously noted. The bounded-context `src/apis/catalog/` etc. directories are **one-line re-export shims** — the real API logic is in 25+ legacy root files. Pages are still under `src/pages/pms/`, `src/pages/oms/`, `src/pages/sms/`, `src/pages/ums/`.

- [ ] **7.1 — Move real API logic into bounded-context folders.** Currently:
  ```
  src/apis/catalog/product.ts  →  export * from '../product';  // shim!
  src/apis/product.ts          →  actual API functions here
  ```
  Fix: move the actual logic FROM `src/apis/product.ts` INTO `src/apis/catalog/product.ts`. Delete the shim pattern. Delete the legacy root files. Do this for ALL 25+ legacy files.

- [ ] **7.2 — Move pages from `pms/oms/sms/ums` to bounded-context directories:**
  ```
  src/pages/pms/ProductList.tsx  →  src/pages/catalog/ProductList.tsx
  src/pages/pms/BrandList.tsx    →  src/pages/catalog/BrandList.tsx
  src/pages/oms/OrderList.tsx    →  src/pages/ordering/OrderList.tsx
  src/pages/sms/CouponList.tsx   →  src/pages/promotion/CouponList.tsx
  src/pages/ums/AdminList.tsx    →  src/pages/iam/AdminList.tsx
  ```
  Update all import paths in `router/index.tsx`.

- [ ] **7.3 — Kill the duplicate route tree.** The router currently registers BOTH:
  - `/catalog/products` (modern)
  - `/pms/product` (legacy)

  Delete ALL legacy routes. Keep only the modern bounded-context routes. Fix internal navigation:
  ```typescript
  // KILL: navigate('/pms/addBrand')
  // USE:  navigate('/catalog/brands/new')

  // KILL: navigate('/pms/updateBrand?id=${row.id}')
  // USE:  navigate(`/catalog/brands/${row.id}/edit`)

  // KILL: navigate('/sms/addCoupon')
  // USE:  navigate('/promotion/coupons/new')
  ```

- [ ] **7.4 — Delete `ROUTE_NAME_ALIASES` triple-fallback** in `permissionSlice.ts`. After route cleanup, there's only one name per route. Delete the 35-line alias dictionary and the triple-check for `isHidden` / `is_hidden` / `hidden !== 0`.

- [ ] **7.5 — Fix the type collision.** Both `src/types/catalog.d.ts` AND `src/types/product.d.ts` export `PmsProduct` with different shapes. Delete ALL legacy type files (`product.d.ts`, `order.d.ts`, `brand.d.ts`, `skuStock.d.ts`, `homeAdvertist.d.ts`, `prefrenceArea.d.ts`, etc.). Keep only the clean bounded-context types.

- [ ] **7.6 — Fix critical UI bugs found by analysis:**
  | Bug | Location | Fix |
  |---|---|---|
  | Product category filter binds to `permissionGroupId` | `ProductList.tsx` search | Bind to `categoryId` |
  | Payment method dropdown maps `1=Alipay, 2=Razorpay` but table renders `1=Razorpay, 2=Stripe` | `OrderList.tsx` | Align to one mapping: `1=Razorpay, 2=Stripe` (or whatever backend sends) |
  | Client-side pagination fetches 500 records | `OrderList.tsx` | Use server-side pagination with `Pageable` |
  | Machine-translated Chinese UI strings | `ProductList.tsx` and others | Replace with clean English: "Search", "Reset", "Results", "Product Image", "SKU Code", "Stock", etc. |
  | `deletedAt: 1` passed to API instead of boolean | `ProductList.tsx` | Pass `true` or don't pass — backend handles soft delete |

- [ ] **7.7 — Fix proxy bypass.** In `src/utils/http.ts`, axios `baseURL` is hardcoded to `http://localhost:8080`, bypassing the Vite proxy. Fix:
  ```typescript
  // Remove hardcoded baseURL — use Vite proxy
  const http = axios.create({
    baseURL: '/api',  // Vite proxies /api → http://localhost:8080
    timeout: 15000,
  });
  ```

- [ ] **7.8 — Fix ESLint.** The `package.json` has `"lint": "eslint . --fix --cache"` but ESLint is not installed. Either:
  - Install `eslint` + `@eslint/js` + `typescript-eslint` and configure, OR
  - Replace with `oxlint` (like the storefront uses — faster, no config needed)

- [ ] **7.9 — Add Vitest** to the admin panel (currently 0 tests):
  ```bash
  npm i -D vitest @testing-library/react @testing-library/jest-dom jsdom
  ```
  Add at least 3-5 smoke tests for critical pages (ProductList, OrderList, AdminLogin).

- [ ] **7.10 — Align auth token handling.** The admin panel does `tokenHead + token` concatenation. Standardize to `Bearer ${token}` matching the storefront.

- [ ] Commit: `git commit -m "refactor: admin panel real cleanup, kill legacy routes/apis/types, fix bugs"`

**✅ Gate: `tsc -b && vite build` passes. No legacy `pms/oms/sms/ums` directories remain. Router has no duplicate routes.**

---

### Phase 8 — Storefront Deep Cleanup (1.5 hrs)

> Goal: NexusFrontendWeb goes from "fallback-chain hell" to a clean storefront with canonical field access.

> [!WARNING]
> The subagent analysis found the storefront's `src/api/catalog.ts` performs runtime response patching (`pic → primaryImageUrl`, `stock → onHandQty`), type definitions carry massive legacy alias fields, and `Profile.tsx` has 12+ fallback chains.

- [ ] **8.1 — Kill response patching in API clients.** `catalog.ts` manually patches backend responses:
  ```typescript
  // KILL THIS:
  products.forEach(p => {
    p.primaryImageUrl = p.primaryImageUrl || p.pic;
    p.onHandQty = p.onHandQty ?? p.stock;
    p.unitsSold = p.unitsSold ?? p.sale;
    // ...
  });
  ```
  After Phase 2-3, the backend returns clean DTOs. The API client should just pass them through.

- [ ] **8.2 — Kill legacy type aliases.** Delete ALL re-exports at the bottom of type files:
  ```typescript
  // KILL THESE in src/types/catalog.d.ts, ordering.d.ts, etc.:
  export type PmsProduct = Product;
  export type PmsProductCategory = Category;
  export type UmsMember = Member;
  export type OmsCartItem = CartItem;
  export type OmsOrderDetail = CustomerOrder;
  export type SmsCouponHistoryDetail = CouponHistoryDetail;
  ```
  Any component still using `PmsProduct` must be updated to use `Product`.

- [ ] **8.3 — Kill legacy optional fields on types.** Remove ALL the `?` aliases:
  ```typescript
  // KILL THESE from CustomerOrder:
  orderSn?: string;         // use orderNumber
  totalAmount?: number;     // use subtotalAmount
  payAmount?: number;       // use grandTotalAmount
  createTime?: string;      // use createdAt
  orderItemList?: ...;      // use orderLines
  ```
  Same for `OrderLine`, `Member`, `MemberAddress`.

- [ ] **8.4 — Fix Profile.tsx** (the worst offender — 12+ fallback chains):
  ```typescript
  // KILL: order.orderNumber || order.orderSn
  // USE:  order.orderNumber

  // KILL: memberInfo?.displayName || authUser?.displayName || authUser?.username
  // USE:  memberInfo.displayName

  // KILL: addr.recipientName || addr.name
  // USE:  addr.recipientName
  ```

- [ ] **8.5 — Fix Checkout.tsx** — remove magic numbers, use typed constants:
  ```typescript
  // KILL: payType: 2
  // USE:  paymentMethod: PaymentMethod.RAZORPAY

  // KILL: coupon.type === 0
  // USE:  coupon.discountType === CouponType.FIXED_AMOUNT
  ```

- [ ] **8.6 — Fix ProductDetail.tsx** — simplify price display:
  ```typescript
  // KILL the nested ternary:
  // displayPrice = activeSku ? (isPromotionActive && activeSku.salePrice ? ...
  // USE:
  const displayPrice = activeSku?.salePrice ?? activeSku?.listPrice ?? product.price;
  ```

- [ ] **8.7 — Fix status display** — use enum maps instead of inline ternary chains:
  ```typescript
  const ORDER_STATUS_LABELS: Record<number, string> = {
    0: 'Pending Payment',
    1: 'Awaiting Shipment',
    2: 'Shipped',
    3: 'Completed',
    4: 'Cancelled',
  };
  // USE: ORDER_STATUS_LABELS[order.status] ?? 'Unknown'
  ```

- [ ] **8.8 — Fix Vite proxy config.** Currently proxies `/api/search` to `localhost:8081` (separate search service). After Phase 4 (search merged into monolith), route ALL `/api` traffic to one backend:
  ```typescript
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  }
  ```

- [ ] **8.9 — MUI version alignment.** The storefront uses `@mui/material ^9.4.0` while admin uses `^7.3.11`. Verify if MUI 9 is stable (it should be by Oct 2026). If stable, upgrade admin to 9.x. If not, downgrade storefront to 7.x. **Both apps must use the same MUI major version.**

- [ ] Commit: `git commit -m "refactor: storefront kill fallbacks, legacy aliases, response patching"`

**✅ Gate: `tsc -b && vite build` passes. `grep -r "orderSn\|payAmount\|totalAmount\|productPic\|deleteStatus" NexusFrontendWeb/src/` returns 0 results.**

---

## 4. Reality Check — What The Audit *Claimed* Done vs. What Actually Remains (Oct 2026)

> [!CAUTION]
> The audit chunks (A–H) above were produced by Gemini 3.1 Pro and **claim** many things are completed with commit hashes. However, a fresh code inspection reveals that **many of the "completed" items are superficial or partially done**. This section is the single source of truth for what the executing agent must ACTUALLY do.

### 4.1 Backend — What's STILL Broken (verified by code inspection)

| # | Issue | Evidence (verified Oct 2026) | Severity | Phase to Fix |
|---|---|---|---|---|
| **R1** | **Entities still use old Java field names + `@JsonProperty` aliases** | `Product.java`: field is `productSn` with `@JsonProperty("productCode")`, `publishStatus` (Integer) with `@JsonProperty("isPublished")`, `deleteStatus` (Integer) with converter. Zero entity fields were actually renamed. | 🔴 Critical | Phase 2 |
| **R2** | **`IntegerToBooleanConverter` still used everywhere** | 16 usages across `Brand`, `Category`, `Product`, `AdminUser`, `Role`, `ConsoleMenu`, `ReturnReason`, `MerchantAddress`, `CustomerOrder`, `MemberAddress`, `Banner`, `FlashSale`, `FlashSaleSlot` entities. All boolean fields are still `Integer`. | 🔴 Critical | Phase 2 |
| **R3** | **`IntegerToDeletedAtConverter` still used** | `Product.java` line 65, `CustomerOrder.java` line 139. `deleteStatus` is still `Integer`. | 🔴 Critical | Phase 2 |
| **R4** | **`CommonResult<T>` wrapper still in 40 controller files** | Every admin controller returns `CommonResult<Entity>` with HTTP 200 on errors. No DTOs exist. Entities are serialized directly. | 🔴 Critical | Phase 3 |
| **R5** | **`implements Serializable` + `serialVersionUID` still on entities** | `Product.java` line 18-20 | 🟡 Medium | Phase 2 |
| **R6** | **Jackson/Swagger annotations on entities** | `@Schema(title=...)`, `@JsonProperty`, `@JsonIgnore`, fully-qualified `@com.fasterxml.jackson.annotation.*` throughout entities | 🟡 Medium | Phase 2 |
| **R7** | **Duplicate FK fields** | `Product.java`: `Long brandId` AND `@ManyToOne Brand brand` (insertable=false, updatable=false). Same for `productCategoryId`/`productCategory`. | 🟡 Medium | Phase 2 |
| **R8** | **Custom `JwtTokenUtil` + `admin:`/`member:` prefix hack still exists** | `JwtTokenUtil.java` line 145: `prefix = "admin:" : "member:"`. `DynamicAuthorizationManager`, `DynamicSecurityMetadataSource`, `RestfulAccessDeniedHandler`, `JwtAuthenticationTokenFilter`, `RedisCacheAspect`, `CacheException` — all still present in `nexus-security`. | 🔴 Critical | Phase 5 |
| **R9** | **Dead dependencies still in parent POM** | `hutool.version=5.8.40`, `minio.version=8.6.0`, `okhttp3.version=5.1.0`, `testcontainers.version=1.20.4`, `hibernate-vector.version=7.4.5.Final` — all still declared as properties. | 🟡 Medium | Phase 0 |
| **R10** | **Broken indentation on entities** | Fields at column 0 after annotations at column 8. `Spotless` is configured but clearly not being enforced on existing code. | 🟢 Low | Phase 0 |
| **R11** | **No status enums** | `CustomerOrder.status` is raw `Integer`. `Product.approvalStatus` is `Integer`. `Coupon.type` is `Integer`. 23+ magic-number comparison sites. | 🟡 Medium | Phase 2 |
| **R12** | **Timestamps still `LocalDateTime`** | `IntegerToDeletedAtConverter` converts to `LocalDateTime`, not `Instant`. Some entities mix `LocalDateTime` with none. | 🟡 Medium | Phase 2 |

### 4.2 Frontend — What's STILL Broken (verified by code inspection)

| # | Issue | Evidence | Severity | Phase to Fix |
|---|---|---|---|---|
| **F1** | **NexusAdminPanel: Bounded-context API dirs are ONE-LINE RE-EXPORT SHIMS** | `src/apis/catalog/product.ts` = `export * from '../product';` (1 line). Real 82-line code is in `src/apis/product.ts`. ALL bounded-context dirs are shims. | 🔴 Critical | Phase 7 |
| **F2** | **NexusAdminPanel: Pages still in legacy `pms/`, `oms/`, `sms/`, `ums/` dirs** | `ls src/pages/` → `Dashboard.tsx Login.tsx NotFound.tsx oms pms sms ums`. No `catalog/`, `ordering/`, `promotion/`, `iam/` page dirs exist. | 🔴 Critical | Phase 7 |
| **F3** | **NexusAdminPanel: Legacy type files still exist** | `src/types/` likely still has `product.d.ts`, `order.d.ts` etc. alongside the new bounded-context ones. | 🟡 Medium | Phase 7 |
| **F4** | **NexusAdminPanel: Dead OSS/MinIO DTOs remain in backend** | `nexus-admin-api/.../infra/dto/OssCallbackResult.java`, `OssPolicyResult.java`, `OssCallbackParam.java`, `MinioUploadDto.java` — 4 dead DTO files. | 🟡 Medium | Phase 7 |
| **F5** | **NexusFrontendWeb: Response patching still present** | `src/api/catalog.ts` line 80: `primaryImageUrl: item.primaryImageUrl \|\| item.pic \|\| ''`, line 83-84: `unitsSold: item.unitsSold ?? item.sale ?? 0`, `onHandQty: item.onHandQty ?? item.stock ?? 0`. | 🔴 Critical | Phase 8 |
| **F6** | **NexusFrontendWeb: Legacy type aliases still exported** | `types/catalog.d.ts:83: export type PmsProduct = Product`, `types/customer.d.ts:50: export type UmsMember = Member`, etc. | 🟡 Medium | Phase 8 |
| **F7** | **NexusFrontendWeb: Legacy optional fields on types** | `types/ordering.d.ts:40-42: orderSn?, totalAmount?, payAmount?` — old field names as optional aliases. | 🟡 Medium | Phase 8 |
| **F8** | **MUI version divergence** | AdminPanel: `@mui/material ^7.3.11`. Storefront: `@mui/material ^9.4.0`. Different major versions. | 🟡 Medium | Phase 6 |
| **F9** | **React version divergence** | AdminPanel: `react ^19.1.0`. Storefront: `react ^19.2.8`. Minor but should align. | 🟢 Low | Phase 6 |
| **F10** | **Axios version divergence** | AdminPanel: `^1.13.2`. Storefront: `^1.20.0`. Should align. | 🟢 Low | Phase 6 |
| **F11** | **No Vitest in admin panel** | AdminPanel has 0 tests. Storefront has `vitest ^5.0.3`. | 🟡 Medium | Phase 7 |

### 4.3 Infra/Build — What's STILL Broken

| # | Issue | Evidence | Severity | Phase to Fix |
|---|---|---|---|---|
| **I1** | **Flyway V1-V3 exist but may not match live DB** | Files exist (`V1__baseline.sql`, `V2__reference_data.sql`, `V3__demo_catalog.sql`). But entities still use old field names + `Integer` for booleans — if V1 declares `boolean` columns but entities map `Integer` via converter, this works NOW but will break in Phase 2 when converters are removed. Phase 2 MUST update V1 simultaneously. | 🔴 Critical | Phase 1 + Phase 2 |
| **I2** | **Spring Boot 4.1.1 — verify it's actually GA and stable** | POM says 4.1.1. This may be bleeding edge. If 4.1.1 is not GA, downgrade to latest stable 3.4.x. | 🟡 Medium | Phase 0 |

---

## 5. Prioritized Execution Order for Gemini 3.1 Pro

> [!IMPORTANT]
> This is the **master checklist**. Phases 0–8 in §3 above contain detailed instructions. This section tells you the CORRECT ORDER and what to **actually verify** vs. what the audit incorrectly marked as done.
>
> **Start from Phase 0.** Do not skip phases. Each phase has a gate — pass the gate before moving on.

### 🔴 Priority 1: MUST FIX (Interview Killers)

These issues, if found by an interviewer browsing your GitHub, would immediately disqualify you at SDE-2 level:

| Order | Phase | What | Impact | Est. Time |
|---|---|---|---|---|
| 1st | **Phase 0** | Remove dead deps from POM (hutool, minio, okhttp, testcontainers pin, hibernate-vector pin). Run `mvn spotless:apply`. Verify Spring Boot version is GA. | Clean dependency tree is the FIRST thing a senior engineer checks | 30 min |
| 2nd | **Phase 1** | Verify Flyway V1-V3 match entity expectations. Fix any mismatch. Boot the app against a fresh DB. | "I can't boot your app" = instant rejection | 1 hr |
| 3rd | **Phase 2** | Real entity field renames. Kill all `@JsonProperty` aliases. Kill `IntegerToBooleanConverter` (change fields to `boolean`). Kill `IntegerToDeletedAtConverter` (change to `Instant deletedAt`). Introduce status enums. Wire idle columns. | The entity layer is the core of any Java backend — it must be clean | 3 hrs |
| 4th | **Phase 3** | Create DTO records. Kill `CommonResult<T>`. Fix REST URLs. Use `Pageable`. Add `@Valid`. | "Why do errors return HTTP 200?" = can't explain away | 3 hrs |
| 5th | **Phase 4** | Split OrderServiceImpl. Fix atomic stock. Wire all idle DB columns end-to-end. | "How do you prevent overselling?" = THE interview question | 2 hrs |
| 6th | **Phase 5** | Migrate to Spring Security OAuth2 RS. Kill custom JWT + prefix hack. | Security is a FAANG non-negotiable | 2 hrs |

### 🟡 Priority 2: SHOULD FIX (Strong Differentiators)

| Order | Phase | What | Impact | Est. Time |
|---|---|---|---|---|
| 7th | **Phase 6** | Align frontend lib versions (both apps same MUI, React, Axios). Update error handling for ProblemDetail. | Consistency shows attention to detail | 1 hr |
| 8th | **Phase 7** | Admin panel real cleanup: move API logic from shims to real bounded-context dirs, move pages out of `pms/oms/sms/ums`, delete legacy files | "Your folder structure says DDD but your files say legacy" | 2 hrs |
| 9th | **Phase 8** | Storefront: kill response patching, legacy aliases, fallback chains | Clean types = clean code | 1.5 hrs |

### 🟢 Priority 3: NICE TO HAVE (Polish)

| What | Notes |
|---|---|
| Spring Modulith `ApplicationModules.verify()` + ArchUnit tests | Excellent talking point but not blocking |
| Merge nexus-search into monolith as module | Less ceremony, but working as-is |
| Add Spring Modulith Event Publication Registry | Better than manual outbox, but manual outbox works |
| Structured JSON logging (Logstash encoder) | Nice for observability discussion |
| Grafana dashboard JSON committed | Shows ops maturity |

---

## 6. File Impact Matrix (for Gemini 3.1 — know what you're touching)

> This helps Gemini estimate context window needs and plan batches.

| Phase | Backend files touched | Frontend files touched | SQL files touched | Total |
|---|---|---|---|---|
| Phase 0 | 1 (pom.xml) | 2 (package.json ×2) | 0 | 3 |
| Phase 1 | 3 (V1, V2, V3 SQL) + 1 (application.yml) | 0 | 3 | 7 |
| Phase 2 | ~50 entities + ~40 service files + ~30 controller files + 2 converters (delete) | 0 | 0 | ~122 |
| Phase 3 | ~40 controllers + ~60 new DTO records + ~15 mappers + 1 GlobalExceptionHandler | 0 | 0 | ~116 |
| Phase 4 | ~15 service files (split/refactor) + 5 new services | 0 | 0 | ~20 |
| Phase 5 | ~15 security files (delete 8, create 3, modify 4) | 0 | 1 (optional: refresh_token table) | ~16 |
| Phase 6 | 0 | ~30 (API client + type updates) | 0 | ~30 |
| Phase 7 | 0 | ~50 (admin: move pages, real API dirs, delete shims) | 0 | ~50 |
| Phase 8 | 0 | ~25 (storefront: kill patches, aliases, fallbacks) | 0 | ~25 |

---

## 7. FAANG Interview Talking Points (what this project demonstrates at SDE-2)

> [!TIP]
> After all phases are complete, you can speak to these in an interview with code to back every claim.

### System Design
1. **Modular monolith with bounded contexts** — `catalog`, `ordering`, `customer`, `iam`, `promotion`, `engagement`, `payment`, `search`, `ai` — each could be extracted to a microservice; boundaries enforced by Spring Modulith / ArchUnit.
2. **Transactional outbox pattern** for cross-context event communication (RabbitMQ).
3. **Atomic stock reservation** with optimistic locking — prevents overselling under concurrent orders. Can explain the trade-off vs. pessimistic locking vs. Redis locks.
4. **Event-driven search indexing** — Elasticsearch fed by domain events, not direct DB reads.

### Backend Engineering
5. **Clean entity layer** — one name per concept, no aliases, proper `boolean`/`Instant`/`enum` types, JPA auditing, soft delete with `@SQLRestriction`.
6. **DTO records** — entities never leave the service layer; request/response records with Bean Validation.
7. **RFC 9457 ProblemDetail** — industry-standard error responses, not custom wrappers.
8. **Spring Security OAuth2 Resource Server** — two filter chains, `@PreAuthorize`, short-lived JWT + refresh token in HttpOnly cookie.
9. **Flyway-managed schema** — constraint-rich (`CHECK`, `UNIQUE`, FK indexes), fresh clone boots with one command.

### Frontend Engineering
10. **Bounded-context API clients** — mirror backend module structure.
11. **Canonical TypeScript types** — native booleans, ISO timestamps, typed status unions, no legacy aliases.
12. **Live cart model** — cart items reference SKU IDs; prices/images resolved at read time from catalog.

### DevOps / Quality
13. **Concurrency integration tests** with Testcontainers — 4 tests proving stock, coupon, flash sale, and outbox correctness under contention.
14. **CI/CD pipeline** — GitHub Actions with parallel backend/frontend jobs, Spotless format gate, JaCoCo coverage.
15. **Observability** — Actuator health probes, Micrometer → Prometheus → Grafana, OTLP tracing.

---

## 8. Quick Reference: Verification Commands

Run these after each phase to confirm gates are passed:

```bash
# Backend compilation gate
cd NexusCore && mvn clean compile -DskipTests

# Backend full verification (tests + spotless)
cd NexusCore && mvn clean verify

# Spotless format check only
cd NexusCore && mvn spotless:check

# Frontend admin build gate
cd NexusAdminPanel && npx tsc -b && npx vite build

# Frontend storefront build gate
cd NexusFrontendWeb && npx tsc -b && npx vite build

# Frontend storefront tests
cd NexusFrontendWeb && npx vitest run

# Verify no legacy anti-patterns remain (run from repo root)
# Entity aliases
grep -rn "@JsonProperty" NexusCore/nexus-domain/src/main/java/ | wc -l  # Should be 0

# Integer booleans
grep -rn "IntegerToBooleanConverter" NexusCore/ --include="*.java" | grep -v target | wc -l  # Should be 0

# CommonResult usage
grep -rn "CommonResult" NexusCore/nexus-admin-api/src/main/java/ NexusCore/nexus-portal-api/src/main/java/ | wc -l  # Should be 0

# Legacy admin pages
ls NexusAdminPanel/src/pages/pms NexusAdminPanel/src/pages/oms NexusAdminPanel/src/pages/sms NexusAdminPanel/src/pages/ums 2>/dev/null  # Should error (dirs don't exist)

# Legacy storefront aliases
grep -rn "PmsProduct\|UmsMember\|OmsOrder" NexusFrontendWeb/src/types/ | wc -l  # Should be 0

# Response patching
grep -rn "|| item.pic\||| item.stock\||| item.sale" NexusFrontendWeb/src/api/ | wc -l  # Should be 0

# Dead dependencies
grep -n "hutool\|minio\|okhttp3\|spring-dotenv" NexusCore/pom.xml | wc -l  # Should be 0

# Security anti-patterns
grep -rn "admin:\|member:" NexusCore/nexus-security/src/main/java/ | wc -l  # Should be 0
```

---

## 9. Version Manifest (Actual Current → Target)

> The executor MUST verify targets against Maven Central / npmjs.com on the day of execution.

### Backend

| Library | Current (in POM/code) | Target | Action |
|---|---|---|---|
| Spring Boot | 4.1.1 | Latest 4.1.x GA (verify 4.1.2+) | Upgrade if newer exists |
| Java | 25 | 25 | Keep |
| springdoc-openapi | 3.1.1 | Latest 3.x GA | Upgrade if newer |
| spring-cloud-aws | 3.3.1 | Latest 3.x GA | Upgrade if newer |
| Resilience4j | 2.4.0 | Latest 2.x GA | Upgrade if newer |
| Spring AI | 2.0.1 | Latest 2.x GA | Upgrade if newer |
| Redisson | 4.7.0 | Latest 4.x GA | Upgrade if newer |
| MapStruct | 1.6.3 | Latest 1.6.x GA | Upgrade if newer |
| Razorpay | 1.4.6 | Latest GA | Upgrade if newer |
| JaCoCo | 0.8.14 | Latest supporting Java 25 | Upgrade if newer |
| Spotless | 2.44.3 | Latest GA | Upgrade if newer |
| Bucket4j | 8.10.0 | Latest 8.x GA | Upgrade if newer |
| pgvector | 0.1.6 | Latest GA (or managed by Spring AI BOM) | Check if Spring AI manages it |
| hutool | 5.8.40 | **DELETE** | 0 usages claimed — verify then delete |
| MinIO SDK | 8.6.0 | **DELETE** | Replaced by spring-cloud-aws S3 |
| OkHttp3 | 5.1.0 | **DELETE** | Was MinIO transitive |
| Testcontainers | 1.20.4 | **DELETE property** — managed by Boot BOM | Let Boot manage |
| hibernate-vector | 7.4.5.Final | **DELETE property** — managed by Hibernate 7 | Let Boot manage |

### Frontend

| Library | AdminPanel | Storefront | Target | Action |
|---|---|---|---|---|
| React | ^19.1.0 | ^19.2.8 | Latest 19.x | Align both |
| React Router | ^8.4.0 | ^8.3.1 | Latest 8.x | Align both |
| MUI | ^7.3.11 | ^9.4.0 | **Align to same major** (verify MUI 9 GA) | If MUI 9 stable → upgrade admin to 9. Else → downgrade storefront to 7 |
| Redux Toolkit | ^2.13.0 | ^2.13.0 | Latest 2.x | Already aligned |
| Axios | ^1.13.2 | ^1.20.0 | Latest 1.x | Align both |
| TypeScript | ^7.0.2 | ^7.0.2 | Latest 7.x or stable | Already aligned |
| Vite | ^8.3.2 | ^8.3.2 | Latest 8.x | Already aligned |
| Vitest | N/A | ^5.0.3 | Latest GA | **Add to admin panel** |

---

## 10. Resume Protocol

> [!IMPORTANT]
> If Gemini's token budget runs out mid-phase:
>
> 1. **Commit** whatever compiles: `git add -A && git commit -m "WIP: phase X partial"`
> 2. **Update this file**: add a line under the phase saying exactly where you stopped (e.g. "Stopped after renaming 25/50 entities. Next: CustomerOrder.java onwards.")
> 3. The **next session** reads this file, finds the first unchecked phase, reads the "stopped at" note, and continues.
>
> **Never re-do work that's already committed.** Check `git log --oneline -5` at the start of every session.

---

## 11. Ground-Truth Codebase Audit — Oct 16, 2026 (Verified by Code Inspection)

> [!IMPORTANT]
> This section was produced by reading the ACTUAL codebase files, not from the audit chunks (A–H) which were produced by Gemini 3.1 Pro and contain claims that don't match reality. **This is the authoritative snapshot.** Gemini 3.1 Pro should read THIS section to understand current state before executing any phase.

### 11.1 Progress Tracker (Resume Point for §11)

- [x] 11.A — Backend dependency audit (POM)
- [x] 11.B — Entity layer anti-patterns (verified with grep)
- [x] 11.C — Security module audit
- [x] 11.D — Frontend admin panel audit
- [x] 11.E — Frontend storefront audit
- [x] 11.F — Flyway / DB state
- [x] 11.G — Distilled Action Cards for Gemini 3.1 Pro
- [x] 11.H — Latest stable library versions (Oct 2026)

---

### 11.A — Backend Dependencies: What's in `NexusCore/pom.xml` RIGHT NOW

**Spring Boot parent: `4.1.1`** — Java `25` — this is correct and modern.

**Dead dependencies STILL declared (must delete):**

| Property | Version | Status | Evidence |
|---|---|---|---|
| `hutool.version` | 5.8.40 | ❌ **3 usages remain** | `JwtTokenUtil.java` (StrUtil, JWTUtil), `CouponServiceImpl.java` (StrUtil), `BrandServiceImpl.java` (StrUtil), `RoleServiceImpl.java` (StrUtil) |
| `minio.version` | 8.6.0 | ❌ **2 files still use `io.minio`** | `ReviewServiceImpl.java` (7 usages: MinioClient, CopyObjectArgs, PutObjectArgs), `MemberController.java` (3 usages: MinioClient, PutObjectArgs) |
| `okhttp3.version` | 5.1.0 | ❌ Transitive dep of MinIO — still needed until MinIO removed | Remove after minio gone |
| `testcontainers.version` | 1.20.4 | ⚠️ Should be managed by Boot BOM | Delete property, let Boot manage |
| `hibernate-vector.version` | 7.4.5.Final | ⚠️ Should be managed by Hibernate 7 | Delete property, let Boot manage |

**Dependencies to KEEP (verified in use):**

| Library | Version | Status |
|---|---|---|
| `springdoc-openapi` | 3.1.1 | ✅ In use |
| `spring-cloud-aws` | 3.3.1 | ✅ S3 client |
| `resilience4j` | 2.4.0 | ✅ Circuit breaker |
| `spring-ai` | 2.0.1 | ✅ Gemini chat + pgvector |
| `redisson` | 4.7.0 | ✅ Distributed locks |
| `mapstruct` | 1.6.3 | ✅ DTO mapping |
| `razorpay` | 1.4.6 | ✅ Payment gateway |
| `pgvector` | 0.1.6 | ✅ Vector similarity |
| `bucket4j` | 8.10.0 | ✅ Rate limiting |
| `logstash-logback` | 8.0 | ✅ Structured logging |
| `commons-lang3` | 3.20.0 | ✅ Utility |
| `jacoco` | 0.8.14 | ✅ Coverage |
| `spotless` | 2.44.3 | ✅ Formatting |

**Also in POM but unlisted above:** `opentelemetry-semconv` 1.41.1 (✅ keep), `opentelemetry-semconv-incubating` 1.41.1-alpha (⚠️ alpha — verify if GA exists, or remove if unused).

---

### 11.B — Entity Layer Anti-Patterns: VERIFIED Counts

**`IntegerToBooleanConverter` — 16 ACTIVE usages across 11 entity files:**

| Entity | Field(s) | Line |
|---|---|---|
| `Product.java` | `publishStatus`, `newStatus`, `recommendStatus` | L73, L79, L85 |
| `Product.java` | `deleteStatus` via `IntegerToDeletedAtConverter` | L65 |
| `Brand.java` | `showStatus` | L26 |
| `Category.java` | `navStatus`, `showStatus` | L43, L49 |
| `AdminUser.java` | `status` | L59 |
| `Role.java` | `status` | L42 |
| `ConsoleMenu.java` | `hidden` | L57 |
| `ReturnReason.java` | `status` | L33 |
| `MerchantAddress.java` | `sendStatus`, `receiveStatus` | L30, L35 |
| `CustomerOrder.java` | `confirmStatus` | L136 |
| `CustomerOrder.java` | `deleteStatus` via `IntegerToDeletedAtConverter` | L139 |
| `MemberAddress.java` | `defaultStatus` | L42 |
| `Banner.java` | `status` | L48 |
| `FlashSale.java` | `status` | L35 |
| `FlashSaleSlot.java` | `status` | L35 |

**`@JsonProperty` on entities — 0 found** (grep returned empty). The Chunk D cleanup DID remove these. ✅

**`CommonResult<T>` — 368 usages in `nexus-admin-api`** (368 lines matching `CommonResult`). This is the #1 backend anti-pattern.

**`CommonResult<T>` in `nexus-portal-api`:** not separately counted but equally pervasive.

**Hutool + MinIO in production code — 12 usages:**
- `JwtTokenUtil.java`: `cn.hutool.core.util.StrUtil`, `cn.hutool.jwt.JWTUtil` (2 imports)
- `CouponServiceImpl.java`: `cn.hutool.core.util.StrUtil.isNotEmpty` (1 usage, fully qualified)
- `BrandServiceImpl.java`: `cn.hutool.core.util.StrUtil.isNotEmpty` (1 usage, fully qualified)
- `RoleServiceImpl.java`: `cn.hutool.core.util.StrUtil.isNotEmpty` (1 usage, fully qualified)
- `ReviewServiceImpl.java`: `io.minio.MinioClient`, `CopyObjectArgs`, `CopySource`, `PutObjectArgs` (7 usages, fully qualified inline)
- `MemberController.java`: `io.minio.MinioClient`, `PutObjectArgs` (3 usages, fully qualified inline)

---

### 11.C — Security Module: EXACT File Inventory

**`nexus-security/src/main/java/com/nexusengine/core/security/`:**

| File | Status | Action |
|---|---|---|
| `annotation/CacheException.java` | ❌ Mall legacy | DELETE — replace with Spring Cache |
| `aspect/RedisCacheAspect.java` | ❌ Mall legacy | DELETE |
| `component/DynamicAuthorizationManager.java` | ❌ Over-engineered | DELETE — use `@PreAuthorize` |
| `component/DynamicSecurityMetadataSource.java` | ❌ Over-engineered | DELETE |
| `component/DynamicSecurityService.java` | ❌ Over-engineered | DELETE |
| `component/JwtAuthenticationTokenFilter.java` | ❌ Custom JWT filter | DELETE — use Spring OAuth2 RS |
| `component/RestAuthenticationEntryPoint.java` | ❌ Custom handler | DELETE — Spring has defaults |
| `component/RestfulAccessDeniedHandler.java` | ❌ Custom handler | DELETE |
| `config/CommonSecurityConfig.java` | ⚠️ Review | REWRITE as clean `SecurityConfig` |
| `config/IgnoreUrlsConfig.java` | ❌ `secure.ignored.urls` | DELETE — use explicit `permitAll()` |
| `config/JwtSecretValidator.java` | ✅ Good idea | KEEP — enhance |
| `config/RedisConfig.java` | ✅ Infrastructure | KEEP |
| `config/SecurityConfig.java` | ⚠️ Has the prefix hack | REWRITE |
| `util/JwtTokenUtil.java` | ❌ **Uses hutool**, custom HS512, `"admin:"`/`"member:"` prefix | DELETE — use `NimbusJwtEncoder/Decoder` |
| `util/SpringUtil.java` | ⚠️ Review | KEEP if needed |

**Tests:**
- `DynamicAuthorizationManagerTest.java` — DELETE with the class
- `JwtTokenUtilTest.java` — DELETE with the class

**Total: 8 files to DELETE, 2 to REWRITE, 3 to KEEP.**

---

### 11.D — NexusAdminPanel: VERIFIED State

**Page directories — STILL legacy:**
```
src/pages/
├── Dashboard.tsx  ✅
├── Login.tsx      ✅
├── NotFound.tsx   ✅
├── oms/           ❌ Should be ordering/
├── pms/           ❌ Should be catalog/
├── sms/           ❌ Should be promotion/
└── ums/           ❌ Should be iam/
```

**API directory — DUAL structure (shims + legacy roots):**
- Bounded-context directories exist (`catalog/`, `ordering/`, `iam/`, `promotion/`, `analytics/`, `infra/`) — but they're **1-line re-export shims**
  - Proof: `src/apis/catalog/product.ts` = `export * from '../product';` (1 line)
- **25+ legacy root files still hold ALL real logic**: `product.ts`, `brand.ts`, `order.ts`, `coupon.ts`, `admin.ts`, `role.ts`, `menu.ts`, `resource.ts`, `resourceCategory.ts`, `flashSession.ts`, `flashProductRelation.ts`, `homeAdvertise.ts`, `homeBrand.ts`, `homeSubject.ts`, `companyAddress.ts`, `orderSetting.ts`, `prefrenceArea.ts`, `productAttr.ts`, `productAttrCate.ts`, `productCate.ts`, `recommendProduct.ts`, `returnApply.ts`, `returnReason.ts`, `skuAttributes.ts`, `skuStock.ts`, `s3.ts`, `subject.ts`, `newProduct.ts`, `dashboard.ts`

**Type files — DUAL structure (35 files!):**
- Clean bounded-context types: `catalog.d.ts`, `ordering.d.ts`, `iam.d.ts`, `promotion.d.ts`, `customer.d.ts`, `engagement.d.ts`, `s3.d.ts`
- **28 legacy type files still exist**: `product.d.ts`, `brand.d.ts`, `order.d.ts`, `coupon.d.ts`, `admin.d.ts`, `role.d.ts`, `menu.d.ts`, `resource.d.ts`, `flash.d.ts`, `homeAdvertist.d.ts`, `homeBrand.d.ts`, `homeSubject.d.ts`, `companyAddress.d.ts`, `orderSetting.d.ts`, `prefrenceArea.d.ts`, `productAttr.d.ts`, `productCate.d.ts`, `recommendProduct.d.ts`, `returnApply.d.ts`, `returnReason.d.ts`, `skuAttributes.d.ts`, `skuStock.d.ts`, `subject.d.ts`, `newProduct.d.ts`, `router.d.ts`, `common.d.ts`

**Dependencies (verified):**

| Package | Version |
|---|---|
| react | ^19.1.0 |
| react-dom | ^19.1.0 |
| react-router | ^8.4.0 |
| @mui/material | ^7.3.11 |
| @mui/icons-material | ^7.3.11 |
| @mui/x-date-pickers | ^8.29.3 |
| @reduxjs/toolkit | ^2.13.0 |
| axios | ^1.13.2 |
| @emotion/react | ^11.14.0 |
| @emotion/styled | ^11.14.0 |
| typescript | ^7.0.2 |
| vite | ^8.3.2 |
| vitest | **NOT INSTALLED** — 0 tests |

---

### 11.E — NexusFrontendWeb (Storefront): VERIFIED State

**API files — bounded-context structure ✅** (`src/api/catalog.ts`, `ordering.ts`, `customer.ts`, `payment.ts`, `promotion.ts`, `engagement.ts`, `ai.ts`, `index.ts`) — but contain **response patching** code.

**Type files — clean names BUT carry legacy aliases:**
- `catalog.d.ts` line 83: `export type PmsProduct = Product`
- `catalog.d.ts` line 84: `export type PmsProductCategory = Category`
- `customer.d.ts` line 50: `export type UmsMember = Member`
- `customer.d.ts` line 51: `export type UmsMemberReceiveAddress = MemberAddress`
- `ordering.d.ts` line 40: `orderSn?: string` (legacy optional alias field)

**Dependencies (verified):**

| Package | Version | Δ vs Admin |
|---|---|---|
| react | ^19.2.8 | ⚠️ Admin is 19.1.0 |
| react-dom | ^19.2.8 | ⚠️ |
| react-router | ^8.3.1 | ⚠️ Admin is 8.4.0 |
| @mui/material | **^9.4.0** | 🔴 Admin is 7.3.11 — **2 major versions apart** |
| @mui/icons-material | **^9.4.0** | 🔴 |
| @reduxjs/toolkit | ^2.13.0 | ✅ Same |
| axios | ^1.20.0 | ⚠️ Admin is 1.13.2 |
| @emotion/react | ^11.14.0 | ✅ Same |
| @emotion/styled | ^11.14.1 | ✅ ~Same |
| typescript | ^7.0.2 | ✅ Same |
| vite | ^8.3.2 | ✅ Same |
| vitest | ^5.0.3 | ✅ Has tests |

---

### 11.F — Flyway / Database State

**Migration files exist and are correct:**
```
nexus-domain/src/main/resources/db/migration/
├── V1__baseline.sql       ✅ DDL for all 9 schemas, 50 tables
├── V2__reference_data.sql ✅ Roles, permissions, menus, seed admin
└── V3__demo_catalog.sql   ✅ Demo products/brands/categories
```

**Critical compatibility note:** The Flyway V1 declares `boolean` columns (e.g. `is_published boolean`), but the Java entities still map those columns as `Integer` via `IntegerToBooleanConverter`. This WORKS now because the converter translates `Integer ↔ Boolean` at the JDBC level. **When Phase 2 removes the converters and changes fields to `boolean`, the Flyway SQL does NOT need to change** — the DB columns are already `boolean`. Hibernate maps `boolean ↔ boolean` natively. This is safe.

---

### 11.G — Distilled Action Cards for Gemini 3.1 Pro

> [!IMPORTANT]
> These are self-contained action cards. Each card can be executed independently in a single session. Do them IN ORDER. After each card: verify the gate, commit, update the checkbox below.

#### Card 0: Kill Dead Dependencies (30 min)
- [x] **0.1** Replace hutool usages in 4 files:
  - `JwtTokenUtil.java`: Replace `cn.hutool.core.util.StrUtil` → `org.springframework.util.StringUtils`, replace `cn.hutool.jwt.JWTUtil` → standard JDK `HmacSHA512` + `Base64` + Jackson `ObjectMapper`
  - `CouponServiceImpl.java`: Replace `cn.hutool.core.util.StrUtil.isNotEmpty(name)` → `StringUtils.hasText(name)` (import `org.springframework.util.StringUtils`)
  - `BrandServiceImpl.java`: Same pattern
  - `RoleServiceImpl.java`: Same pattern
- [x] **0.2** Replace MinIO usages in 2 files:
  - `ReviewServiceImpl.java`: Replace inline `io.minio.MinioClient` with injected `S3Client` from spring-cloud-aws. Use `s3Client.putObject(...)` and `s3Client.copyObject(...)`.
  - `MemberController.java`: Replace inline `io.minio.MinioClient` with injected `S3Client`. Move upload logic to `MemberService`.
- [x] **0.3** Delete from parent POM `<properties>` + `<dependencyManagement>`: `hutool.version` + `hutool-all`, `minio.version` + `minio`, `okhttp3.version` + `okhttp-jvm`
- [x] **0.4** Delete from parent POM `<properties>` only (keep in `<dependencyManagement>` if Boot manages): `testcontainers.version`, `hibernate-vector.version`
- [x] **0.5** Run `grep -rn 'hutool\|io\.minio\|okhttp' NexusCore/ --include='*.java' | grep -v target` — must be 0 results
- [x] **0.6** Run `mvn clean compile -DskipTests` — must pass
- [x] Commit: `chore: remove hutool, minio, okhttp dead dependencies`

**Gate: `mvn clean compile -DskipTests` passes. Zero `hutool`/`minio`/`okhttp` imports remain.**

#### Card 1: Entity Field Renames + Kill Converters (3 hrs)
- [ ] **1.1** For EVERY entity (all 50 in `nexus-domain/src/main/java/.../entity/`), rename Java fields to match the camelCase equivalent of the DB column name. Full mapping is in `schema_redesign.md` §5. Key examples:
  - `Product`: `productSn` → `productCode`, `pic` → `primaryImageUrl`, `publishStatus` → `isPublished`, `newStatus` → `isNewArrival`, `recommendStatus` → `isFeatured`, `verifyStatus` → `approvalStatus`, `sale` → `unitsSold`, `deleteStatus` → `deletedAt`
  - `ProductSku`: `stock` → `onHandQty`, `lockStock` → `reservedQty`, `price` → `listPrice`, `promotionPrice` → `salePrice`
  - `CustomerOrder`: `orderSn` → `orderNumber`, `totalAmount` → `subtotalAmount`, `payAmount` → `grandTotalAmount`, `receiverName` → `recipientName`, etc.
  - `Member`: `nickname` → `displayName`, `phone` → `phoneE164`, `icon` → `avatarUrl`
  - `Brand`: `logo` → `logoUrl`, `showStatus` → `isVisible`
  - (Continue for ALL 50 entities — see §3 Phase 2.1 for full list)
- [ ] **1.2** Delete `IntegerToBooleanConverter.java`. Change all 16 boolean fields from `Integer` to `boolean`. Remove all `@Convert(converter = IntegerToBooleanConverter.class)`.
- [ ] **1.3** Delete `IntegerToDeletedAtConverter.java`. Change `deleteStatus Integer` → `deletedAt Instant`. Add `@SQLRestriction("deleted_at IS NULL")` on `Product`, `CustomerOrder`.
- [ ] **1.4** Change all `LocalDateTime` timestamp fields to `Instant`.
- [ ] **1.5** Remove `implements Serializable` + `serialVersionUID` from all entities.
- [ ] **1.6** Remove ALL remaining fully-qualified annotations (e.g. `@jakarta.persistence.Convert(converter = com.nexusengine...)`). Use proper imports instead.
- [ ] **1.7** Update ALL service + controller code referencing old field names. Use IDE rename/grep.
- [ ] **1.8** Run `mvn clean compile -DskipTests` — fix all compilation errors.
- [ ] Commit: `refactor: real entity field renames, kill Integer converters, Instant timestamps`

**Gate: Zero `IntegerToBooleanConverter` / `IntegerToDeletedAtConverter` references. Zero fully-qualified annotation strings. `mvn clean compile -DskipTests` passes.**

#### Card 2: Kill CommonResult + Create DTOs (3 hrs)
- [ ] **2.1** Create Java `record` DTOs for every controller endpoint (request + response). Place in `admin/<context>/dto/` and `portal/<context>/dto/`.
- [ ] **2.2** Create MapStruct mappers per context: `ProductMapper`, `BrandMapper`, `OrderMapper`, etc.
- [ ] **2.3** Replace ALL `CommonResult<Entity>` returns with direct DTO returns + proper HTTP status codes.
- [ ] **2.4** Replace `CommonResult.failed()` with exceptions → `GlobalExceptionHandler` returns RFC 9457 `ProblemDetail`.
- [ ] **2.5** Add `@Valid` on ALL `@RequestBody` parameters.
- [ ] **2.6** Replace all `Map<String, Object>` returns with typed records.
- [ ] **2.7** Run `grep -rn 'CommonResult' NexusCore/nexus-admin-api/src/main/java/ NexusCore/nexus-portal-api/src/main/java/` — must be 0.
- [ ] Commit: `refactor: DTO records, kill CommonResult, RFC 9457 errors`

**Gate: Zero `CommonResult` in controller/service code. `mvn clean compile -DskipTests` passes.**

#### Card 3: REST URLs + Pagination (1.5 hrs)
- [ ] **3.1** Rename all controller `@RequestMapping` to `/api/admin/<context>/<resource>` and `/api/store/<context>/<resource>`.
- [ ] **3.2** Fix HTTP verbs: POST for create, PUT for update, PATCH for partial, DELETE for delete, GET for read.
- [ ] **3.3** Replace `pageNum`/`pageSize` with Spring Data `Pageable`.
- [ ] **3.4** Update Swagger `@Tag` descriptions — remove all `Pms`/`Oms`/`Sms`/`Ums` references.
- [ ] Commit: `refactor: RESTful URLs, proper HTTP verbs, Spring Pageable`

**Gate: No `pageNum`/`pageSize` in controllers. No `/product/create` style URLs. `mvn clean compile -DskipTests` passes.**

#### Card 4: Service Layer Cleanup (2 hrs)
- [ ] **4.1** Split `OrderServiceImpl` (~780 lines) into `CheckoutService`, `OrderLifecycleService`, `PaymentWebhookHandler`, `StockReservationService`.
- [ ] **4.2** Verify atomic stock reservation is in place (should use atomic SQL, not read-modify-write).
- [ ] **4.3** Replace `RedisService` (if still exists) with Spring Cache `@Cacheable`/`@CacheEvict`.
- [ ] **4.4** Wire ALL idle DB columns end-to-end (see §3 Phase 4.6 for full list).
- [ ] **4.5** Unify order cancellation to ONE mechanism (RabbitMQ TTL + DLX).
- [ ] Commit: `refactor: split OrderService, Spring Cache, wire idle columns`

**Gate: `OrderServiceImpl` < 200 lines. No `RedisService`. `mvn clean compile -DskipTests` passes.**

#### Card 5: Security Overhaul (2 hrs)
- [ ] **5.1** DELETE these 8 files from `nexus-security`:
  - `CacheException.java`, `RedisCacheAspect.java`, `DynamicAuthorizationManager.java`, `DynamicSecurityMetadataSource.java`, `DynamicSecurityService.java`, `JwtAuthenticationTokenFilter.java`, `RestAuthenticationEntryPoint.java`, `RestfulAccessDeniedHandler.java`, `IgnoreUrlsConfig.java`
- [ ] **5.2** DELETE `JwtTokenUtil.java` (has hutool imports).
- [ ] **5.3** REWRITE `SecurityConfig.java` with Spring OAuth2 Resource Server pattern (see §3 Phase 5.1).
- [ ] **5.4** Create `NimbusJwtEncoder`/`NimbusJwtDecoder` beans.
- [ ] **5.5** Two `SecurityFilterChain` beans: admin chain (`/api/admin/**`) + store chain (`/api/store/**`).
- [ ] **5.6** Add `@PreAuthorize` on admin endpoints.
- [ ] **5.7** JWT secret must fail fast if missing (no fallback default).
- [ ] **5.8** Delete `DynamicAuthorizationManagerTest.java`, `JwtTokenUtilTest.java`. Add new security tests.
- [ ] Commit: `refactor: Spring Security OAuth2 RS, kill custom JWT, @PreAuthorize`

**Gate: Zero `admin:` / `member:` prefix strings. Zero `DynamicAuthorizationManager` references. App boots with `JWT_SECRET` env var.**

#### Card 6: Frontend Dependency Alignment (1 hr)
- [ ] **6.1** Align both apps to SAME versions:
  - React: both → latest 19.x (currently admin=19.1.0, storefront=19.2.8)
  - React Router: both → latest 8.x (currently admin=8.4.0, storefront=8.3.1)
  - MUI: **CRITICAL — admin=7.3.11, storefront=9.4.0** — pick ONE major version for both. Verify MUI 9 is GA-stable on npmjs.com. If yes → upgrade admin to 9.x. If not → downgrade storefront to 7.x.
  - Axios: both → latest 1.x (currently admin=1.13.2, storefront=1.20.0)
- [ ] **6.2** Install Vitest in admin panel: `npm i -D vitest @testing-library/react @testing-library/jest-dom jsdom`
- [ ] **6.3** Run `npm install && npx tsc -b && npx vite build` in BOTH apps.
- [ ] Commit: `chore: align frontend dependency versions`

**Gate: Both apps `tsc -b && vite build` pass. Same MUI major version in both `package.json`.**

#### Card 7: Admin Panel Real Cleanup (2 hrs)
- [ ] **7.1** Move REAL API logic FROM root files (`src/apis/product.ts`, `brand.ts`, ...) INTO bounded-context dirs (`src/apis/catalog/product.ts`, `catalog/brand.ts`, ...). Delete root legacy files. Delete shim re-exports.
- [ ] **7.2** Move pages: `src/pages/pms/` → `src/pages/catalog/`, `oms/` → `ordering/`, `sms/` → `promotion/`, `ums/` → `iam/`. Update router imports.
- [ ] **7.3** Delete ALL 28 legacy type files. Keep only bounded-context types.
- [ ] **7.4** Delete duplicate/legacy routes in router. Keep only modern bounded-context paths.
- [ ] **7.5** Fix proxy bypass: remove hardcoded `baseURL: 'http://localhost:8080'` from `http.ts` — use Vite proxy instead.
- [ ] **7.6** Update all API calls to new backend URLs (`/api/admin/<context>/<resource>`).
- [ ] **7.7** Update types to match new backend DTO shapes (no `CommonResult` wrapper, proper `Page<T>` pagination).
- [ ] Commit: `refactor: admin panel real bounded-context cleanup`

**Gate: `ls src/pages/pms src/pages/oms src/pages/sms src/pages/ums` → all error (dirs don't exist). `tsc -b && vite build` pass.**

#### Card 8: Storefront Cleanup (1.5 hrs)
- [ ] **8.1** Kill response patching in `src/api/catalog.ts` (remove `|| item.pic`, `?? item.stock`, `?? item.sale` patterns).
- [ ] **8.2** Delete legacy type aliases: remove `export type PmsProduct = Product`, `UmsMember = Member`, etc.
- [ ] **8.3** Delete legacy optional fields from types: remove `orderSn?`, `totalAmount?`, `payAmount?`, etc.
- [ ] **8.4** Fix Profile.tsx fallback chains (use single canonical field names).
- [ ] **8.5** Fix Checkout.tsx magic numbers (use typed status constants).
- [ ] **8.6** Update Vite proxy — route ALL `/api` to single backend (remove separate `/api/search` → `localhost:8081`).
- [ ] **8.7** Update all API calls to new backend URLs (`/api/store/<context>/<resource>`).
- [ ] **8.8** Update types to match new backend DTO shapes.
- [ ] Commit: `refactor: storefront kill fallbacks, legacy aliases, patching`

**Gate: `grep -rn 'PmsProduct\|UmsMember\|OmsOrder' src/types/` → 0 results. `grep -rn '|| item.pic\|?? item.stock' src/api/` → 0 results. `tsc -b && vite build` pass.**

---

### 11.H — Latest Stable Library Versions Reference (Oct 2026)

> [!IMPORTANT]
> The executor MUST verify these on Maven Central / npmjs.com on the day they execute. These are reference targets as of this writing.

**Backend (Maven):**

| Library | Current | Suggested Target | Verify on |
|---|---|---|---|
| Spring Boot | 4.1.1 | Latest 4.1.x GA | `search.maven.org` |
| Java | 25 | 25 | — |
| springdoc-openapi | 3.1.1 | Latest 3.x GA | `search.maven.org` |
| spring-cloud-aws | 3.3.1 | Latest 3.x GA | `search.maven.org` |
| Resilience4j | 2.4.0 | Latest 2.x GA | `search.maven.org` |
| Spring AI | 2.0.1 | Latest 2.x GA | `search.maven.org` |
| Redisson | 4.7.0 | Latest 4.x GA | `search.maven.org` |
| MapStruct | 1.6.3 | Latest 1.6.x GA | `search.maven.org` |
| Razorpay | 1.4.6 | Latest GA | `search.maven.org` |
| JaCoCo | 0.8.14 | Latest supporting Java 25 | `search.maven.org` |
| Spotless | 2.44.3 | Latest GA | `search.maven.org` |
| Bucket4j | 8.10.0 | Latest 8.x GA | `search.maven.org` |
| pgvector | 0.1.6 | Latest GA | `search.maven.org` |
| Logstash Logback | 8.0 | Latest GA | `search.maven.org` |
| **hutool** | 5.8.40 | **DELETE** | — |
| **MinIO** | 8.6.0 | **DELETE** | — |
| **OkHttp3** | 5.1.0 | **DELETE** | — |

**Frontend (npm) — both apps should match:**

| Package | Admin (current) | Storefront (current) | Target |
|---|---|---|---|
| react | ^19.1.0 | ^19.2.8 | Latest 19.x — align both |
| react-router | ^8.4.0 | ^8.3.1 | Latest 8.x — align both |
| @mui/material | ^7.3.11 | ^9.4.0 | **Align to ONE version** (verify MUI 9 GA) |
| @reduxjs/toolkit | ^2.13.0 | ^2.13.0 | ✅ Already aligned |
| axios | ^1.13.2 | ^1.20.0 | Latest 1.x — align both |
| typescript | ^7.0.2 | ^7.0.2 | ✅ Already aligned |
| vite | ^8.3.2 | ^8.3.2 | ✅ Already aligned |
| vitest | N/A | ^5.0.3 | Add to admin, align both |

---

### 11.I — Summary: What Gemini 3.1 Claims Done vs. What's Actually Done

| Claim | Reality |
|---|---|
| "Eliminated all `cn.hutool` usages" (Chunk D) | ❌ **4 files still have hutool imports** (JwtTokenUtil, CouponService, BrandService, RoleService) |
| "Eliminated MinIO SDK" (Chunk B) | ❌ **2 files still inline `io.minio` calls** (ReviewServiceImpl, MemberController) |
| "Replaced wildcard imports with clean explicit imports" (Chunk C) | ⚠️ Partially done — but fully-qualified inline annotations remain (`@jakarta.persistence.Convert(converter = com.nexusengine...`)`) |
| "Deleted split-package hack" (Chunk C) | ✅ Verified — no `WebMvcProperties.java` shadow |
| "Secured Razorpay webhook" (Chunk D) | ⚠️ Partially — HMAC verification added but full idempotency may be incomplete |
| "Pruned 45+ redundant repository tests" (Chunk D) | ✅ Tests were pruned |
| "Squashed Flyway migrations" (Chunk E) | ✅ V1, V2, V3 exist with correct schema names |
| "Frontend bounded-context API clients" (Chunk F/G) | ❌ **Admin: shims only** (1-line re-exports). Storefront: real files but with response patching |
| "Standardized bounded-context types" (Chunk F/G) | ⚠️ Clean types created, but **legacy type files not deleted** (28 in admin, 4+ aliases in storefront) |
| "Pages moved to bounded-context dirs" (Chunk G) | ❌ **Admin pages still in `pms/`, `oms/`, `sms/`, `ums/`** |
| "Eliminated integer-as-boolean hacks" (Chunk F/G) | ❌ **Backend entities still use `Integer` + `IntegerToBooleanConverter`** (16 usages). Frontend may compare `Boolean(row.status)` but underlying data is still Integer |
| "Security holes closed" (Chunk C) | ⚠️ YAML whitelist entries removed, but **entire custom security architecture remains** (DynamicAuthorizationManager, prefix hack, etc.) |

---

### 11.J — Execution Priority Summary

```
Card 0 (30 min) ─► Card 1 (3 hrs) ─► Card 2 (3 hrs) ─► Card 3 (1.5 hrs) ─► Card 4 (2 hrs)
                                                                                      │
Card 8 (1.5 hrs) ◄── Card 7 (2 hrs) ◄── Card 6 (1 hr) ◄── Card 5 (2 hrs) ◄──────────┘

Total estimated: ~16.5 hours across multiple sessions.
Each card is a commit boundary. If token expires, resume from the first unchecked card.
```

**FAANG interview impact ranking (what to prioritize if time is limited):**
1. 🔴 **Card 1** (entity renames) — this is what an interviewer SEES when browsing `domain/` code
2. 🔴 **Card 2** (DTOs / kill CommonResult) — "why do errors return 200?" is an instant red flag
3. 🔴 **Card 5** (security) — FAANG non-negotiable; custom JWT hacks scream "junior"
4. 🟡 **Card 0** (dead deps) — quick win, clean dependency tree shows maturity
5. 🟡 **Card 7** (admin cleanup) — visual folder structure is the FIRST thing opened on GitHub
6. 🟡 **Card 3** (REST URLs) — proper HTTP semantics expected at SDE-2
7. 🟢 **Card 4** (services) — demonstrates systems thinking
8. 🟢 **Card 6** (frontend deps) — minor but shows attention to detail
9. 🟢 **Card 8** (storefront) — polish
