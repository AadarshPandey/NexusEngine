-- V11__cleanup_dummy_data.sql
-- Clean up all the "Sample text" garbage rows that were blindly injected by previous dummy data scripts

-- 1. Delete garbage users and admins
DELETE FROM ums_admin WHERE password ILIKE '%sample%';
DELETE FROM ums_member WHERE password ILIKE '%sample%' OR job ILIKE '%sample%';

-- 2. Truncate garbage logs and histories that don't need sensible data
TRUNCATE TABLE cms_member_report CASCADE;
TRUNCATE TABLE cms_subject_comment CASCADE;
TRUNCATE TABLE cms_topic CASCADE;
TRUNCATE TABLE cms_topic_comment CASCADE;
TRUNCATE TABLE ums_admin_login_log CASCADE;
TRUNCATE TABLE ums_member_login_log CASCADE;
TRUNCATE TABLE ums_growth_change_history CASCADE;
TRUNCATE TABLE ums_integration_change_history CASCADE;
TRUNCATE TABLE outbox_event CASCADE;

-- 3. Update sensible data for addresses and permissions
UPDATE oms_company_address SET 
  address_name = 'Nexus HQ', 
  detail_address = '123 Tech Boulevard, Silicon Valley' 
WHERE address_name ILIKE '%sample%';

UPDATE ums_member_receive_address SET 
  detail_address = '456 Innovation Drive, Suite 100' 
WHERE detail_address ILIKE '%sample%';

UPDATE ums_permission SET 
  uri = '/api/v1/resource', 
  value = 'read:resource' 
WHERE uri ILIKE '%sample%';
