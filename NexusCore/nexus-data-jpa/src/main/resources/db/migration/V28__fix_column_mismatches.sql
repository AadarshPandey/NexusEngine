-- Flyway V25: Cleanup legacy columns and fix mismatches

-- 1. Fix pms_product legacy columns
ALTER TABLE pms_product DROP COLUMN IF EXISTS feight_template_id;
ALTER TABLE pms_product DROP COLUMN IF EXISTS album_pics;

-- 2. Fix pms_sku_stock legacy columns
ALTER TABLE pms_sku_stock DROP COLUMN IF EXISTS sp_data;

-- 3. Fix ums_member_level duplicate old columns
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS comment_growth_point;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS free_freight_point;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS priviledge_birthday;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS priviledge_comment;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS priviledge_free_freight;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS priviledge_member_price;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS priviledge_promotion;
ALTER TABLE ums_member_level DROP COLUMN IF EXISTS priviledge_sign_in;
