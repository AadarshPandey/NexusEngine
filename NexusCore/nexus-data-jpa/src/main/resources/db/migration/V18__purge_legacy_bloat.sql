-- V18: Purge Legacy CMS and SMS Bloat Tables
-- These tables were part of the tightly-coupled FAANG monorepo and are no longer used by the application.
-- Dropping them completes Phase 4 of the Codebase Purge Plan.

DROP TABLE IF EXISTS cms_help CASCADE;
DROP TABLE IF EXISTS cms_help_category CASCADE;
DROP TABLE IF EXISTS cms_member_report CASCADE;
DROP TABLE IF EXISTS cms_preference_area CASCADE;
DROP TABLE IF EXISTS cms_preference_area_product_relation CASCADE;
DROP TABLE IF EXISTS cms_subject CASCADE;
DROP TABLE IF EXISTS cms_subject_category CASCADE;
DROP TABLE IF EXISTS cms_subject_comment CASCADE;
DROP TABLE IF EXISTS cms_subject_product_relation CASCADE;
DROP TABLE IF EXISTS cms_topic CASCADE;
DROP TABLE IF EXISTS cms_topic_category CASCADE;
DROP TABLE IF EXISTS cms_topic_comment CASCADE;

DROP TABLE IF EXISTS sms_flash_promotion CASCADE;
DROP TABLE IF EXISTS sms_flash_promotion_log CASCADE;
DROP TABLE IF EXISTS sms_flash_promotion_product_relation CASCADE;
DROP TABLE IF EXISTS sms_flash_promotion_session CASCADE;
DROP TABLE IF EXISTS sms_home_brand CASCADE;
DROP TABLE IF EXISTS sms_home_new_product CASCADE;
DROP TABLE IF EXISTS sms_home_recommend_product CASCADE;
DROP TABLE IF EXISTS sms_home_recommend_subject CASCADE;

-- Note: sms_coupon, sms_coupon_product_relation, sms_coupon_product_category_relation, sms_coupon_history
-- and sms_home_advertise are kept according to the Codebase Purge Plan.

