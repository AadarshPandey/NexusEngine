-- 1. Add percentage cap for percentage-based coupons
ALTER TABLE sms_coupon ADD COLUMN max_discount_amount DECIMAL(10,2) DEFAULT NULL;
COMMENT ON COLUMN sms_coupon.max_discount_amount IS 'Maximum discount for percentage coupons (cap)';

-- 2. Add brand coupon support (use_type = 3)
CREATE TABLE sms_coupon_brand_relation (
    id          BIGSERIAL PRIMARY KEY,
    coupon_id   BIGINT       NOT NULL,
    brand_id    BIGINT       NOT NULL,
    brand_name  VARCHAR(200) DEFAULT NULL
);
COMMENT ON TABLE sms_coupon_brand_relation IS 'Coupon-brand relation';
COMMENT ON COLUMN sms_coupon_brand_relation.brand_name IS 'Brand name (denormalized)';

CREATE INDEX idx_coupon_id ON sms_coupon_brand_relation(coupon_id);

-- 3. Add optimistic locking for coupon stock
ALTER TABLE sms_coupon ADD COLUMN version INT DEFAULT 0;
COMMENT ON COLUMN sms_coupon.version IS 'Optimistic lock version for stock management';

-- 4. Add index for faster coupon history lookups
CREATE INDEX idx_coupon_history_member_status ON sms_coupon_history(member_id, use_status);

-- 5. Add coupon usage tracking on order
ALTER TABLE sms_coupon_history ADD COLUMN IF NOT EXISTS order_id BIGINT DEFAULT NULL;
ALTER TABLE sms_coupon_history ADD COLUMN IF NOT EXISTS order_sn VARCHAR(100) DEFAULT NULL;
