-- V19: Add Indexes for Dashboard Performance
-- The dashboard runs aggregate queries on oms_order and ums_member.
-- These indexes prevent full table scans when filtering by create_time, status, etc.

CREATE INDEX IF NOT EXISTS idx_oms_order_create_time_status ON oms_order(create_time, status);
CREATE INDEX IF NOT EXISTS idx_oms_order_status ON oms_order(status);
CREATE INDEX IF NOT EXISTS idx_oms_order_return_status ON oms_order_return_apply(status);
CREATE INDEX IF NOT EXISTS idx_ums_member_create_time ON ums_member(create_time);
CREATE INDEX IF NOT EXISTS idx_pms_product_publish_stock ON pms_product(publish_status, stock);

