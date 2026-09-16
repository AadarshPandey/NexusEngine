# FAANG Architecture Refactor Checklist: Modular Monolith

This document tracks the migration of the legacy macro-service schema into a strict, FAANG-level Modular Monolith.

## 🗄️ Phase 1: 1NF Media Normalization (Current Focus)
- [x] **1.1 DB Migration:** Create `pms_product_media`, migrate data from `album_pics` via `unnest(string_to_array())`, and drop `album_pics` column (`V14`).
- [x] **1.2 JPA Entities:** Create `PmsProductMedia.java`, remove `albumPics` from `PmsProduct.java`, add `@OneToMany` relation.
- [x] **1.3 DTO - [ ] **1.3 DTO & Services:** Update `PmsPortalProductDetail` Services:** Update `PmsPortalProductDetail` and `PmsProductService` to serve the new media array.
- [x] **1.4 Frontend:** Update React TS interfaces and `ProductDetail.tsx` to iterate over `mediaList` instead of `.split(",")`.

## 🔗 Phase 2: Bounded Context Isolation (Soft Links)
- [x] **2.1 Order Context:** Drop hard FKs from `oms_order` and `oms_order_item` that point to `pms_` or `ums_` tables. Replace with logical IDs.
- [x] **2.2 Customer Context:** Drop hard FKs from `ums_member` to `ums_member_level`.
- [x] **2.3 Promotion Context:** Drop hard FKs from `sms_coupon_history` pointing to `oms_order` and `ums_member`.

## 📦 Phase 3: Inventory & SKU Consistency
- [x] **3.1 Drop Duplicate Stock:** Remove `stock` and `price` from `pms_product` (making `pms_sku_stock` the single source of truth).
- [x] **3.2 Concurrency Control:** Ensure `lock_stock` logic is strictly enforced in `pms_sku_stock`.
- [x] **3.3 JSONB Attributes:** Migrate legacy `sp1`, `sp2` columns in `pms_sku_stock` into a single `sku_attributes` JSONB column.

## 💳 Phase 4: Payment Idempotency
- [ ] **4.1 Idempotency Table:** Create `oms_payment_transaction` (order_id, transaction_id, status, payload).
- [ ] **4.2 Webhook Service:** Implement idempotent payment verification logic in `OmsOrderService`.

---
*Status: Starting Phase 1...*
