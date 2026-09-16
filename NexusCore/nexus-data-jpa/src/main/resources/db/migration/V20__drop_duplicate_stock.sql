-- V20: Drop Duplicate Stock and Price Columns from pms_product
-- Enforces pms_sku_stock as the single source of truth for inventory and pricing (Phase 3.1)

ALTER TABLE pms_product
    DROP COLUMN IF EXISTS price,
    DROP COLUMN IF EXISTS original_price,
    DROP COLUMN IF EXISTS promotion_price,
    DROP COLUMN IF EXISTS stock,
    DROP COLUMN IF EXISTS low_stock;
