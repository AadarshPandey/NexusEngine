# Migration Plan: Chinglish to Standard English E-Commerce Terminology

## 1. Objective
Refactor legacy database table names and column attributes that contain spelling mistakes or literal translations from Chinese e-commerce concepts (e.g., "first_fee", "integration"). The goal is to migrate these to industry-standard English terminology across the entire stack (PostgreSQL -> Spring Boot -> React Frontend) without breaking existing functionality.

## 2. Dictionary / Renaming Mapping

### A. Shipping & Freight (`pms_feight_template`)
| Old Name | New Name | Type | Notes |
|----------|----------|------|-------|
| `pms_feight_template` | `pms_freight_template` | Table | Fix typo |
| `first_weight` | `base_weight` | Column | Standard shipping term |
| `first_fee` | `base_shipping_fee` | Column | Standard shipping term |
| `continue_weight` | `incremental_weight_unit` | Column | Standard shipping term |
| `continme_fee` | `incremental_fee` | Column | Fix typo & terminology |
| `feight_template_id` | `freight_template_id` | Column | Found in `pms_product` |

### B. Loyalty Points & VIP Tiers (`ums_member`, `ums_member_level`, `ums_member_task`)
| Old Name | New Name | Type | Notes |
|----------|----------|------|-------|
| `intergration` / `integration` | `reward_points` | Column | Direct translation of 积分 |
| `history_integration` | `lifetime_reward_points` | Column | |
| `growth` | `experience_points` | Column | Direct translation of 成长值 |
| `luckey_count` | `bonus_draws_remaining` | Column | Fix typo & clarify intent |
| `comment_growth_point` | `review_reward_xp` | Column | XP given for reviews |
| `free_freight_point` | `free_shipping_threshold`| Column | Min order amount |

### C. VIP Perks / Privileges (`ums_member_level`)
*Note: These act as boolean flags.*
| Old Name | New Name | Type | Notes |
|----------|----------|------|-------|
| `priviledge_member_price` | `has_vip_pricing` | Column | Fix typo & clarify |
| `priviledge_free_freight` | `has_free_shipping_perk`| Column | |
| `priviledge_sign_in` | `can_earn_login_rewards`| Column | |
| `priviledge_comment` | `has_review_privilege` | Column | |
| `priviledge_promotion` | `has_promotion_privilege`| Column | |
| `priviledge_birthday` | `has_birthday_privilege` | Column | |

### D. Verification & Auditing (`pms_product_vertify_record`)
| Old Name | New Name | Type | Notes |
|----------|----------|------|-------|
| `pms_product_vertify_record`| `pms_product_verify_record` | Table | Fix typo |
| `vertify_man` | `reviewer_name` | Column | Fix typo & terminology |
| `vertify_status` | `approval_status` | Column | |

### E. Social, Wishlists & Collections (`pms_comment`, `ums_member_statistics_info`)
| Old Name | New Name | Type | Notes |
|----------|----------|------|-------|
| `pms_comment_replay` | `pms_comment_reply` | Table | Fix typo |
| `replay_count` | `reply_count` | Column | Fix typo |
| `collect_couont` | `likes_count` | Column | Fix typo (couont) |
| `collect_product_count` | `saved_products_count` | Column | "Collect" means wishlist |
| `collect_subject_count` | `saved_articles_count` | Column | |
| `collect_topic_count` | `saved_topics_count` | Column | |
| `collect_comment_count` | `liked_comments_count` | Column | |

### F. Preferences & Features (`cms_prefrence_area`)
| Old Name | New Name | Type | Notes |
|----------|----------|------|-------|
| `cms_prefrence_area` | `cms_preference_area` | Table | Fix typo |
| `cms_prefrence_area_product_relation`| `cms_preference_area_product_relation`| Table | Fix typo |
| `prefrence_area_id` | `preference_area_id` | Column | |

---

## 3. Execution Steps for the AI Agent
*Note for future agents: Execute these steps sequentially. Do not skip phases to avoid breaking the application state.*

### Phase 1: Database Migration (Flyway)
1. Create a new Flyway migration script (e.g., `V5__rename_chinese_to_english.sql`) in `NexusCore/nexus-data-jpa/src/main/resources/db/migration/`.
2. Write `ALTER TABLE ... RENAME TO ...` and `ALTER TABLE ... RENAME COLUMN ... TO ...` statements for all mappings defined in Section 2.
3. *Crucial:* Ensure you also rename the Foreign Key constraints associated with these columns/tables to match their new names.

### Phase 2: JPA Entity & Repository Layer Updates
1. Rename the physical `.java` files for the affected entities and repositories (e.g., `PmsFeightTemplate.java` -> `PmsFreightTemplate.java`).
2. Update the class names, `@Table` annotations, and `@Column` annotations to match the new database schema.
3. Update standard Spring Data JPA method names (e.g., `findByFeightTemplateId` -> `findByFreightTemplateId`) if they exist.

### Phase 3: Business Logic & DTO Refactor (Backend)
1. Rename service interfaces and implementations (e.g., `CmsPrefrenceAreaService` -> `CmsPreferenceAreaService`).
2. Update all DTO fields in `nexus-admin` and `nexus-portal` (e.g., in `PmsProductParam`).
3. Update API Endpoint paths in Controllers (e.g., `@RequestMapping("/prefrenceArea")` -> `@RequestMapping("/preferenceArea")`).
4. Recompile the backend (`mvn clean install`) and fix any resulting compilation errors caused by renamed getter/setter methods.

### Phase 4: React Frontend Refactor
1. Perform a global search and replace in the React frontend repository for the changed API endpoint paths (e.g., `/prefrenceArea` -> `/preferenceArea`).
2. Search and replace JSON payload keys in the frontend state and API calls that correspond to the renamed DTO fields (e.g., `prefrenceAreaProductRelationList` -> `preferenceAreaProductRelationList`).
3. Ensure the React build compiles successfully.
