-- NexusEngine Database Seed Script
-- Run after Flyway V1 baseline: docker exec -i postgres psql -U postgres -d nexuscore < NexusCore/document/sql/seed.sql

-- Admin user (password: admin123)
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

-- Member level (using new column names)
INSERT INTO ums_member_level (id, name, growth_point, default_status, free_shipping_threshold, review_reward_xp, has_free_shipping_perk, can_earn_login_rewards, has_review_privilege, has_promotion_privilege, has_vip_pricing, has_birthday_privilege, note)
VALUES (4, 'Gold Member', 1000, 1, 199, 5, true, true, true, true, true, true, 'Default member level')
ON CONFLICT (id) DO NOTHING;

-- Customer member (password: admin123, using renamed columns)
INSERT INTO ums_member (id, username, password, phone, status, gender, icon, member_level_id, experience_points, reward_points, create_time)
VALUES (1, 'customer1', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', '+919876543210', 1, 1, NULL, 4, 0, 0, '2024-01-01 00:00:00')
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

-- Products (no price/stock/original_price columns — these are derived from pms_sku_stock via @Formula)
INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, sub_title, description, unit, weight, keywords, detail_title, vendor_id)
VALUES (1, 1, 1, 'iPhone 16 Pro', NULL, 'IP16PRO', 0, 1, 1, 1, 1, 0, 0, 'Latest Apple iPhone', 'The most advanced iPhone ever', 'piece', 200, 'iphone apple', 'iPhone 16 Pro Details', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, sub_title, description, unit, weight, keywords, detail_title, vendor_id)
VALUES (2, 2, 1, 'Samsung Galaxy S24 Ultra', NULL, 'SGS24U', 0, 1, 1, 1, 1, 0, 0, 'Samsung Flagship Phone', 'The ultimate Galaxy experience', 'piece', 195, 'samsung galaxy', 'Galaxy S24 Ultra Details', 2)
ON CONFLICT (id) DO NOTHING;

INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, sub_title, description, unit, weight, keywords, detail_title, vendor_id)
VALUES (3, 1, 1, 'MacBook Pro M4', NULL, 'MBP-M4', 0, 1, 1, 1, 1, 0, 0, 'Apple MacBook Pro', 'Supercharged by M4 chip', 'piece', 1600, 'macbook apple laptop', 'MacBook Pro M4 Details', 1)
ON CONFLICT (id) DO NOTHING;

-- SKU Stock (using sku_attributes instead of sp_data)
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sku_attributes)
VALUES (1, 1, 'IP16PRO-256', 89999, 50, 5, 0, NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sku_attributes)
VALUES (2, 1, 'IP16PRO-512', 99999, 30, 5, 0, NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sku_attributes)
VALUES (3, 2, 'SGS24U-256', 79999, 40, 5, 0, NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, sale, sku_attributes)
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

-- Home banners (using renamed table sys_banner)
INSERT INTO sys_banner (id, name, type, pic, start_time, end_time, status, click_count, order_count, url, note, sort)
VALUES (1, 'Summer Sale', 1, NULL, '2024-06-01 00:00:00', '2026-12-31 23:59:59', 1, 0, 0, '/product/1', 'Summer sale banner', 0)
ON CONFLICT (id) DO NOTHING;

-- Order settings
INSERT INTO oms_order_setting (id, flash_order_overtime, normal_order_overtime, confirm_overtime, finish_overtime, comment_overtime)
VALUES (1, 60, 120, 15, 7, 7) ON CONFLICT (id) DO NOTHING;
INSERT INTO ums_menu VALUES (1, 0, '2020-02-02 14:50:36', '商品', 0, 0, 'pms', 'product', 0);
INSERT INTO ums_menu VALUES (2, 1, '2020-02-02 14:51:50', '商品列表', 1, 0, 'product', 'product-list', 0);
INSERT INTO ums_menu VALUES (3, 1, '2020-02-02 14:52:44', '添加商品', 1, 0, 'addProduct', 'product-add', 0);
INSERT INTO ums_menu VALUES (4, 1, '2020-02-02 14:53:51', '商品分类', 1, 0, 'productCate', 'product-cate', 0);
INSERT INTO ums_menu VALUES (5, 1, '2020-02-02 14:54:51', '商品类型', 1, 0, 'productAttr', 'product-attr', 0);
INSERT INTO ums_menu VALUES (6, 1, '2020-02-02 14:56:29', '品牌管理', 1, 0, 'brand', 'product-brand', 0);
INSERT INTO ums_menu VALUES (7, 0, '2020-02-02 16:54:07', '订单', 0, 0, 'oms', 'order', 0);
INSERT INTO ums_menu VALUES (8, 7, '2020-02-02 16:55:18', '订单列表', 1, 0, 'order', 'product-list', 0);
INSERT INTO ums_menu VALUES (9, 7, '2020-02-02 16:56:46', '订单设置', 1, 0, 'orderSetting', 'order-setting', 0);
INSERT INTO ums_menu VALUES (10, 7, '2020-02-02 16:57:39', '退货申请处理', 1, 0, 'returnApply', 'order-return', 0);
INSERT INTO ums_menu VALUES (11, 7, '2020-02-02 16:59:40', '退货原因设置', 1, 0, 'returnReason', 'order-return-reason', 0);
INSERT INTO ums_menu VALUES (12, 0, '2020-02-04 16:18:00', '营销', 0, 0, 'sms', 'sms', 0);
INSERT INTO ums_menu VALUES (13, 12, '2020-02-04 16:19:22', '秒杀活动列表', 1, 0, 'flash', 'sms-flash', 0);
INSERT INTO ums_menu VALUES (14, 12, '2020-02-04 16:20:16', '优惠券列表', 1, 0, 'coupon', 'sms-coupon', 0);
INSERT INTO ums_menu VALUES (16, 12, '2020-02-07 16:22:38', '品牌推荐', 1, 0, 'homeBrand', 'product-brand', 0);
INSERT INTO ums_menu VALUES (17, 12, '2020-02-07 16:23:14', '新品推荐', 1, 0, 'homeNew', 'sms-new', 0);
INSERT INTO ums_menu VALUES (18, 12, '2020-02-07 16:26:38', '人气推荐', 1, 0, 'homeHot', 'sms-hot', 0);
INSERT INTO ums_menu VALUES (19, 12, '2020-02-07 16:28:16', '专题推荐', 1, 0, 'homeSubject', 'sms-subject', 0);
INSERT INTO ums_menu VALUES (20, 12, '2020-02-07 16:28:42', '广告列表', 1, 0, 'homeAdvertise', 'sms-ad', 0);
INSERT INTO ums_menu VALUES (21, 0, '2020-02-07 16:29:13', '权限', 0, 0, 'ums', 'ums', 0);
INSERT INTO ums_menu VALUES (22, 21, '2020-02-07 16:29:51', '用户列表', 1, 0, 'admin', 'ums-admin', 0);
INSERT INTO ums_menu VALUES (23, 21, '2020-02-07 16:30:13', '角色列表', 1, 0, 'role', 'ums-role', 0);
INSERT INTO ums_menu VALUES (24, 21, '2020-02-07 16:30:53', '菜单列表', 1, 0, 'menu', 'ums-menu', 0);
INSERT INTO ums_menu VALUES (25, 21, '2020-02-07 16:31:13', '资源列表', 1, 0, 'resource', 'ums-resource', 0);
INSERT INTO ums_resource VALUES (1, '2020-02-04 17:04:55', '商品品牌管理', '/brand/**', NULL, 1);
INSERT INTO ums_resource VALUES (2, '2020-02-04 17:05:35', '商品属性分类管理', '/productAttribute/category/**', NULL, 1);
INSERT INTO ums_resource VALUES (3, '2020-02-04 17:06:13', '商品属性管理', '/productAttribute/**', NULL, 1);
INSERT INTO ums_resource VALUES (4, '2020-02-04 17:07:15', '商品分类管理', '/productCategory/**', NULL, 1);
INSERT INTO ums_resource VALUES (5, '2020-02-04 17:09:16', '商品管理', '/product/**', NULL, 1);
INSERT INTO ums_resource VALUES (6, '2020-02-04 17:09:53', '商品库存管理', '/sku/**', NULL, 1);
INSERT INTO ums_resource VALUES (8, '2020-02-05 14:43:37', '订单管理', '/order/**', '', 2);
INSERT INTO ums_resource VALUES (9, '2020-02-05 14:44:22', ' 订单退货申请管理', '/returnApply/**', '', 2);
INSERT INTO ums_resource VALUES (10, '2020-02-05 14:45:08', '退货原因管理', '/returnReason/**', '', 2);
INSERT INTO ums_resource VALUES (11, '2020-02-05 14:45:43', '订单设置管理', '/orderSetting/**', '', 2);
INSERT INTO ums_resource VALUES (12, '2020-02-05 14:46:23', '收货地址管理', '/companyAddress/**', '', 2);
INSERT INTO ums_resource VALUES (13, '2020-02-07 16:37:22', '优惠券管理', '/coupon/**', '', 3);
INSERT INTO ums_resource VALUES (14, '2020-02-07 16:37:59', '优惠券领取记录管理', '/couponHistory/**', '', 3);
INSERT INTO ums_resource VALUES (15, '2020-02-07 16:38:28', '限时购活动管理', '/flash/**', '', 3);
INSERT INTO ums_resource VALUES (16, '2020-02-07 16:38:59', '限时购商品关系管理', '/flashProductRelation/**', '', 3);
INSERT INTO ums_resource VALUES (17, '2020-02-07 16:39:22', '限时购场次管理', '/flashSession/**', '', 3);
INSERT INTO ums_resource VALUES (18, '2020-02-07 16:40:07', '首页轮播广告管理', '/home/advertise/**', '', 3);
INSERT INTO ums_resource VALUES (19, '2020-02-07 16:40:34', '首页品牌管理', '/home/brand/**', '', 3);
INSERT INTO ums_resource VALUES (20, '2020-02-07 16:41:06', '首页新品管理', '/home/newProduct/**', '', 3);
INSERT INTO ums_resource VALUES (21, '2020-02-07 16:42:16', '首页人气推荐管理', '/home/recommendProduct/**', '', 3);
INSERT INTO ums_resource VALUES (22, '2020-02-07 16:42:48', '首页专题推荐管理', '/home/recommendSubject/**', '', 3);
INSERT INTO ums_resource VALUES (23, '2020-02-07 16:44:56', ' 商品优选管理', '/prefrenceArea/**', '', 5);
INSERT INTO ums_resource VALUES (24, '2020-02-07 16:45:39', '商品专题管理', '/subject/**', '', 5);
INSERT INTO ums_resource VALUES (25, '2020-02-07 16:47:34', '后台用户管理', '/admin/**', '', 4);
INSERT INTO ums_resource VALUES (26, '2020-02-07 16:48:24', '后台用户角色管理', '/role/**', '', 4);
INSERT INTO ums_resource VALUES (27, '2020-02-07 16:48:48', '后台菜单管理', '/menu/**', '', 4);
INSERT INTO ums_resource VALUES (28, '2020-02-07 16:49:18', '后台资源分类管理', '/resourceCategory/**', '', 4);
INSERT INTO ums_resource VALUES (29, '2020-02-07 16:49:45', '后台资源管理', '/resource/**', '', 4);
INSERT INTO ums_resource VALUES (30, '2020-09-19 15:47:57', '会员等级管理', '/memberLevel/**', '', 7);
INSERT INTO ums_resource VALUES (31, '2020-09-19 15:51:29', '获取登录用户信息', '/admin/info', '用户登录必配', 4);
INSERT INTO ums_resource VALUES (32, '2020-09-19 15:53:34', '用户登出', '/admin/logout', '用户登出必配', 4);
INSERT INTO ums_resource_category VALUES (1, '2020-02-05 10:21:44', '商品模块', 0);
INSERT INTO ums_resource_category VALUES (2, '2020-02-05 10:22:34', '订单模块', 0);
INSERT INTO ums_resource_category VALUES (3, '2020-02-05 10:22:48', '营销模块', 0);
INSERT INTO ums_resource_category VALUES (4, '2020-02-05 10:23:04', '权限模块', 0);
INSERT INTO ums_resource_category VALUES (5, '2020-02-07 16:34:27', '内容模块', 0);
INSERT INTO ums_resource_category VALUES (7, '2020-09-19 15:49:08', '其他模块', 0);
DELETE FROM ums_role_menu_relation WHERE role_id = 1; INSERT INTO ums_role_menu_relation (role_id, menu_id) SELECT 1, id FROM ums_menu; DELETE FROM ums_role_resource_relation WHERE role_id = 1; INSERT INTO ums_role_resource_relation (role_id, resource_id) SELECT 1, id FROM ums_resource;
