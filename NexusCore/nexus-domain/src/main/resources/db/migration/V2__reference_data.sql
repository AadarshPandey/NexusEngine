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

-- 4. Ordering Return Reasons
INSERT INTO ordering.return_reason (id, label, display_order, is_active) VALUES
(1, 'Defective or damaged product', 1, true),
(2, 'Wrong item sent', 2, true),
(3, 'Changed mind / Not needed', 3, true),
(4, 'Product quality not as expected', 4, true)
ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, is_active = EXCLUDED.is_active;

-- 5. Ordering Singleton Order Policy (Row ID = 1)
INSERT INTO ordering.order_policy (id, payment_timeout_min, auto_confirm_receipt_days, auto_complete_days, review_window_days)
VALUES (1, 120, 15, 7, 7)
ON CONFLICT (id) DO UPDATE SET
    payment_timeout_min = EXCLUDED.payment_timeout_min,
    auto_confirm_receipt_days = EXCLUDED.auto_confirm_receipt_days,
    auto_complete_days = EXCLUDED.auto_complete_days,
    review_window_days = EXCLUDED.review_window_days;

-- 6. Synchronize Identity Sequences
SELECT setval(pg_get_serial_sequence('iam.role', 'id'), COALESCE((SELECT MAX(id) FROM iam.role), 1));
SELECT setval(pg_get_serial_sequence('iam.admin_user', 'id'), COALESCE((SELECT MAX(id) FROM iam.admin_user), 1));
SELECT setval(pg_get_serial_sequence('iam.admin_user_role', 'id'), COALESCE((SELECT MAX(id) FROM iam.admin_user_role), 1));
SELECT setval(pg_get_serial_sequence('ordering.return_reason', 'id'), COALESCE((SELECT MAX(id) FROM ordering.return_reason), 1));
