-- Create admin user
INSERT INTO ums_admin (id, username, password, icon, email, nick_name, note, create_time, login_time, status)
VALUES (1, 'admin', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/nexus/images/20180607/timg.jpg', 'admin@macrozheng.com', 'System Administrator', 'System Administrator', '2018-10-08 13:32:47', '2019-04-20 12:43:33', 1) ON CONFLICT (id) DO UPDATE SET password = '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO';

-- Create role
INSERT INTO ums_role (id, name, description, admin_count, create_time, status, sort)
VALUES (1, 'Super Admin', 'Has all permissions', 0, '2018-09-30 15:46:11', 1, 0) ON CONFLICT (id) DO NOTHING;

-- Map admin to role
INSERT INTO ums_admin_role_relation (id, admin_id, role_id)
VALUES (1, 1, 1) ON CONFLICT (id) DO NOTHING;

-- Sample Product Category
INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description)
VALUES (1, 0, 'Electronics', 0, 100, 'piece', 1, 1, 0, 'icon-1', 'electronics', 'Electronic items') ON CONFLICT (id) DO NOTHING;
