-- NexusEngine Database Seed Script
-- Run after Hibernate creates the tables: docker exec -i postgres psql -U postgres -d nexuscore < NexusCore/document/sql/seed.sql

-- Admin user (password: macro123)
INSERT INTO ums_admin (id, username, password, icon, email, nick_name, note, create_time, login_time, status, vendor_id)
VALUES (1, 'admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', NULL, 'admin@nexusengine.com', 'System Administrator', 'System Administrator', '2018-10-08 13:32:47', '2019-04-20 12:43:33', 1, NULL)
ON CONFLICT (id) DO UPDATE SET password = EXCLUDED.password;

-- Apple Vendor Admin
INSERT INTO ums_admin (id, username, password, icon, email, nick_name, note, create_time, login_time, status, vendor_id)
VALUES (2, 'apple_admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', NULL, 'apple@nexusengine.com', 'Apple Administrator', 'Apple Administrator', '2018-10-08 13:32:47', '2019-04-20 12:43:33', 1, 1)
ON CONFLICT (id) DO UPDATE SET password = EXCLUDED.password;

-- Admin role
INSERT INTO ums_role (id, name, description, admin_count, create_time, status, sort)
VALUES (1, 'Super Admin', 'Has all permissions', 0, '2018-09-30 15:46:11', 1, 0)
ON CONFLICT (id) DO NOTHING;

-- Map admin to role
INSERT INTO ums_admin_role_relation (id, admin_id, role_id)
VALUES (1, 1, 1) ON CONFLICT (id) DO NOTHING;

-- Member level (required before member)
INSERT INTO ums_member_level (id, name, growth_point, default_status, free_freight_point, comment_growth_point, priviledge_free_freight, priviledge_sign_in, priviledge_comment, priviledge_promotion, priviledge_member_price, priviledge_birthday, note)
VALUES (4, 'Gold Member', 1000, 1, 199, 5, 1, 1, 1, 1, 1, 1, 'Default member level')
ON CONFLICT (id) DO NOTHING;

-- Customer member (password: macro123)
INSERT INTO ums_member (id, username, password, phone, status, gender, city, icon, member_level_id, growth, integration, create_time)
VALUES (1, 'customer1', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', '13800138000', 1, 1, 'Mumbai', NULL, 4, 0, 0, '2024-01-01 00:00:00')
ON CONFLICT (id) DO NOTHING;

-- Product categories
INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description)
VALUES (1, 0, 'Electronics', 0, 100, 'piece', 1, 1, 0, NULL, 'electronics', 'Electronic items')
ON CONFLICT (id) DO NOTHING;

INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description)
VALUES (2, 0, 'Clothing', 0, 50, 'piece', 1, 1, 1, NULL, 'clothing', 'Clothing items')
ON CONFLICT (id) DO NOTHING;

-- Brands
INSERT INTO pms_brand (id, name, first_letter, sort, factory_status, show_status, product_count, product_comment_count, logo, big_pic, brand_story)
VALUES (1, 'Apple', 'A', 0, 1, 1, 10, 0, NULL, NULL, 'Think Different')
ON CONFLICT (id) DO NOTHING;

INSERT INTO pms_brand (id, name, first_letter, sort, factory_status, show_status, product_count, product_comment_count, logo, big_pic, brand_story)
VALUES (2, 'Samsung', 'S', 0, 1, 1, 10, 0, NULL, NULL, 'Do What You Cant')
ON CONFLICT (id) DO NOTHING;

-- Products (prices in INR paisa)
INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommand_status, verify_status, sort, sale, price, sub_title, description, original_price, stock, unit, weight, keywords, detail_title, vendor_id)
VALUES (1, 1, 1, 'iPhone 16 Pro', NULL, 'IP16PRO', 0, 1, 1, 1, 1, 0, 0, 89999, 'Latest Apple iPhone', 'The most advanced iPhone ever', 99999, 100, 'piece', 200, 'iphone apple', 'iPhone 16 Pro Details', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommand_status, verify_status, sort, sale, price, sub_title, description, original_price, stock, unit, weight, keywords, detail_title, vendor_id)
VALUES (2, 2, 1, 'Samsung Galaxy S24 Ultra', NULL, 'SGS24U', 0, 1, 1, 1, 1, 0, 0, 79999, 'Samsung Flagship Phone', 'The ultimate Galaxy experience', 89999, 100, 'piece', 195, 'samsung galaxy', 'Galaxy S24 Ultra Details', 2)
ON CONFLICT (id) DO NOTHING;

INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommand_status, verify_status, sort, sale, price, sub_title, description, original_price, stock, unit, weight, keywords, detail_title, vendor_id)
VALUES (3, 1, 1, 'MacBook Pro M4', NULL, 'MBP-M4', 0, 1, 1, 1, 1, 0, 0, 149999, 'Apple MacBook Pro', 'Supercharged by M4 chip', 169999, 50, 'piece', 1600, 'macbook apple laptop', 'MacBook Pro M4 Details', 1)
ON CONFLICT (id) DO NOTHING;

-- SKU Stock
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sp_data)
VALUES (1, 1, 'IP16PRO-256', 89999, 50, 5, 0, NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sp_data)
VALUES (2, 1, 'IP16PRO-512', 99999, 30, 5, 0, NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sp_data)
VALUES (3, 2, 'SGS24U-256', 79999, 40, 5, 0, NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sp_data)
VALUES (4, 3, 'MBP-M4-512', 149999, 25, 3, 0, NULL) ON CONFLICT (id) DO NOTHING;

-- Resources for permissions
INSERT INTO ums_resource (id, create_time, name, url, description, category_id)
VALUES (1, '2020-02-04 17:04:55', 'All Product Management', '/product/**', NULL, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO ums_resource (id, create_time, name, url, description, category_id)
VALUES (2, '2020-02-04 17:05:35', 'All Order Management', '/order/**', NULL, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO ums_resource (id, create_time, name, url, description, category_id)
VALUES (3, '2020-02-04 17:06:13', 'All Brand Management', '/brand/**', NULL, 1) ON CONFLICT (id) DO NOTHING;

-- Role-resource relations
INSERT INTO ums_role_resource_relation (id, role_id, resource_id) VALUES (1, 1, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO ums_role_resource_relation (id, role_id, resource_id) VALUES (2, 1, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO ums_role_resource_relation (id, role_id, resource_id) VALUES (3, 1, 3) ON CONFLICT (id) DO NOTHING;

-- Home banners
INSERT INTO sms_home_advertise (id, name, type, pic, start_time, end_time, status, click_count, order_count, url, note, sort)
VALUES (1, 'Summer Sale', 1, NULL, '2024-06-01 00:00:00', '2026-12-31 23:59:59', 1, 0, 0, '/product/1', 'Summer sale banner', 0)
ON CONFLICT (id) DO NOTHING;

-- Order settings
INSERT INTO oms_order_setting (id, flash_order_overtime, normal_order_overtime, confirm_overtime, finish_overtime, comment_overtime)
VALUES (1, 60, 120, 15, 7, 7) ON CONFLICT (id) DO NOTHING;

-- AI Embeddings
CREATE TABLE IF NOT EXISTS pms_product_embedding (product_id BIGINT PRIMARY KEY, embedding vector(1536));
