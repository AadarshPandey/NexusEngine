-- V16__normalize_inventory.sql
-- FAANG Modular Monolith: Normalizing Inventory and Pricing to SKU strictly

-- 1. Drop redundant price and stock from the base product table
ALTER TABLE pms_product DROP COLUMN IF EXISTS price;
ALTER TABLE pms_product DROP COLUMN IF EXISTS stock;

-- 2. Convert sp_data (which is currently a VARCHAR storing JSON) to actual JSONB for modern querying
ALTER TABLE pms_sku_stock ALTER COLUMN sp_data TYPE JSONB USING sp_data::JSONB;
ALTER TABLE pms_sku_stock RENAME COLUMN sp_data TO sku_attributes;
