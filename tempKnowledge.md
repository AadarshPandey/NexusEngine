# Phase 5 — Spring Boot 4 Migration: Research & Execution Knowledge Base

> **Created**: 2026-09-26  
> **Purpose**: Persist all research gathered for Phase 5 of ModernizationPlan.md so work can resume across sessions.  
> **Status**: Research COMPLETE. Execution NOT STARTED.

---

## 1. Project Structure

```
NexusEngine/
├── NexusCore/                          # Java backend (target of this migration)
│   ├── pom.xml                         # Root POM (multi-module reactor)
│   ├── nexus-common/                   # Shared utilities, Redis config, API classes
│   ├── nexus-data-jpa/                 # JPA entities, repositories, Flyway migrations
│   ├── nexus-security/                 # Spring Security, JWT, dynamic authorization
│   ├── nexus-admin/                    # Admin management API (107 source files)
│   ├── nexus-portal/                   # Customer-facing store API (108 source files)
│   ├── nexus-search/                   # Elasticsearch product search
│   ├── nexus-ai/                       # Spring AI shared library (ChatClient, VectorStore)
│   └── nexus-application/              # Runnable monolith entry point
├── NexusFrontendWeb/
├── NexusAdminPanel/
└── ModernizationPlan.md
```

---

## 2. Current POM State (PARTIALLY MIGRATED — BROKEN)

The root `NexusCore/pom.xml` has been **partially updated** for Phase 5 but is **inconsistent**:

### What's ALREADY updated in root POM properties:
| Property | Value |
|---|---|
| `spring-boot-starter-parent` (parent) | `4.1.1` |
| `java.version` | `25` |
| `spring-ai.version` | `2.0.1` |
| `testcontainers.version` | `2.0.5` (property only!) |
| `redisson.version` | `4.7.0` (property only!) |
| `hibernate-vector.version` | `7.4.10` (property only!) |
| `spring-data-commons.version` | `4.0.9` |
| `resilience4j.version` | `2.4.0` |

### What's STILL BROKEN — hardcoded OLD versions:

| File | Line | What | Old Version | Should Be |
|---|---|---|---|---|
| **Root pom.xml** L133 | Testcontainers BOM in `<dependencyManagement>` | `1.19.7` | `${testcontainers.version}` (2.0.5) |
| **Root pom.xml** L247 | Redisson in `<dependencyManagement>` | `3.33.0` | `${redisson.version}` (4.7.0) |
| **Root pom.xml** L240 | Resilience4j artifact ID | `resilience4j-spring-boot3` | `resilience4j-spring-boot3` (check if Boot 4 needs new artifact) |
| **nexus-data-jpa/pom.xml** L44 | `hibernate-vector` | `6.6.49.Final` | `${hibernate-vector.version}` (7.4.10) |
| **nexus-portal/pom.xml** L53 | `bucket4j-core` | `8.10.0` | Remove `<version>` (inherit `8.14.0` from root) |
| **nexus-portal/pom.xml** L65 | `spring-ai-starter-model-vertex-ai-gemini` | `1.0.0` | Remove `<version>` (inherit from BOM) |
| **nexus-portal/pom.xml** L69 | `spring-ai-starter-vector-store-pgvector` | `1.0.0` | Remove `<version>` (inherit from BOM) |
| **nexus-portal/pom.xml** L86,92,98 | Testcontainers `junit-jupiter`, `postgresql`, `rabbitmq` | `1.19.7` | Remove `<version>` (inherit from BOM) |
| **nexus-portal/pom.xml** L111 | `minio` | `8.6.0` | Remove `<version>` (inherit from root) |
| **nexus-ai/pom.xml** L37 | `spring-ai-starter-model-vertex-ai-gemini` | `1.0.0` | Remove `<version>` |
| **nexus-ai/pom.xml** L40 | `spring-ai-starter-vector-store-pgvector` | `1.0.0` | Remove `<version>` |

---

## 3. Spring Security 7 Migration (CRITICAL)

### Files using DEPRECATED `ConfigAttribute` / `SecurityConfig` (REMOVED in Security 7):

1. **`nexus-security/.../DynamicSecurityService.java`** (interface)
   - `import org.springframework.security.access.ConfigAttribute;`
   - Returns `Map<String, ConfigAttribute>`
   - **FIX**: Replace with `Map<String, String>` (just store the authority string)

2. **`nexus-security/.../DynamicSecurityMetadataSource.java`**
   - Implements `FilterInvocationSecurityMetadataSource` (REMOVED in Security 7)
   - Uses `ConfigAttribute`, `FilterInvocation`
   - **FIX**: Remove `FilterInvocationSecurityMetadataSource`. Convert to a plain Spring bean that stores `Map<String, String>` and provides path-matching lookup.

3. **`nexus-security/.../DynamicAuthorizationManager.java`**
   - Uses `ConfigAttribute` for authority matching
   - Already implements `AuthorizationManager<RequestAuthorizationContext>` (good — this is the Security 7 approach)
   - **FIX**: Change to use `String` instead of `ConfigAttribute`

4. **`nexus-admin/.../AdminNexusSecurityConfig.java`** L42
   - `new org.springframework.security.access.SecurityConfig(...)` — REMOVED class
   - **FIX**: Just return the authority string directly

5. **`nexus-security/test/.../DynamicAuthorizationManagerTest.java`**
   - `import org.springframework.security.access.SecurityConfig;`
   - **FIX**: Update test

### Files using `@Autowired` field injection (should be constructor injection):
- `SecurityConfig.java` — 5 `@Autowired` fields (L30-39)
- `UnifiedSecurityConfig.java` — 2 `@Autowired` fields (L16-20)
- `PortalNexusSecurityConfig.java` — 1 `@Autowired` field (L16)
- `AdminNexusSecurityConfig.java` — 2 `@Autowired` fields (L24-27)
- `JwtAuthenticationTokenFilter.java` — 2 `@Autowired` fields (L27-30)
- `DynamicAuthorizationManager.java` — 2 `@Autowired` fields (L28-31)
- `DynamicSecurityMetadataSource.java` — 1 `@Autowired` field (L21)
- `RateLimitInterceptor.java` — 2 `@Autowired` fields
- `OutboxEventProcessor.java` — 1 `@Autowired` field (L23, FQN inline)
- `AbstractNexusChatService.java` — 2 `@Autowired` fields (L25-29)

---

## 4. Spring AI 2.x Migration

### Current AI Dependencies (per module):
Both `nexus-ai/pom.xml` and `nexus-portal/pom.xml` declare:
- `spring-ai-starter-model-openai` (no version — inherits from BOM)
- `spring-ai-starter-model-vertex-ai-gemini` (hardcoded `1.0.0`)
- `spring-ai-starter-vector-store-pgvector` (hardcoded `1.0.0`)

### Migration Plan per ModernizationPlan.md:
> Swap `spring-ai-starter-model-openai` for `spring-ai-starter-model-google-genai` (native Gemini support without OpenAI compatibility layer!)

### AI Source Files (all in nexus-ai):
- `AiAutoConfiguration.java` — empty marker config (OK)
- `NexusChatService.java` — interface: `ask()`, `askWithContext()` (OK)
- `AbstractNexusChatService.java` — uses `ChatClient`, `ChatMemory`, `VectorStore`, `SearchRequest`, `MessageChatMemoryAdvisor` (needs review for AI 2.x API changes)
- `JpaChatMemory.java` — implements `ChatMemory`, uses `MessageType`, `UserMessage`, `AssistantMessage`, `SystemMessage` (needs review)
- `ChatRequest.java`, `ChatResponse.java` — DTOs

### YAML AI Config (application.yml + profiles):
```yaml
# application.yml L31-36
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:demo-key}
      embedding:
        options:
          model: text-embedding-ada-002

# application-dev.yml L50-56
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:sk-your-key-here}
      chat:
        options:
          model: gpt-4o-mini
          temperature: 0.7
```

**FIX**: Replace `spring.ai.openai` with `spring.ai.google-genai` (or appropriate Boot 4 / Spring AI 2.x property namespace for native Gemini).

---

## 5. Hibernate 7 Migration

### hibernate-vector version:
- `nexus-data-jpa/pom.xml` L44: hardcoded `6.6.49.Final` → should use `${hibernate-vector.version}` = `7.4.10`

### `save()` calls (Spring Data JPA — NOT Hibernate Session):
All 40+ `.save()` calls are through **Spring Data JPA `JpaRepository.save()`** — these are **NOT affected** by Hibernate 7 removing `Session.save()`. Spring Data's `save()` internally uses `persist()`/`merge()` and continues to work fine.

**No Hibernate Session API migration needed.** ✅

### No `javax.*` issues:
Only `javax.sql.DataSource` found in `FlywayMigrationTest.java` — this is a **JDK class, NOT Jakarta EE**, so it stays as `javax.sql.DataSource`. ✅

---

## 6. Testcontainers 2.x Migration

### Current usage (all in nexus-portal tests):
- `TestcontainersConfig.java` — uses `PostgreSQLContainer`, `RabbitMQContainer`, `GenericContainer`, `@ServiceConnection`
- `FlywayMigrationTest.java` (nexus-data-jpa) — uses `PostgreSQLContainer`, `@Container`, `@Testcontainers`
- 5+ integration tests use `@Testcontainers` + `@Import(TestcontainersConfig.class)`

### Testcontainers 2.x changes:
- Module coordinates may change (per OpenRewrite recipes)
- Root BOM still says `1.19.7` but property says `2.0.5`
- Child POMs (portal) hardcode `1.19.7`

**FIX**: 
1. Update root BOM `<dependencyManagement>` to use `${testcontainers.version}` 
2. Remove all hardcoded versions in child POMs
3. Verify API compatibility (container class names may change in 2.x)

---

## 7. Redisson 4.x Migration

### Current usage (all in nexus-portal):
- `RateLimitInterceptor.java` — `RedissonClient`, `RRateLimiter`, `RateIntervalUnit`, `RateType`
- `OutboxEventProcessor.java` — `RedissonClient`, `RLock`
- `UmsMemberCouponServiceImpl.java` — `RedissonClient`, `RLock`
- `OmsPortalOrderServiceImpl.java` — `RedissonClient`, `RLock`, `getMultiLock()`
- Tests: `RateLimitInterceptorTest.java`, `OutboxEventProcessorTest.java`

### Dependency:
- Root POM property: `redisson.version = 4.7.0`
- Root POM dependencyManagement: HARDCODED `3.33.0` (not using property!)
- Artifact: `redisson-spring-boot-starter`

**FIX**:
1. Update dependencyManagement to use `${redisson.version}`
2. Check if Redisson 4.x renamed the starter artifact
3. Check for removed/deprecated codecs

---

## 8. YAML Configuration Issues (from earlier phases, still present)

### application.yml:
- L22: `matching-strategy: ant_path_matcher` — **REMOVE** (deprecated, Boot 3+ defaults to path_pattern_parser)
- L29: `dialect: org.hibernate.dialect.PostgreSQLDialect` — **REMOVE** (Hibernate 6+ auto-detects)
- L48-49: Swagger 2 / Springfox URLs (`/swagger-resources/**`, `/v2/api-docs`) — **REMOVE**

### application-dev.yml:
- L23: `dialect: org.hibernate.dialect.PostgreSQLDialect` — **REMOVE**
- L26-27: `spring.data.mongodb.uri` — **REMOVE** (MongoDB not used)

### application-prod.yml:
- L22: `dialect: org.hibernate.dialect.PostgreSQLDialect` — **REMOVE**
- L25-26: `spring.data.mongodb.uri` — **REMOVE**
- L12: `password: postgres` — **Should be `${DB_PASSWORD}`** (security)
- L36-37: `username: guest / password: guest` — **Should use env vars** (security)
- L50-51: Hardcoded `localhost:5174` URLs — **Should use `${FRONTEND_BASE_URL}`** (config)
- L60: `com.nexusengine.core: debug` — **Change to INFO** for prod

---

## 9. Jackson 2.x → 3.x (Boot 4)

### Files using Jackson:
- `JacksonConfig.java` (nexus-portal) — `ObjectMapper`, `Jackson2ObjectMapperBuilder`, `JsonInclude`
- `BaseRedisConfig.java` (nexus-common) — `ObjectMapper`, `JsonAutoDetect`, `PropertyAccessor`, `LaissezFaireSubTypeValidator`, `Jackson2JsonRedisSerializer`
- `RateLimitInterceptor.java` — `ObjectMapper`
- `OmsPortalOrderServiceImpl.java` — `ObjectMapper`, `JsonNode`
- `UmsMember.java`, `UmsAdmin.java` — `@JsonIgnore`
- Multiple test files

### Impact Assessment:
Jackson 3.x in Boot 4 may change package from `com.fasterxml.jackson` to `tools.jackson`. However, Spring Boot 4 may still use Jackson 2.x initially or provide compatibility. **Need to verify actual Boot 4.1.1 Jackson version.** If Jackson 3.x with new packages, ALL imports need updating.

Spring Boot typically manages Jackson version via its BOM — if Boot 4.1.1 declares Jackson 3.x, the `Jackson2ObjectMapperBuilder` may be renamed. Check Boot 4 release notes.

**Conservative approach**: Keep existing Jackson code and let Boot 4's auto-configuration handle version management. Only update if compilation fails.

---

## 10. Resilience4j

### Current artifact:
- `resilience4j-spring-boot3` version `2.4.0`

### Used in:
- `nexus-admin/pom.xml` and `nexus-portal/pom.xml` as dependency
- `application.yml` — circuit breaker, rate limiter, retry config

### Boot 4 compatibility:
- Check if `resilience4j-spring-boot3` works with Boot 4 or if there's a `resilience4j-spring-boot4` artifact
- Plan says "Built-in Retry: Native RetryTemplate in Spring Core" — could potentially replace Resilience4j retry, but circuit breaker and rate limiter still need Resilience4j

---

## 11. Execution Plan (ORDERED)

### Step 1: Fix Root POM dependency versions
- Fix Testcontainers BOM version: `1.19.7` → `${testcontainers.version}`
- Fix Redisson dependencyManagement version: `3.33.0` → `${redisson.version}`

### Step 2: Fix all child POM hardcoded versions
- nexus-data-jpa: hibernate-vector `6.6.49.Final` → `${hibernate-vector.version}`
- nexus-portal: Remove hardcoded bucket4j, testcontainers, spring-ai, minio versions
- nexus-ai: Remove hardcoded spring-ai versions

### Step 3: Spring AI 2.x artifact migration
- Replace `spring-ai-starter-model-openai` → `spring-ai-starter-model-google-genai` (per plan)
- Remove `spring-ai-starter-model-vertex-ai-gemini` (replaced by google-genai)
- Update YAML from `spring.ai.openai` → `spring.ai.google.genai` namespace

### Step 4: Spring Security 7 — Remove deprecated APIs
- Replace `ConfigAttribute` with `String` throughout security components
- Remove `FilterInvocationSecurityMetadataSource` from DynamicSecurityMetadataSource
- Remove `org.springframework.security.access.SecurityConfig` usage
- Convert field injection → constructor injection

### Step 5: YAML cleanup
- Remove deprecated configs (ant_path_matcher, hibernate dialect, swagger 2 urls, mongodb)
- Update prod security (env vars for passwords)

### Step 6: Testcontainers 2.x compatibility check
- Verify container class names still valid
- Ensure BOM version propagates correctly

### Step 7: Redisson 4.x compatibility check  
- Verify API compatibility (RLock, RRateLimiter, RedissonClient, getMultiLock)

### Step 8: Compile and fix
- Run `mvn compile -pl nexus-common` first (base module)
- Fix any compilation errors module by module
- Run tests

---

## 12. Files That Need Modification (Complete List)

### POM files:
1. `NexusCore/pom.xml` — Fix BOM versions, resilience4j artifact
2. `NexusCore/nexus-data-jpa/pom.xml` — Fix hibernate-vector version
3. `NexusCore/nexus-portal/pom.xml` — Remove 6 hardcoded versions, update AI artifacts
4. `NexusCore/nexus-ai/pom.xml` — Remove 2 hardcoded versions, update AI artifacts

### Security files (Spring Security 7):
5. `nexus-security/.../DynamicSecurityService.java` — ConfigAttribute → String
6. `nexus-security/.../DynamicSecurityMetadataSource.java` — Major rewrite (remove FilterInvocationSecurityMetadataSource)
7. `nexus-security/.../DynamicAuthorizationManager.java` — ConfigAttribute → String
8. `nexus-security/.../SecurityConfig.java` — Constructor injection
9. `nexus-security/.../CommonSecurityConfig.java` — No changes needed (clean)
10. `nexus-security/.../JwtAuthenticationTokenFilter.java` — Constructor injection
11. `nexus-admin/.../AdminNexusSecurityConfig.java` — Remove SecurityConfig class usage, constructor injection
12. `nexus-portal/.../PortalNexusSecurityConfig.java` — Constructor injection
13. `nexus-application/.../UnifiedSecurityConfig.java` — Constructor injection
14. `nexus-security/test/.../DynamicAuthorizationManagerTest.java` — Update for new API

### AI files (Spring AI 2.x):
15. `nexus-ai/.../AiAutoConfiguration.java` — Update docstring
16. `nexus-ai/.../AbstractNexusChatService.java` — Verify API compatibility, constructor injection

### YAML files:
17. `nexus-application/.../application.yml` — Remove deprecated configs, update AI namespace
18. `nexus-application/.../application-dev.yml` — Remove dialect, mongodb, update AI config
19. `nexus-application/.../application-prod.yml` — Remove dialect, mongodb, security hardening
20. `nexus-portal/test/.../application-test.yml` — Update AI config namespace
