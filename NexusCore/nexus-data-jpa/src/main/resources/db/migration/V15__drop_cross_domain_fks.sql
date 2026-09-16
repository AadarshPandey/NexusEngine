-- V15__drop_cross_domain_fks.sql
-- FAANG Modular Monolith: Replacing Hard Foreign Keys with Soft Links across Domain Boundaries

-- 1. Severing Order Context (OMS) dependencies on external domains
ALTER TABLE oms_order DROP CONSTRAINT IF EXISTS fk_oms_order_member_id;
ALTER TABLE oms_order DROP CONSTRAINT IF EXISTS fk_oms_order_coupon_id;
ALTER TABLE oms_order_item DROP CONSTRAINT IF EXISTS fk_oms_order_item_product_id;
ALTER TABLE oms_order_item DROP CONSTRAINT IF EXISTS fk_oms_order_item_product_category_id;
ALTER TABLE oms_order_item DROP CONSTRAINT IF EXISTS fk_oms_order_item_product_sku_id;

-- 2. Severing Promotion Context (SMS) dependencies on external domains
ALTER TABLE sms_coupon_history DROP CONSTRAINT IF EXISTS fk_sms_coupon_history_member_id;
ALTER TABLE sms_coupon_history DROP CONSTRAINT IF EXISTS fk_sms_coupon_history_order_id;

-- 3. Severing internal Customer Context (UMS) strict hierarchy to prevent dangerous ON DELETE CASCADEs
ALTER TABLE ums_member DROP CONSTRAINT IF EXISTS fk_ums_member_member_level_id;

