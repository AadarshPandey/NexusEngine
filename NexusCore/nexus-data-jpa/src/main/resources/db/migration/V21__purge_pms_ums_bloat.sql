-- V21: The Deep Clean - Remove gamification bloat, redundant RBAC, and template typos

-- Drop PMS Typos and Redundant Media
DROP TABLE IF EXISTS pms_product_vertify_record CASCADE;
DROP TABLE IF EXISTS pms_feight_template CASCADE;
DROP TABLE IF EXISTS pms_comment_replay CASCADE;
DROP TABLE IF EXISTS pms_album CASCADE;
DROP TABLE IF EXISTS pms_album_pic CASCADE;

-- Drop UMS Gamification and Loyalty Bloat
DROP TABLE IF EXISTS ums_growth_change_history CASCADE;
DROP TABLE IF EXISTS ums_integration_change_history CASCADE;
DROP TABLE IF EXISTS ums_integration_consume_setting CASCADE;
DROP TABLE IF EXISTS ums_member_rule_setting CASCADE;
DROP TABLE IF EXISTS ums_member_task CASCADE;
DROP TABLE IF EXISTS ums_member_tag CASCADE;
DROP TABLE IF EXISTS ums_member_member_tag_relation CASCADE;
DROP TABLE IF EXISTS ums_member_statistics_info CASCADE;

-- Drop UMS Redundant Permission System (We rely on Menus/Resources)
DROP TABLE IF EXISTS ums_permission CASCADE;
DROP TABLE IF EXISTS ums_admin_permission_relation CASCADE;
DROP TABLE IF EXISTS ums_role_permission_relation CASCADE;
