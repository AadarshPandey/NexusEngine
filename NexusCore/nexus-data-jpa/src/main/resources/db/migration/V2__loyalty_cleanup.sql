-- V2__loyalty_cleanup.sql
-- Simplifies the loyalty and membership system by removing unused tiers, experience points, and custom member pricing tables.

-- 1. Drop unused tables
DROP TABLE IF EXISTS ums_member_level CASCADE;
DROP TABLE IF EXISTS pms_member_price CASCADE;

-- 2. Drop columns from ums_member
ALTER TABLE ums_member DROP COLUMN IF EXISTS member_level_id;
ALTER TABLE ums_member DROP COLUMN IF EXISTS experience_points;

-- 3. Rename points columns in ums_member
ALTER TABLE ums_member RENAME COLUMN reward_points TO points;
ALTER TABLE ums_member RENAME COLUMN lifetime_reward_points TO lifetime_points;

-- 4. Drop columns from oms_order
ALTER TABLE oms_order DROP COLUMN IF EXISTS experience_points;

-- 5. Rename points columns in oms_order
ALTER TABLE oms_order RENAME COLUMN reward_points TO earned_points;
ALTER TABLE oms_order RENAME COLUMN use_integration TO used_points;
ALTER TABLE oms_order RENAME COLUMN integration_amount TO points_discount_amount;

-- 6. Drop columns from oms_order_item
ALTER TABLE oms_order_item DROP COLUMN IF EXISTS gift_growth;

-- 7. Rename points columns in oms_order_item
ALTER TABLE oms_order_item RENAME COLUMN gift_integration TO earned_points;
ALTER TABLE oms_order_item RENAME COLUMN integration_amount TO points_discount_amount;

-- 8. Drop columns from pms_product
ALTER TABLE pms_product DROP COLUMN IF EXISTS gift_growth;

-- 9. sms_coupon - remove member_level
ALTER TABLE sms_coupon DROP COLUMN IF EXISTS member_level;
