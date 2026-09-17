-- V1: Consolidated baseline schema for NexusEngine

-- ==========================================
-- PMS (Product Management System)
-- ==========================================

CREATE TABLE IF NOT EXISTS pms_brand (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    first_letter VARCHAR(255),
    sort INTEGER,
    factory_status INTEGER,
    show_status INTEGER,
    product_count INTEGER,
    product_comment_count INTEGER,
    logo VARCHAR(255),
    big_pic VARCHAR(255),
    brand_story VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pms_product_category (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    name VARCHAR(255),
    level INTEGER,
    product_count INTEGER,
    product_unit VARCHAR(255),
    nav_status INTEGER,
    show_status INTEGER,
    sort INTEGER,
    icon VARCHAR(255),
    keywords VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pms_product_attribute_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    attribute_count INTEGER,
    param_count INTEGER
);

CREATE TABLE IF NOT EXISTS pms_product (
    id BIGSERIAL PRIMARY KEY,
    brand_id BIGINT,
    product_category_id BIGINT,
    product_attribute_category_id BIGINT,
    name VARCHAR(255),
    pic VARCHAR(255),
    product_sn VARCHAR(255),
    delete_status INTEGER,
    publish_status INTEGER,
    new_status INTEGER,
    recommend_status INTEGER,
    verify_status INTEGER,
    sort INTEGER,
    sale INTEGER,
    gift_growth INTEGER,
    gift_point INTEGER,
    use_point_limit INTEGER,
    sub_title VARCHAR(255),
    unit VARCHAR(255),
    weight NUMERIC(19,2),
    preview_status INTEGER,
    service_ids VARCHAR(255),
    keywords VARCHAR(255),
    note VARCHAR(255),
    detail_title VARCHAR(255),
    promotion_start_time TIMESTAMP,
    promotion_end_time TIMESTAMP,
    promotion_per_limit INTEGER,
    promotion_type INTEGER,
    brand_name VARCHAR(255),
    product_category_name VARCHAR(255),
    description VARCHAR(255),
    detail_desc VARCHAR(255),
    detail_html VARCHAR(255),
    detail_mobile_html VARCHAR(255),
    vendor_id BIGINT
);

CREATE TABLE IF NOT EXISTS pms_product_media (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    media_type VARCHAR(50),
    media_url VARCHAR(255) NOT NULL,
    sort_order INTEGER
);

CREATE TABLE IF NOT EXISTS pms_product_attribute (
    id BIGSERIAL PRIMARY KEY,
    product_attribute_category_id BIGINT,
    name VARCHAR(255),
    select_type INTEGER,
    input_type INTEGER,
    input_list VARCHAR(255),
    sort INTEGER,
    filter_type INTEGER,
    search_type INTEGER,
    related_status INTEGER,
    hand_add_status INTEGER,
    type INTEGER
);

CREATE TABLE IF NOT EXISTS pms_product_attribute_value (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    product_attribute_id BIGINT,
    value VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pms_product_category_attribute_relation (
    id BIGSERIAL PRIMARY KEY,
    product_category_id BIGINT,
    product_attribute_id BIGINT
);

-- Requires pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS pms_product_embedding (
    product_id BIGINT PRIMARY KEY,
    embedding vector(1536)
);

CREATE TABLE IF NOT EXISTS pms_product_full_reduction (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    full_price NUMERIC(19,2),
    reduce_price NUMERIC(19,2)
);

CREATE TABLE IF NOT EXISTS pms_product_ladder (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    count INTEGER,
    discount NUMERIC(19,2),
    price NUMERIC(19,2)
);

CREATE TABLE IF NOT EXISTS pms_product_operate_log (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    price_old NUMERIC(19,2),
    price_new NUMERIC(19,2),
    sale_price_old NUMERIC(19,2),
    sale_price_new NUMERIC(19,2),
    gift_point_old INTEGER,
    gift_point_new INTEGER,
    use_point_limit_old INTEGER,
    use_point_limit_new INTEGER,
    operate_man VARCHAR(255),
    create_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pms_product_verify_record (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    create_time TIMESTAMP,
    reviewer_name VARCHAR(255),
    status INTEGER,
    detail VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pms_sku_stock (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    sku_code VARCHAR(255),
    price NUMERIC(19,2),
    stock INTEGER,
    low_stock INTEGER,
    pic VARCHAR(255),
    sale INTEGER,
    promotion_price NUMERIC(19,2),
    lock_stock INTEGER,
    sku_attributes JSONB
);

CREATE TABLE IF NOT EXISTS pms_comment (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    member_nick_name VARCHAR(255),
    product_name VARCHAR(255),
    star INTEGER,
    member_ip VARCHAR(255),
    create_time TIMESTAMP,
    show_status INTEGER,
    product_attribute VARCHAR(255),
    likes_count INTEGER,
    read_count INTEGER,
    pics VARCHAR(255),
    member_icon VARCHAR(255),
    reply_count INTEGER,
    content VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pms_comment_reply (
    id BIGSERIAL PRIMARY KEY,
    comment_id BIGINT,
    member_nick_name VARCHAR(255),
    member_icon VARCHAR(255),
    content VARCHAR(255),
    create_time TIMESTAMP,
    type INTEGER
);

CREATE TABLE IF NOT EXISTS pms_member_price (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    member_level_id BIGINT,
    member_price NUMERIC(19,2),
    member_level_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pms_review (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    member_id BIGINT,
    parent_id BIGINT,
    rating INTEGER,
    content VARCHAR(255),
    like_count INTEGER,
    status INTEGER,
    created_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pms_review_media (
    id BIGSERIAL PRIMARY KEY,
    review_id BIGINT,
    media_type VARCHAR(255),
    media_url VARCHAR(255),
    thumbnail_url VARCHAR(255),
    sort_order INTEGER
);

-- ==========================================
-- OMS (Order Management System)
-- ==========================================

CREATE TABLE IF NOT EXISTS oms_cart_item (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    product_sku_id BIGINT,
    member_id BIGINT,
    quantity INTEGER,
    price NUMERIC(19,2),
    product_pic VARCHAR(255),
    product_name VARCHAR(255),
    product_sub_title VARCHAR(255),
    product_sku_code VARCHAR(255),
    member_nickname VARCHAR(255),
    create_date TIMESTAMP,
    modify_date TIMESTAMP,
    delete_status INTEGER,
    product_category_id BIGINT,
    product_brand VARCHAR(255),
    product_sn VARCHAR(255),
    product_attr VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oms_company_address (
    id BIGSERIAL PRIMARY KEY,
    address_name VARCHAR(255),
    send_status INTEGER,
    receive_status INTEGER,
    name VARCHAR(255),
    phone VARCHAR(255),
    province VARCHAR(255),
    city VARCHAR(255),
    region VARCHAR(255),
    detail_address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oms_order (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    coupon_id BIGINT,
    order_sn VARCHAR(255),
    create_time TIMESTAMP,
    member_username VARCHAR(255),
    total_amount NUMERIC(19,2),
    pay_amount NUMERIC(19,2),
    freight_amount NUMERIC(19,2),
    promotion_amount NUMERIC(19,2),
    integration_amount NUMERIC(19,2),
    coupon_amount NUMERIC(19,2),
    discount_amount NUMERIC(19,2),
    pay_type INTEGER,
    source_type INTEGER,
    status INTEGER,
    order_type INTEGER,
    delivery_company VARCHAR(255),
    delivery_sn VARCHAR(255),
    auto_confirm_day INTEGER,
    reward_points INTEGER,
    experience_points INTEGER,
    promotion_info VARCHAR(255),
    bill_type INTEGER,
    bill_header VARCHAR(255),
    bill_content VARCHAR(255),
    bill_receiver_phone VARCHAR(255),
    bill_receiver_email VARCHAR(255),
    receiver_name VARCHAR(255),
    receiver_phone VARCHAR(255),
    receiver_post_code VARCHAR(255),
    receiver_province VARCHAR(255),
    receiver_city VARCHAR(255),
    receiver_region VARCHAR(255),
    receiver_detail_address VARCHAR(255),
    note VARCHAR(255),
    confirm_status INTEGER,
    delete_status INTEGER,
    use_integration INTEGER,
    payment_id VARCHAR(255),
    payment_time TIMESTAMP,
    delivery_time TIMESTAMP,
    receive_time TIMESTAMP,
    comment_time TIMESTAMP,
    modify_time TIMESTAMP,
    vendor_id BIGINT
);

CREATE TABLE IF NOT EXISTS oms_order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT,
    order_sn VARCHAR(255),
    product_id BIGINT,
    product_pic VARCHAR(255),
    product_name VARCHAR(255),
    product_brand VARCHAR(255),
    product_sn VARCHAR(255),
    product_price NUMERIC(19,2),
    product_quantity INTEGER,
    product_sku_id BIGINT,
    product_sku_code VARCHAR(255),
    product_category_id BIGINT,
    promotion_name VARCHAR(255),
    promotion_amount NUMERIC(19,2),
    coupon_amount NUMERIC(19,2),
    integration_amount NUMERIC(19,2),
    real_amount NUMERIC(19,2),
    gift_integration INTEGER,
    gift_growth INTEGER,
    product_attr VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oms_order_operate_history (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT,
    operate_man VARCHAR(255),
    create_time TIMESTAMP,
    order_status INTEGER,
    note VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oms_order_return_apply (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT,
    company_address_id BIGINT,
    product_id BIGINT,
    order_sn VARCHAR(255),
    create_time TIMESTAMP,
    member_username VARCHAR(255),
    return_amount NUMERIC(19,2),
    return_name VARCHAR(255),
    return_phone VARCHAR(255),
    status INTEGER,
    handle_time TIMESTAMP,
    product_pic VARCHAR(255),
    product_name VARCHAR(255),
    product_brand VARCHAR(255),
    product_attr VARCHAR(255),
    product_count INTEGER,
    product_price NUMERIC(19,2),
    product_real_price NUMERIC(19,2),
    reason VARCHAR(255),
    description VARCHAR(255),
    proof_pics VARCHAR(255),
    handle_note VARCHAR(255),
    handle_man VARCHAR(255),
    receive_man VARCHAR(255),
    receive_time TIMESTAMP,
    receive_note VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oms_order_return_reason (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    sort INTEGER,
    status INTEGER,
    create_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS oms_order_setting (
    id BIGSERIAL PRIMARY KEY,
    flash_order_overtime INTEGER,
    normal_order_overtime INTEGER,
    confirm_overtime INTEGER,
    finish_overtime INTEGER,
    comment_overtime INTEGER
);

CREATE TABLE IF NOT EXISTS oms_payment_transaction (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount NUMERIC(38, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    webhook_payload JSONB,
    create_time TIMESTAMP DEFAULT NOW(),
    CONSTRAINT uk_oms_payment_transaction_id UNIQUE (transaction_id)
);

-- ==========================================
-- UMS (User Management System)
-- ==========================================

CREATE TABLE IF NOT EXISTS ums_admin (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    icon VARCHAR(255),
    email VARCHAR(255),
    nick_name VARCHAR(255),
    note VARCHAR(255),
    create_time TIMESTAMP,
    login_time TIMESTAMP,
    status INTEGER,
    vendor_id BIGINT
);

CREATE TABLE IF NOT EXISTS ums_admin_login_log (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT,
    create_time TIMESTAMP,
    ip VARCHAR(255),
    address VARCHAR(255),
    user_agent VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ums_admin_role_relation (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT,
    role_id BIGINT
);

CREATE TABLE IF NOT EXISTS ums_member (
    id BIGSERIAL PRIMARY KEY,
    member_level_id BIGINT,
    username VARCHAR(255),
    password VARCHAR(255),
    nickname VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    status INTEGER,
    create_time TIMESTAMP,
    icon VARCHAR(255),
    gender INTEGER,
    birthday TIMESTAMP,
    personalized_signature VARCHAR(255),
    source_type INTEGER,
    reward_points INTEGER,
    experience_points INTEGER,
    bonus_draws_remaining INTEGER,
    lifetime_reward_points INTEGER
);

CREATE TABLE IF NOT EXISTS ums_member_level (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    growth_point INTEGER,
    default_status INTEGER,
    free_shipping_threshold NUMERIC(19,2),
    review_reward_xp INTEGER,
    has_free_shipping_perk INTEGER,
    can_earn_login_rewards INTEGER,
    has_review_privilege INTEGER,
    has_promotion_privilege INTEGER,
    has_vip_pricing INTEGER,
    has_birthday_privilege INTEGER,
    note VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ums_member_login_log (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    create_time TIMESTAMP,
    ip VARCHAR(255),
    city VARCHAR(255),
    login_type INTEGER,
    province VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ums_member_product_category_relation (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    product_category_id BIGINT
);

CREATE TABLE IF NOT EXISTS ums_member_receive_address (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT,
    name VARCHAR(255),
    phone_number VARCHAR(255),
    default_status INTEGER,
    post_code VARCHAR(255),
    province VARCHAR(255),
    city VARCHAR(255),
    region VARCHAR(255),
    detail_address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ums_menu (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    create_time TIMESTAMP,
    title VARCHAR(255),
    level INTEGER,
    sort INTEGER,
    name VARCHAR(255),
    icon VARCHAR(255),
    hidden INTEGER
);

CREATE TABLE IF NOT EXISTS ums_resource (
    id BIGSERIAL PRIMARY KEY,
    create_time TIMESTAMP,
    name VARCHAR(255),
    url VARCHAR(255),
    description VARCHAR(255),
    category_id BIGINT
);

CREATE TABLE IF NOT EXISTS ums_resource_category (
    id BIGSERIAL PRIMARY KEY,
    create_time TIMESTAMP,
    name VARCHAR(255),
    sort INTEGER
);

CREATE TABLE IF NOT EXISTS ums_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    admin_count INTEGER,
    create_time TIMESTAMP,
    status INTEGER,
    sort INTEGER
);

CREATE TABLE IF NOT EXISTS ums_role_menu_relation (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT,
    menu_id BIGINT
);

CREATE TABLE IF NOT EXISTS ums_role_resource_relation (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT,
    resource_id BIGINT
);

-- ==========================================
-- SMS (Sales Management System)
-- ==========================================

CREATE TABLE IF NOT EXISTS sms_coupon (
    id BIGSERIAL PRIMARY KEY,
    type INTEGER,
    name VARCHAR(255),
    platform INTEGER,
    count INTEGER,
    amount NUMERIC(19,2),
    per_limit INTEGER,
    min_point NUMERIC(19,2),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    use_type INTEGER,
    note VARCHAR(255),
    publish_count INTEGER,
    use_count INTEGER,
    receive_count INTEGER,
    enable_time TIMESTAMP,
    code VARCHAR(255),
    member_level INTEGER
);

CREATE TABLE IF NOT EXISTS sms_coupon_history (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT,
    member_id BIGINT,
    coupon_code VARCHAR(255),
    member_nickname VARCHAR(255),
    get_type INTEGER,
    create_time TIMESTAMP,
    use_status INTEGER,
    use_time TIMESTAMP,
    order_id BIGINT,
    order_sn VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sms_coupon_product_category_relation (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT,
    product_category_id BIGINT,
    product_category_name VARCHAR(255),
    parent_category_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sms_coupon_product_relation (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT,
    product_id BIGINT,
    product_name VARCHAR(255),
    product_sn VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sys_banner (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    type INTEGER,
    pic VARCHAR(255),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status INTEGER,
    click_count INTEGER,
    order_count INTEGER,
    url VARCHAR(255),
    note VARCHAR(255),
    sort INTEGER
);

-- ==========================================
-- SYS (System & Events)
-- ==========================================

CREATE TABLE IF NOT EXISTS outbox_event (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    payload TEXT,
    status VARCHAR(255) NOT NULL,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP
);

-- ==========================================
-- Indexes
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_oms_order_create_time_status ON oms_order(create_time, status);
CREATE INDEX IF NOT EXISTS idx_oms_order_status ON oms_order(status);
CREATE INDEX IF NOT EXISTS idx_oms_order_return_status ON oms_order_return_apply(status);
CREATE INDEX IF NOT EXISTS idx_ums_member_create_time ON ums_member(create_time);
CREATE INDEX IF NOT EXISTS idx_pms_product_publish_status ON pms_product(publish_status, delete_status);
CREATE INDEX IF NOT EXISTS idx_oms_payment_order_id ON oms_payment_transaction(order_id);

