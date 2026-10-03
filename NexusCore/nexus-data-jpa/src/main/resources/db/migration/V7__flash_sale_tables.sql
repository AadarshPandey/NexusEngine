-- V2__flash_sale_tables.sql

-- Flash promotion event (e.g., "Diwali Mega Sale")
CREATE TABLE sms_flash_promotion (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200)    NOT NULL,
    start_date      DATE            NOT NULL,
    end_date        DATE            NOT NULL,
    status          INTEGER         DEFAULT 0,  -- 0=inactive, 1=active
    create_time     TIMESTAMP       DEFAULT NOW()
);
COMMENT ON TABLE sms_flash_promotion IS 'Flash promotion event definition';

-- Flash promotion time session (e.g., "10:00 AM - 12:00 PM")
CREATE TABLE sms_flash_promotion_session (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200)    NOT NULL,
    start_time      TIME            NOT NULL,   -- e.g., '10:00:00'
    end_time        TIME            NOT NULL,   -- e.g., '12:00:00'
    status          INTEGER         DEFAULT 0,  -- 0=inactive, 1=active
    create_time     TIMESTAMP       DEFAULT NOW()
);
COMMENT ON TABLE sms_flash_promotion_session IS 'Time slots within a flash promotion day';

-- Product <-> Session mapping with flash-specific price and stock
CREATE TABLE sms_flash_promotion_product_relation (
    id                              BIGSERIAL PRIMARY KEY,
    flash_promotion_id              BIGINT          NOT NULL REFERENCES sms_flash_promotion(id),
    flash_promotion_session_id      BIGINT          NOT NULL REFERENCES sms_flash_promotion_session(id),
    product_id                      BIGINT          NOT NULL,
    flash_promotion_price           DECIMAL(10,2)   NOT NULL,
    flash_promotion_count           INTEGER         NOT NULL,   -- Total flash stock
    flash_promotion_limit           INTEGER         DEFAULT 1,  -- Per-user limit
    UNIQUE(flash_promotion_id, flash_promotion_session_id, product_id)
);
COMMENT ON TABLE sms_flash_promotion_product_relation IS 'Products in a specific flash session with flash pricing';
CREATE INDEX idx_fpp_session ON sms_flash_promotion_product_relation(flash_promotion_id, flash_promotion_session_id);

-- Flash sale order log for auditing
CREATE TABLE sms_flash_promotion_log (
    id              BIGSERIAL PRIMARY KEY,
    member_id       BIGINT      NOT NULL,
    product_id      BIGINT      NOT NULL,
    flash_promotion_id BIGINT,
    session_id      BIGINT,
    order_id        BIGINT,
    quantity        INTEGER     DEFAULT 1,
    status          VARCHAR(50) DEFAULT 'PENDING',  -- PENDING, SUCCESS, FAILED
    create_time     TIMESTAMP   DEFAULT NOW()
);
CREATE INDEX idx_flash_log_member ON sms_flash_promotion_log(member_id, product_id);
