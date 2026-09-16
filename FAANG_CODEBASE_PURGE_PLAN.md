# FAANG Database & Codebase Purge Plan

**Objective:** Safely eliminate the ~70 legacy tables (like the `cms_*` blogging tables and complex `sms_*` marketing tables) inherited from the Chinese framework. We must do this *without* crashing the Spring Boot application by systematically removing dependencies from the frontend to the backend *before* executing the SQL `DROP` commands.

## Phase 1: Identify the "Keep" vs "Drop" Tables
Our core architecture relies on the Modular Monolith structure:
* **KEEP:** `pms_*` (Product/Catalog), `oms_*` (Orders), `ums_*` (Users/Auth)
* **KEEP & RENAME:** `sms_home_advertise` -> `sys_banner` (The frontend currently relies on this for the Home Page carousel).
* **DROP:** All `cms_*` tables (Subject, Topic, Help, Prefrence Area)
* **DROP:** All complex `sms_*` tables (Flash Promotions, Home Brand links, Home Recommend Subjects).

## Phase 2: Frontend Dependency Resolution (React)
1. **Home.tsx & BannerCarousel.tsx:** Ensure they only rely on the renamed `sys_banner` API and basic product fetchers. 
2. **Remove Subject/Topic references:** Delete any UI components rendering `subjectList` from the Home API payload (we already commented most of this out earlier, but we will clean the TypeScript interfaces completely).

## Phase 3: Backend Dependency Resolution (Spring Boot)
If we simply drop the database tables right now, `nexus-admin` and `nexus-portal` will crash on startup because they scan for JPA entities and controllers linked to those tables. We must surgically remove them:
1. **Delete Admin Endpoints (`nexus-admin`):** 
   - Delete `CmsSubjectController.java`, `SmsFlashPromotionController.java`, etc.
   - Delete their corresponding `*Service.java` and `*ServiceImpl.java` files.
2. **Update Portal Aggregators (`nexus-portal`):**
   - Update `HomeDao.java` and `HomeContentResult.java` to stop injecting `CmsSubject`.
   - Update API endpoints to only return Products, Brands, and Banners.
3. **Delete JPA Models (`nexus-data-jpa`):**
   - Delete the `CmsSubject.java`, `SmsFlashPromotion.java` entities.
   - Delete `CmsSubjectRepository.java`, etc.

## Phase 4: Database Execution (Flyway)
Once the Java bytecode is compiled without *any* references to the bloat tables, it is finally safe to execute the physical drops.
1. **[ ] Create Migration:** Write `V18__purge_legacy_bloat.sql`.
2. **[ ] Execute Drops:** Execute `DROP TABLE cms_subject;`, `DROP TABLE sms_flash_promotion;` (over 50 tables total).
3. **[ ] Rename Banner:** Execute `ALTER TABLE sms_home_advertise RENAME TO sys_banner;`

## Phase 5: Build & Deploy
1. **[ ] Compile:** Run `mvn clean install` across all 9 modules to ensure 0 compilation errors.
2. **[ ] Boot Test:** Start the application to prove it boots cleanly with only the ~20 core tables.
