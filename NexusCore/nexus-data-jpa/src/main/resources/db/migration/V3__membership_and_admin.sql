-- V3: Membership Tiers and Brand Admins

-- Membership Levels
INSERT INTO ums_member_level (id, name, growth_point, default_status, free_shipping_threshold, review_reward_xp, has_free_shipping_perk, can_earn_login_rewards, has_review_privilege, has_promotion_privilege, has_vip_pricing, has_birthday_privilege, note)
VALUES 
(1, 'Bronze', 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 'Default Level'),
(2, 'Silver', 1000, 0, 499, 2, 1, 1, 1, 1, 1, 0, 'Silver Level'),
(3, 'Gold', 5000, 0, 299, 5, 1, 1, 1, 1, 1, 1, 'Gold Level'),
(4, 'Platinum', 15000, 0, 0, 10, 1, 1, 1, 1, 1, 1, 'Platinum Level'),
(5, 'Diamond', 50000, 0, 0, 20, 1, 1, 1, 1, 1, 1, 'Diamond Level')
ON CONFLICT DO NOTHING;

SELECT setval('ums_member_level_id_seq', 6);

-- Brand Admins
-- Assuming admin 1 and 2 exist from seed
INSERT INTO ums_admin (id, username, password, icon, email, nick_name, note, create_time, status, vendor_id)
VALUES 
(3, 'samsung_admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', NULL, 'samsung@nexusengine.com', 'Samsung Admin', 'Brand Admin', CURRENT_TIMESTAMP, 1, 2),
(4, 'nike_admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', NULL, 'nike@nexusengine.com', 'Nike Admin', 'Brand Admin', CURRENT_TIMESTAMP, 1, 3)
ON CONFLICT DO NOTHING;

SELECT setval('ums_admin_id_seq', 5);

-- Roles
INSERT INTO ums_role (id, name, description, admin_count, create_time, status, sort)
VALUES 
(2, 'Product Manager', 'Manage products for a specific brand', 2, CURRENT_TIMESTAMP, 1, 1),
(3, 'Order Manager', 'Manage orders', 0, CURRENT_TIMESTAMP, 1, 2)
ON CONFLICT DO NOTHING;

SELECT setval('ums_role_id_seq', 4);

-- Map admins to Product Manager role
INSERT INTO ums_admin_role_relation (admin_id, role_id)
VALUES 
(3, 2),
(4, 2)
ON CONFLICT DO NOTHING;

