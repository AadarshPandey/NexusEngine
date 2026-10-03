# NexusEngine Schema Migration & Cleanup SOP

Use this prompt/checklist for future requests when you need to safely drop tables or columns in the NexusEngine stack. This ensures all layers (Database, Backend, Frontend, and Docker) are seamlessly updated without breaking the application.

## 📝 The Prompt Template for Next Time

> **"Please remove the following columns: `[LIST OF COLUMNS]` from the `[TABLE NAME]` table. Follow the NexusEngine Schema Migration SOP: resolve all codebase dependencies across all modules (Admin, Portal, Search), update frontend interfaces, modify the Flyway and data seed SQL scripts, execute the drop on the live database, and rebuild the Docker containers."**

---

## ✅ AI Assistant Checklist for Dropping Columns/Tables

When executing the prompt above, the AI must strictly follow these steps in order:

### 1. Comprehensive Code Search
- Search across the **entire backend** (`./NexusCore`) using `grep` to find all getters, setters, and usages.
- **Search for raw string references:** The Java compiler will *not* catch column names passed as raw strings. Explicitly `grep` for the column names being used inside `Sort.by("columnName")`, `@Query`, custom JPQL, and native SQL in DAOs and Repositories.
- *Lesson learned:* Do not just check the Admin module. Check customer-facing modules like `nexus-portal` (e.g., `PmsPortalReviewServiceImpl` modifying `product_comment_count` dynamically, and `HomeDao.java` hardcoding `Sort.by(..., "sort")`).
- Search the **frontend codebase** (`./NexusAdminPanel` and `./NexusFrontendWeb`) for column names, camelCase property names, and TypeScript interfaces.

### 2. Clean Up Backend (Java / Spring Boot)
- Remove the fields from JPA Entities (`@Entity`) and DTOs (`record` or class).
- Remove logic from Controllers and Service Implementations that references these fields.
- Remove or update any manual `Sort.by(...)` or `@Query(...)` parameters that relied on the deleted columns.
- Run a quick `mvn clean compile -T 4` locally in `./NexusCore` to guarantee no hidden compilation errors remain (but remember this won't catch raw string bugs!).

### 3. Clean Up Frontend (React / TypeScript)
- Remove the fields from TypeScript type definitions (e.g., `src/types/*.d.ts`).
- Remove the columns from Table headers (`TableHead`), Table rows (`TableBody`), and Data forms in the React components.

### 4. Update Database Migration Scripts
- Remove the columns/tables from `V1__baseline.sql` (schema definitions).
- Remove the data inserts for these columns from `V2__seed_full_catalog.sql` (use a Python regex script or `sed` for accuracy).
- Remove the definitions and `COPY` data mapping in `./resource_setup/postgres_data.sql`.

### 5. Sync the Live Running Environment
- **Apply to Live DB:** Run the `ALTER TABLE ... DROP COLUMN ...` command directly on the running Postgres container so changes reflect in DataGrip immediately without needing a full volume wipe:
  ```bash
  docker exec postgres psql -U postgres -d nexuscore -c "ALTER TABLE schema.table DROP COLUMN column_name;"
  ```
- **Rebuild Docker Images:** Changes made to the Java and React code are not picked up by a simple container restart. You **must** rebuild the docker images so the old code stops trying to query the dropped columns:
  ```bash
  docker compose up -d --build nexus-application nexus-admin-panel
  ```
