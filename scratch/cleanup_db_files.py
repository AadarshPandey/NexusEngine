import os
import re

v1_path = '/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql'
with open(v1_path, 'r') as f:
    v1_content = f.read()

# pms_member_price
v1_content = re.sub(r'CREATE TABLE IF NOT EXISTS pms_member_price \([^;]+;\n', '', v1_content)
# ums_member_level
v1_content = re.sub(r'CREATE TABLE IF NOT EXISTS ums_member_level \([^;]+;\n', '', v1_content)

# sms_coupon: drop member_level
v1_content = re.sub(r',\s+member_level INTEGER\n', '\n', v1_content)

# pms_product: drop gift_growth
v1_content = re.sub(r'\s+gift_growth INTEGER,\n', '\n', v1_content)
v1_content = v1_content.replace('gift_point INTEGER,', 'gift_points INTEGER,')

# pms_product_operate_log
v1_content = v1_content.replace('gift_point_old INTEGER,', 'gift_points_old INTEGER,')
v1_content = v1_content.replace('gift_point_new INTEGER,', 'gift_points_new INTEGER,')

# oms_order_item
v1_content = re.sub(r'\s+gift_growth INTEGER,\n', '\n', v1_content)
v1_content = v1_content.replace('gift_integration INTEGER,', 'earned_points INTEGER,')
v1_content = v1_content.replace('integration_amount NUMERIC(19,2),', 'points_discount_amount NUMERIC(19,2),')
# Wait, oms_order also has integration_amount, let's just do replace all.
# ums_member
v1_content = re.sub(r'\s+member_level_id BIGINT,\n', '\n', v1_content)
v1_content = re.sub(r'\s+experience_points INTEGER,\n', '\n', v1_content)
# We can just replace the block for ums_member
v1_content = re.sub(r'CREATE TABLE IF NOT EXISTS ums_member \([^;]+;', '''CREATE TABLE IF NOT EXISTS ums_member (
    id BIGSERIAL PRIMARY KEY,
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
    points INTEGER,
    bonus_draws_remaining INTEGER,
    lifetime_points INTEGER
);''', v1_content)

# oms_order block
v1_content = re.sub(r'CREATE TABLE IF NOT EXISTS oms_order \([^;]+;', '''CREATE TABLE IF NOT EXISTS oms_order (
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
    points_discount_amount NUMERIC(19,2),
    coupon_amount NUMERIC(19,2),
    discount_amount NUMERIC(19,2),
    pay_type INTEGER,
    source_type INTEGER,
    status INTEGER,
    order_type INTEGER,
    delivery_company VARCHAR(255),
    delivery_sn VARCHAR(255),
    auto_confirm_day INTEGER,
    earned_points INTEGER,
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
    used_points INTEGER,
    payment_id VARCHAR(255),
    payment_time TIMESTAMP,
    delivery_time TIMESTAMP,
    receive_time TIMESTAMP,
    comment_time TIMESTAMP,
    modify_time TIMESTAMP,
    vendor_id BIGINT
);''', v1_content)

# oms_order_item block
v1_content = re.sub(r'CREATE TABLE IF NOT EXISTS oms_order_item \([^;]+;', '''CREATE TABLE IF NOT EXISTS oms_order_item (
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
    points_discount_amount NUMERIC(19,2),
    real_amount NUMERIC(19,2),
    earned_points INTEGER,
    product_attr VARCHAR(255)
);''', v1_content)

with open(v1_path, 'w') as f:
    f.write(v1_content)


# 2. Update seed.sql
seed_path = '/home/aadarsh/Documents/NexusEngine/NexusCore/document/sql/seed.sql'
with open(seed_path, 'r') as f:
    seed_content = f.read()

# Remove ums_member_level insert
seed_content = re.sub(r'-- Member level.*?ON CONFLICT \(id\) DO NOTHING;\n', '', seed_content, flags=re.DOTALL)

# Update ums_member insert
seed_content = seed_content.replace(
    'INSERT INTO ums_member (id, username, password, phone, status, gender, icon, member_level_id, experience_points, reward_points, create_time)',
    'INSERT INTO ums_member (id, username, password, phone, status, gender, icon, points, create_time)'
)
seed_content = seed_content.replace(
    "VALUES (1, 'customer1', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', '+919876543210', 1, 1, NULL, 4, 0, 0, '2024-01-01 00:00:00')",
    "VALUES (1, 'customer1', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', '+919876543210', 1, 1, NULL, 0, '2024-01-01 00:00:00')"
)

# Remove ums_resource for memberLevel
seed_content = re.sub(r"INSERT INTO ums_resource VALUES \(30, '2020-09-19 15:47:57', '会员等级管理', '/memberLevel/\*\*', '', 7\);\n", '', seed_content)

with open(seed_path, 'w') as f:
    f.write(seed_content)
