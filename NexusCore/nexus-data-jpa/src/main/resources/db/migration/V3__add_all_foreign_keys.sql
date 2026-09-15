-- V3__add_all_foreign_keys.sql

ALTER TABLE pms_member_price ADD CONSTRAINT fk_pms_member_price_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE ums_member_receive_address ADD CONSTRAINT fk_ums_member_receive_address_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE ums_role_permission_relation ADD CONSTRAINT fk_ums_role_permission_relation_role_id FOREIGN KEY (role_id) REFERENCES ums_role(id) ON DELETE CASCADE;

ALTER TABLE pms_product_full_reduction ADD CONSTRAINT fk_pms_product_full_reduction_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE cms_prefrence_area_product_relation ADD CONSTRAINT fk_cms_prefrence_area_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE ums_role_menu_relation ADD CONSTRAINT fk_ums_role_menu_relation_role_id FOREIGN KEY (role_id) REFERENCES ums_role(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_product_relation ADD CONSTRAINT fk_sms_coupon_product_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_product_relation ADD CONSTRAINT fk_sms_coupon_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE sms_flash_promotion_product_relation ADD CONSTRAINT fk_sms_flash_promotion_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE oms_order ADD CONSTRAINT fk_oms_order_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE oms_order ADD CONSTRAINT fk_oms_order_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id) ON DELETE CASCADE;

ALTER TABLE pms_product_attribute_value ADD CONSTRAINT fk_pms_product_attribute_value_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE cms_subject_product_relation ADD CONSTRAINT fk_cms_subject_product_relation_subject_id FOREIGN KEY (subject_id) REFERENCES cms_subject(id) ON DELETE CASCADE;

ALTER TABLE cms_subject_product_relation ADD CONSTRAINT fk_cms_subject_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE oms_order_item ADD CONSTRAINT fk_oms_order_item_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE oms_order_item ADD CONSTRAINT fk_oms_order_item_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id) ON DELETE CASCADE;

ALTER TABLE ums_member_product_category_relation ADD CONSTRAINT fk_ums_member_product_category_relation_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE ums_member_product_category_relation ADD CONSTRAINT fk_ums_member_product_category_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id) ON DELETE CASCADE;

ALTER TABLE ums_member_statistics_info ADD CONSTRAINT fk_ums_member_statistics_info_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE ums_admin_login_log ADD CONSTRAINT fk_ums_admin_login_log_admin_id FOREIGN KEY (admin_id) REFERENCES ums_admin(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_product_category_relation ADD CONSTRAINT fk_sms_coupon_product_category_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_product_category_relation ADD CONSTRAINT fk_sms_coupon_product_category_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id) ON DELETE CASCADE;

ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE sms_home_recommend_product ADD CONSTRAINT fk_sms_home_recommend_product_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE oms_order_return_apply ADD CONSTRAINT fk_oms_order_return_apply_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id) ON DELETE CASCADE;

ALTER TABLE oms_order_return_apply ADD CONSTRAINT fk_oms_order_return_apply_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE cms_subject_comment ADD CONSTRAINT fk_cms_subject_comment_subject_id FOREIGN KEY (subject_id) REFERENCES cms_subject(id) ON DELETE CASCADE;

ALTER TABLE pms_product_category_attribute_relation ADD CONSTRAINT fk_pms_product_category_attribute_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id) ON DELETE CASCADE;

ALTER TABLE sms_home_recommend_subject ADD CONSTRAINT fk_sms_home_recommend_subject_subject_id FOREIGN KEY (subject_id) REFERENCES cms_subject(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_history ADD CONSTRAINT fk_sms_coupon_history_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_history ADD CONSTRAINT fk_sms_coupon_history_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE sms_coupon_history ADD CONSTRAINT fk_sms_coupon_history_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id) ON DELETE CASCADE;

ALTER TABLE ums_growth_change_history ADD CONSTRAINT fk_ums_growth_change_history_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE ums_member_login_log ADD CONSTRAINT fk_ums_member_login_log_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE pms_product_embedding ADD CONSTRAINT fk_pms_product_embedding_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE ums_member_member_tag_relation ADD CONSTRAINT fk_ums_member_member_tag_relation_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE oms_order_operate_history ADD CONSTRAINT fk_oms_order_operate_history_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id) ON DELETE CASCADE;

ALTER TABLE pms_comment ADD CONSTRAINT fk_pms_comment_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE sms_home_brand ADD CONSTRAINT fk_sms_home_brand_brand_id FOREIGN KEY (brand_id) REFERENCES pms_brand(id) ON DELETE CASCADE;

ALTER TABLE sms_home_new_product ADD CONSTRAINT fk_sms_home_new_product_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE oms_cart_item ADD CONSTRAINT fk_oms_cart_item_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE oms_cart_item ADD CONSTRAINT fk_oms_cart_item_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id) ON DELETE CASCADE;

ALTER TABLE pms_product_operate_log ADD CONSTRAINT fk_pms_product_operate_log_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE pms_product_ladder ADD CONSTRAINT fk_pms_product_ladder_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE pms_product_vertify_record ADD CONSTRAINT fk_pms_product_vertify_record_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE ums_integration_change_history ADD CONSTRAINT fk_ums_integration_change_history_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

ALTER TABLE ums_admin_permission_relation ADD CONSTRAINT fk_ums_admin_permission_relation_admin_id FOREIGN KEY (admin_id) REFERENCES ums_admin(id) ON DELETE CASCADE;

