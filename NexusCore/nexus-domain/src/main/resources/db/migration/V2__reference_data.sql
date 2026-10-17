-- =====================================================================
-- NexusCore Reference Data Migration (V2__reference_data.sql)
-- Seeds IAM roles, permissions, console menus, relations,
-- return reasons, default order policy, and admin user credentials.
-- =====================================================================

-- 1. IAM Roles
INSERT INTO iam.role (id, name, description, is_active, display_order) VALUES
(1, 'Super Admin', 'Has all system permissions and unrestricted access', true, 0),
(2, 'Product Manager', 'Manage products and catalog for brands', true, 1),
(3, 'Order Manager', 'Manage customer orders, returns, and shipments', true, 2)
ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    description = EXCLUDED.description,
    is_active = EXCLUDED.is_active;

-- 2. IAM Admin Users (Demo password: macro123 / $2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO)
INSERT INTO iam.admin_user (id, username, password_hash, avatar_url, email, nick_name, note, is_enabled) VALUES
(1, 'admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', 'http://localhost:4566/nexus-media/public/users/avatars/1/avatar.svg', 'admin@nexusengine.com', 'System Administrator', 'System Administrator', true),
(2, 'samsung_admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', 'http://localhost:4566/nexus-media/public/catalog/brands/86/logo.svg', 'samsung@nexusengine.com', 'Samsung Admin', 'Samsung Brand Administrator', true),
(3, 'nike_admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', 'http://localhost:4566/nexus-media/public/catalog/brands/62/logo.svg', 'nike@nexusengine.com', 'Nike Admin', 'Nike Brand Administrator', true)
ON CONFLICT (id) DO UPDATE SET
    password_hash = EXCLUDED.password_hash,
    avatar_url = EXCLUDED.avatar_url,
    email = EXCLUDED.email;

-- 3. IAM Admin User Roles
INSERT INTO iam.admin_user_role (id, admin_id, role_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 2),
(4, 2, 3),
(5, 3, 3)
ON CONFLICT (admin_id, role_id) DO NOTHING;

-- 4. IAM Permission Groups
INSERT INTO iam.permission_group (id, name, display_order) VALUES
(1, 'Product module', 1),
(2, 'Order module', 2),
(3, 'Marketing module', 3),
(4, 'Permission module', 4),
(5, 'Content module', 5),
(6, 'Other modules', 6)
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, display_order = EXCLUDED.display_order;

-- 5. IAM Permissions
INSERT INTO iam.permission (id, permission_group_id, name, url_pattern, description) VALUES
(1, 1, 'All Product Management', '/product/**', 'Full catalog product management'),
(2, 2, 'All Order Management', '/order/**', 'Full order management'),
(3, 1, 'All Brand Management', '/brand/**', 'Brand management'),
(4, 1, 'Product Category Management', '/productCategory/**', 'Category tree and attributes'),
(5, 1, 'Product Creation and Updates', '/product/create, /product/update/**', 'Create or modify products'),
(6, 1, 'Product Inventory Management', '/sku/**', 'SKU inventory and stock adjustments'),
(7, 2, 'Order Management', '/order/status/**', 'Order status modification'),
(8, 2, 'Order Return Request Management', '/returnApply/**', 'Process return and refund requests'),
(9, 2, 'Return Reason Management', '/returnReason/**', 'Configure return reasons'),
(10, 2, 'Order Settings Management', '/orderSetting/**', 'Configure order timeout and policies'),
(11, 2, 'Shipping Address Management', '/companyAddress/**', 'Manage merchant fulfillment addresses'),
(12, 3, 'Coupon Management', '/coupon/**', 'Manage discount coupons'),
(13, 3, 'Coupon History Management', '/couponHistory/**', 'Coupon claim and usage records'),
(14, 3, 'Flash Sale Event Management', '/flash/**', 'Manage flash sale promotions'),
(15, 3, 'Flash Sale Product Relation', '/flashProductRelation/**', 'Map products to flash sale slots'),
(16, 3, 'Flash Sale Session Management', '/flashSession/**', 'Time slot definitions for flash sales'),
(17, 3, 'Promotional Banner Management', '/home/advertise/**', 'Homepage carousel banners'),
(18, 1, 'Brand Recommendation Management', '/home/brand/**', 'Featured brands on homepage'),
(19, 1, 'New Product Recommendation', '/home/newProduct/**', 'New arrival highlights'),
(20, 1, 'Popular Product Recommendation', '/home/recommendProduct/**', 'Popular products on homepage'),
(21, 5, 'Topic Recommendation Management', '/home/recommendSubject/**', 'Editorial subject curation'),
(22, 5, 'Product Selection Management', '/prefrenceArea/**', 'Curated preference sections'),
(23, 5, 'Product Topic Management', '/subject/**', 'Product topics and stories'),
(24, 4, 'Backend User Management', '/admin/**', 'Admin user accounts'),
(25, 4, 'Backend User Role Management', '/role/**', 'Role RBAC permissions'),
(26, 4, 'Backend Menu Management', '/menu/**', 'Console menu hierarchy'),
(27, 4, 'Backend Resource Category Management', '/resourceCategory/**', 'Permission groups'),
(28, 4, 'Backend Resource Management', '/resource/**', 'Permission endpoints'),
(29, 4, 'Membership Level Management', '/memberLevel/**', 'Customer loyalty tiers'),
(30, 4, 'Get Login User Info', '/admin/info', 'Current logged-in admin details'),
(31, 4, 'User Logout', '/admin/logout', 'Session revocation and logout'),
(32, 6, 'Dashboard Info', '/dashboard/**', 'Analytics and KPIs dashboard')
ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    url_pattern = EXCLUDED.url_pattern,
    description = EXCLUDED.description;

-- 6. IAM Console Menus
INSERT INTO iam.console_menu (id, parent_id, title, depth, display_order, route_name, icon, is_hidden) VALUES
(1, NULL, 'Catalog', 0, 0, 'catalog', 'catalog', false),
(2, 1, 'Product List', 1, 0, 'product', 'product-list', false),
(3, 1, 'Add Product', 1, 1, 'addProduct', 'product-add', false),
(4, 1, 'Product Category', 1, 2, 'productCate', 'product-cate', false),
(5, 1, 'Product Type', 1, 3, 'skuAttributes', 'product-attr', false),
(6, 1, 'Brand Management', 1, 4, 'brand', 'product-brand', false),
(7, NULL, 'Ordering', 0, 1, 'ordering', 'ordering', false),
(8, 7, 'Order List', 1, 0, 'order', 'product-list', false),
(9, 7, 'Order Settings', 1, 1, 'orderSetting', 'order-setting', false),
(10, 7, 'Return Request Processing', 1, 2, 'returnApply', 'order-return', false),
(11, 7, 'Return Reason Settings', 1, 3, 'returnReason', 'order-return-reasonId', false),
(12, NULL, 'Promotion', 0, 2, 'promotion', 'promotion', false),
(13, 12, 'Flash Sales', 1, 0, 'flash', 'sms-flash', false),
(14, 12, 'Coupons', 1, 1, 'coupon', 'sms-coupon', false),
(15, 12, 'Advertisements', 1, 2, 'homeAdvertise', 'sms-ad', false),
(16, NULL, 'Permissions', 0, 3, 'iam', 'iam', false),
(17, 16, 'Admin Users', 1, 0, 'admin', 'ums-admin', false),
(18, 16, 'Roles', 1, 1, 'role', 'ums-role', false),
(19, 16, 'Menus', 1, 2, 'menu', 'ums-menu', false),
(20, 16, 'Permissions', 1, 3, 'resource', 'ums-resource', false)
ON CONFLICT (id) DO UPDATE SET
    title = EXCLUDED.title,
    depth = EXCLUDED.depth,
    display_order = EXCLUDED.display_order,
    route_name = EXCLUDED.route_name,
    icon = EXCLUDED.icon;

-- 7. IAM Role-Menu Relations
-- Super Admin (Role 1): all menus
INSERT INTO iam.role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 7), (1, 8), (1, 9), (1, 10), (1, 11),
(1, 12), (1, 13), (1, 14), (1, 15),
(1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
-- Product Manager (Role 2): catalog menus
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6),
-- Order Manager (Role 3): ordering menus
(3, 7), (3, 8), (3, 9), (3, 10), (3, 11)
ON CONFLICT (role_id, menu_id) DO NOTHING;

-- 8. IAM Role-Permission Relations
-- Super Admin (Role 1): all permissions
INSERT INTO iam.role_permission (role_id, resource_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
(1, 21), (1, 22), (1, 23), (1, 24), (1, 25), (1, 26), (1, 27), (1, 28), (1, 29), (1, 30),
(1, 31), (1, 32),
-- Product Manager (Role 2): product, brand, category, info, dashboard
(2, 1), (2, 3), (2, 4), (2, 5), (2, 6), (2, 18), (2, 19), (2, 20), (2, 30), (2, 31), (2, 32),
-- Order Manager (Role 3): orders, returns, settings, info, dashboard
(3, 2), (3, 7), (3, 8), (3, 9), (3, 10), (3, 11), (3, 30), (3, 31), (3, 32)
ON CONFLICT (role_id, resource_id) DO NOTHING;

-- 9. Ordering Return Reasons
INSERT INTO ordering.return_reason (id, label, display_order, is_active) VALUES
(1, 'Defective or damaged product', 1, true),
(2, 'Wrong item sent', 2, true),
(3, 'Changed mind / Not needed', 3, true),
(4, 'Product quality not as expected', 4, true)
ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, is_active = EXCLUDED.is_active;

-- 10. Ordering Singleton Order Policy (Row ID = 1)
INSERT INTO ordering.order_policy (id, flash_payment_timeout_min, payment_timeout_min, auto_confirm_receipt_days, auto_complete_days, review_window_days)
VALUES (1, 60, 120, 15, 7, 7)
ON CONFLICT (id) DO UPDATE SET
    flash_payment_timeout_min = EXCLUDED.flash_payment_timeout_min,
    payment_timeout_min = EXCLUDED.payment_timeout_min,
    auto_confirm_receipt_days = EXCLUDED.auto_confirm_receipt_days,
    auto_complete_days = EXCLUDED.auto_complete_days,
    review_window_days = EXCLUDED.review_window_days;

-- 11. Synchronize Identity Sequences
SELECT setval(pg_get_serial_sequence('iam.role', 'id'), COALESCE((SELECT MAX(id) FROM iam.role), 1));
SELECT setval(pg_get_serial_sequence('iam.admin_user', 'id'), COALESCE((SELECT MAX(id) FROM iam.admin_user), 1));
SELECT setval(pg_get_serial_sequence('iam.admin_user_role', 'id'), COALESCE((SELECT MAX(id) FROM iam.admin_user_role), 1));
SELECT setval(pg_get_serial_sequence('iam.permission_group', 'id'), COALESCE((SELECT MAX(id) FROM iam.permission_group), 1));
SELECT setval(pg_get_serial_sequence('iam.permission', 'id'), COALESCE((SELECT MAX(id) FROM iam.permission), 1));
SELECT setval(pg_get_serial_sequence('iam.console_menu', 'id'), COALESCE((SELECT MAX(id) FROM iam.console_menu), 1));
SELECT setval(pg_get_serial_sequence('iam.role_menu', 'id'), COALESCE((SELECT MAX(id) FROM iam.role_menu), 1));
SELECT setval(pg_get_serial_sequence('iam.role_permission', 'id'), COALESCE((SELECT MAX(id) FROM iam.role_permission), 1));
SELECT setval(pg_get_serial_sequence('ordering.return_reason', 'id'), COALESCE((SELECT MAX(id) FROM ordering.return_reason), 1));
