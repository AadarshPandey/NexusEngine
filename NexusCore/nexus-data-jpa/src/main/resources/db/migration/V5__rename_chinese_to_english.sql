-- V5__rename_chinese_to_english.sql
-- Table Renames
ALTER TABLE pms_feight_template RENAME TO pms_freight_template;
ALTER TABLE pms_product_vertify_record RENAME TO pms_product_verify_record;
ALTER TABLE pms_comment_replay RENAME TO pms_comment_reply;
ALTER TABLE cms_prefrence_area RENAME TO cms_preference_area;
ALTER TABLE cms_prefrence_area_product_relation RENAME TO cms_preference_area_product_relation;

-- Column Renames - pms_freight_template
ALTER TABLE pms_freight_template RENAME COLUMN first_weight TO base_weight;
ALTER TABLE pms_freight_template RENAME COLUMN first_fee TO base_shipping_fee;
ALTER TABLE pms_freight_template RENAME COLUMN continue_weight TO incremental_weight_unit;
ALTER TABLE pms_freight_template RENAME COLUMN continme_fee TO incremental_fee;

-- Column Renames - pms_product
ALTER TABLE pms_product RENAME COLUMN feight_template_id TO freight_template_id;

-- Column Renames - ums_member_task, ums_member, ums_member_level
ALTER TABLE ums_member_task RENAME COLUMN intergration TO reward_points;
ALTER TABLE ums_member RENAME COLUMN integration TO reward_points;
ALTER TABLE ums_member RENAME COLUMN history_integration TO lifetime_reward_points;
ALTER TABLE ums_member RENAME COLUMN growth TO experience_points;
ALTER TABLE ums_member RENAME COLUMN luckey_count TO bonus_draws_remaining;
ALTER TABLE ums_member_task RENAME COLUMN growth TO experience_points;
ALTER TABLE ums_member_level RENAME COLUMN free_freight_point TO free_shipping_threshold;
ALTER TABLE ums_member_level RENAME COLUMN comment_growth_point TO review_reward_xp;
ALTER TABLE ums_member_level RENAME COLUMN priviledge_member_price TO has_vip_pricing;
ALTER TABLE ums_member_level RENAME COLUMN priviledge_free_freight TO has_free_shipping_perk;
ALTER TABLE ums_member_level RENAME COLUMN priviledge_sign_in TO can_earn_login_rewards;
ALTER TABLE ums_member_level RENAME COLUMN priviledge_comment TO has_review_privilege;
ALTER TABLE ums_member_level RENAME COLUMN priviledge_promotion TO has_promotion_privilege;
ALTER TABLE ums_member_level RENAME COLUMN priviledge_birthday TO has_birthday_privilege;

-- Column Renames - pms_product_verify_record
ALTER TABLE pms_product_verify_record RENAME COLUMN vertify_man TO reviewer_name;

-- Column Renames - pms_comment, pms_comment_reply
ALTER TABLE pms_comment RENAME COLUMN replay_count TO reply_count;
ALTER TABLE pms_comment RENAME COLUMN collect_couont TO likes_count;

-- Column Renames - ums_member_statistics_info
ALTER TABLE ums_member_statistics_info RENAME COLUMN collect_product_count TO saved_products_count;
ALTER TABLE ums_member_statistics_info RENAME COLUMN collect_subject_count TO saved_articles_count;
ALTER TABLE ums_member_statistics_info RENAME COLUMN collect_topic_count TO saved_topics_count;
ALTER TABLE ums_member_statistics_info RENAME COLUMN collect_comment_count TO liked_comments_count;

-- Column Renames - cms_preference_area_product_relation
ALTER TABLE cms_preference_area_product_relation RENAME COLUMN prefrence_area_id TO preference_area_id;

