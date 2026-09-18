import re

with open('/home/aadarsh/Documents/NexusEngine/document/plans/nexus-engine-master-plan.md', 'r') as f:
    content = f.read()

# Insert new Phase 2
new_phase_2 = """## Phase 2: Loyalty & Membership Cleanup (Task 1)

> [!IMPORTANT]
> We are aggressively simplifying the membership and loyalty system to demonstrate pragmatic engineering: keeping what works (transactional points as currency) and removing dead-weight complexity (unused tiers, experience points, and custom member pricing tables).

### Task 2.1: Remove `UmsMemberLevel`
- [ ] Drop table `ums_member_level`
- [ ] Remove entity, repository, service, and controller for `UmsMemberLevel`
- [ ] Drop `member_level_id` from `ums_member`
- [ ] Update Admin UI to remove Member Level references

### Task 2.2: Remove `PmsMemberPrice`
- [ ] Drop table `pms_member_price`
- [ ] Remove `PmsMemberPrice` entity and repository
- [ ] Remove from `PmsProductParam`, `PmsProductResult`, and `PmsProductServiceImpl`

### Task 2.3: Clean Up Experience & Growth Points
- [ ] Drop `experience_points` from `ums_member` and `oms_order`
- [ ] Drop `gift_growth` from `oms_order_item` and `pms_product`
- [ ] Remove related logic from entities, services, and frontend types

### Task 2.4: Rename Reward Points to just "Points"
- [ ] Update `ums_member`: `reward_points` -> `points`, `lifetime_reward_points` -> `lifetime_points`
- [ ] Update `oms_order`: `reward_points` -> `earned_points`, `use_integration` -> `used_points`, `integration_amount` -> `points_discount_amount`
- [ ] Update `oms_order_item`: `gift_integration` -> `earned_points`, `integration_amount` -> `points_discount_amount`
- [ ] Align all frontend and backend code to simply use "points"

### Task 2.5: Apply Database Changes
- [ ] Create Flyway `V2__loyalty_cleanup.sql` to execute these schema drops and renames.
- [ ] Update `V1__baseline.sql` to reflect these changes so new setups are clean.

---

## Phase 3: Seed Full E-Commerce Catalog (Flyway V3)"""

content = content.replace("## Phase 2: Seed Full E-Commerce Catalog (Flyway V2)", new_phase_2)

# Shift other phases
content = content.replace("## Phase 3: MinIO Media Architecture", "## Phase 4: MinIO Media Architecture")
content = content.replace("Task 3.", "Task 4.")

content = content.replace("## Phase 4: Admin Roles & Membership System", "## Phase 5: Admin Roles System")
content = content.replace("Task 4.1", "Task 5.1")
content = content.replace("Task 4.2", "Task 5.2")
content = content.replace("Task 4.4", "Task 5.3") # Shift 4.4 to 5.3 since 4.3 is removed

# Remove Task 4.3 (Membership Levels)
# It looks like:
task_4_3 = """### Task 4.3: Membership Levels
- [ ] Populate `ums_member_level` with tiered membership system:

| Level | Name | Growth Points | Free Shipping | VIP Pricing | Birthday Perk |
|---|---|---|---|---|---|
| 1 | Bronze (Default) | 0 | No | No | No |
| 2 | Silver | 1000 | ₹499+ | 3% off | No |
| 3 | Gold | 5000 | ₹299+ | 5% off | Yes |
| 4 | Platinum | 15000 | Free | 8% off | Yes |
| 5 | Diamond | 50000 | Free | 12% off | Yes |

"""
content = content.replace(task_4_3, "")

content = content.replace("## Phase 5: Testing & Verification", "## Phase 6: Testing & Verification")
content = content.replace("Task 5.1", "Task 6.1")
content = content.replace("Task 5.2", "Task 6.2")


# Update diagram
diagram_old = """    A["✅ Phase 1: Fix 500 Errors<br/>Entity fixes + V1 baseline migration<br/>COMPLETE"] --> B["Phase 2: Seed Catalog<br/>V2 migration with brands/categories/products/SKUs"]
    B --> C["Phase 3: MinIO Media<br/>Create object keys + upload placeholder images"]
    C --> D["Phase 4: Admin & Membership<br/>Roles, brand admins, membership levels"]
    D --> E["Phase 5: Test & Verify<br/>All endpoints + both frontends"]"""

diagram_new = """    A["✅ Phase 1: Fix 500 Errors<br/>COMPLETE"] --> B["Phase 2: Loyalty Cleanup<br/>Simplify points & remove tiers"]
    B --> C["Phase 3: Seed Catalog<br/>V3 migration with brands/products"]
    C --> D["Phase 4: MinIO Media<br/>Create object keys + uploads"]
    D --> E["Phase 5: Admin Roles<br/>Roles, brand admins"]
    E --> F["Phase 6: Test & Verify<br/>All endpoints + both frontends"]"""

content = content.replace(diagram_old, diagram_new)

# Update Flyway Migration Plan table
flyway_old = """| `V2__seed_full_catalog.sql` | Full catalog seed (brands, categories, products, SKUs, attributes, media, banners) | ⬜ Pending |
| `V3__membership_and_admin.sql` | Membership tiers, brand admin accounts, role assignments | ⬜ Pending |"""

flyway_new = """| `V2__loyalty_cleanup.sql` | Schema cleanup for member levels, prices, and points | ⬜ Pending |
| `V3__seed_full_catalog.sql` | Full catalog seed (brands, categories, products, SKUs, attributes, media, banners) | ⬜ Pending |
| `V4__admin_roles.sql` | Brand admin accounts, role assignments | ⬜ Pending |"""

content = content.replace(flyway_old, flyway_new)
content = content.replace("via `V2__seed_full_catalog.sql`", "via `V3__seed_full_catalog.sql`")
content = content.replace("Target Rows", "Target Rows (After Cleanup)")
content = content.replace("| `ums_member_level` | 1 (Gold) | 5 (Bronze→Diamond) |", "| `ums_member_level` | 1 (Gold) | 0 (Table to be dropped) |")


with open('/home/aadarsh/Documents/NexusEngine/document/plans/nexus-engine-master-plan.md', 'w') as f:
    f.write(content)
