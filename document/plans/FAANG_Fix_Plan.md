# FAANG Audit Fix Implementation Plan

Here is the step-by-step plan to resolve the 5 must-fix items identified in the NexusEngine audit.

- [x] **1. Fix `FetchType.EAGER` to `LAZY`**
  - **Target:** `PmsProduct.java`
  - **Action:** Change `@OneToMany(fetch = FetchType.EAGER)` to `FetchType.LAZY` for `mediaList` to prevent N+1 and Cartesian product issues when fetching product lists.

- [x] **2. Fix Typo: `recommandStatus` → `recommendStatus`**
  - **Target:** `PmsProduct.java`, `EsProduct.java`, and relevant getters/setters/database columns in controllers.
  - **Action:** Perform a codebase-wide rename of `recommandStatus` to `recommendStatus` for better professionalism. Added Flyway V26.

- [x] **3. Fix Partial-Update Overwrite Bug**
  - **Target:** `UmsAdminServiceImpl.java` (`update` method)
  - **Action:** Refactor the logic to only apply non-null properties from the incoming DTO to the retrieved `rawAdmin` entity before saving, preventing data loss on omitted fields.

- [x] **4. Enforce RESTful HTTP Verbs**
  - **Target:** Controllers (e.g., `UmsAdminController.java`, `PmsProductController.java`, etc.)
  - **Action:** Refactor endpoints like `POST /update/{id}` to `PUT /{id}` (or PATCH), and `POST /delete/{id}` to `DELETE /{id}` to comply with FAANG REST standards. Scripted and executed.

- [x] **5. Introduce Java Enums for Order Status**
  - **Target:** `OmsOrder.java`, `OmsOrder.status`, `payType`, `orderType`
  - **Action:** Created `OrderStatus`, `PayType`, `OrderType` enums and added a Flyway V27 `CHECK` constraint to guarantee data integrity at the database layer.

I will now begin executing these fixes and will update this checklist as each item is completed.
