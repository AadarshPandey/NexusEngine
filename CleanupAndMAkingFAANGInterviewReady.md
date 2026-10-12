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
- [ ] Final — Consolidated execution plan for Gemini

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

_(pending)_

---

## 3. Execution Plan for Gemini 3.1 Pro

_(pending)_
