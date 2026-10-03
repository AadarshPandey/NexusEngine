
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_role_resource_relation_resource_id'
    ) THEN
        ALTER TABLE ums_role_resource_relation ADD CONSTRAINT fk_ums_role_resource_relation_resource_id FOREIGN KEY (resource_id) REFERENCES ums_resource(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_attribute_value_product_id'
    ) THEN
        ALTER TABLE pms_product_attribute_value ADD CONSTRAINT fk_pms_product_attribute_value_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_attribute_value_product_attribute_id'
    ) THEN
        ALTER TABLE pms_product_attribute_value ADD CONSTRAINT fk_pms_product_attribute_value_product_attribute_id FOREIGN KEY (product_attribute_id) REFERENCES pms_product_attribute(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_brand_relation_coupon_id'
    ) THEN
        ALTER TABLE sms_coupon_brand_relation ADD CONSTRAINT fk_sms_coupon_brand_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_brand_relation_brand_id'
    ) THEN
        ALTER TABLE sms_coupon_brand_relation ADD CONSTRAINT fk_sms_coupon_brand_relation_brand_id FOREIGN KEY (brand_id) REFERENCES pms_brand(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_product_relation_flash_promotion_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_product_relation ADD CONSTRAINT fk_sms_flash_promotion_product_relation_flash_promotion_id FOREIGN KEY (flash_promotion_id) REFERENCES sms_flash_promotion(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_product_relation_flash_promotion_session_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_product_relation ADD CONSTRAINT fk_sms_flash_promotion_product_relation_flash_promotion_session_id FOREIGN KEY (flash_promotion_session_id) REFERENCES sms_flash_promotion_session(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_product_relation_product_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_product_relation ADD CONSTRAINT fk_sms_flash_promotion_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_log_member_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_log_product_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_log_flash_promotion_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_flash_promotion_id FOREIGN KEY (flash_promotion_id) REFERENCES sms_flash_promotion(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_log_session_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_session_id FOREIGN KEY (session_id) REFERENCES sms_flash_promotion_session(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_flash_promotion_log_order_id'
    ) THEN
        ALTER TABLE sms_flash_promotion_log ADD CONSTRAINT fk_sms_flash_promotion_log_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ai_chat_conversation_member_id'
    ) THEN
        ALTER TABLE ai_chat_conversation ADD CONSTRAINT fk_ai_chat_conversation_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_role_menu_relation_role_id'
    ) THEN
        ALTER TABLE ums_role_menu_relation ADD CONSTRAINT fk_ums_role_menu_relation_role_id FOREIGN KEY (role_id) REFERENCES ums_role(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_role_menu_relation_menu_id'
    ) THEN
        ALTER TABLE ums_role_menu_relation ADD CONSTRAINT fk_ums_role_menu_relation_menu_id FOREIGN KEY (menu_id) REFERENCES ums_menu(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_role_resource_relation_role_id'
    ) THEN
        ALTER TABLE ums_role_resource_relation ADD CONSTRAINT fk_ums_role_resource_relation_role_id FOREIGN KEY (role_id) REFERENCES ums_role(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_attribute_product_attribute_category_id'
    ) THEN
        ALTER TABLE pms_product_attribute ADD CONSTRAINT fk_pms_product_attribute_product_attribute_category_id FOREIGN KEY (product_attribute_category_id) REFERENCES pms_product_attribute_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_member_id'
    ) THEN
        ALTER TABLE oms_order ADD CONSTRAINT fk_oms_order_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_coupon_id'
    ) THEN
        ALTER TABLE oms_order ADD CONSTRAINT fk_oms_order_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_item_order_id'
    ) THEN
        ALTER TABLE oms_order_item ADD CONSTRAINT fk_oms_order_item_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_item_product_id'
    ) THEN
        ALTER TABLE oms_order_item ADD CONSTRAINT fk_oms_order_item_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_item_product_sku_id'
    ) THEN
        ALTER TABLE oms_order_item ADD CONSTRAINT fk_oms_order_item_product_sku_id FOREIGN KEY (product_sku_id) REFERENCES pms_sku_stock(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_item_product_category_id'
    ) THEN
        ALTER TABLE oms_order_item ADD CONSTRAINT fk_oms_order_item_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_cart_item_product_id'
    ) THEN
        ALTER TABLE oms_cart_item ADD CONSTRAINT fk_oms_cart_item_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_cart_item_product_sku_id'
    ) THEN
        ALTER TABLE oms_cart_item ADD CONSTRAINT fk_oms_cart_item_product_sku_id FOREIGN KEY (product_sku_id) REFERENCES pms_sku_stock(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_cart_item_member_id'
    ) THEN
        ALTER TABLE oms_cart_item ADD CONSTRAINT fk_oms_cart_item_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_cart_item_product_category_id'
    ) THEN
        ALTER TABLE oms_cart_item ADD CONSTRAINT fk_oms_cart_item_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_operate_history_order_id'
    ) THEN
        ALTER TABLE oms_order_operate_history ADD CONSTRAINT fk_oms_order_operate_history_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_return_apply_order_id'
    ) THEN
        ALTER TABLE oms_order_return_apply ADD CONSTRAINT fk_oms_order_return_apply_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_return_apply_company_address_id'
    ) THEN
        ALTER TABLE oms_order_return_apply ADD CONSTRAINT fk_oms_order_return_apply_company_address_id FOREIGN KEY (company_address_id) REFERENCES oms_company_address(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_order_return_apply_product_id'
    ) THEN
        ALTER TABLE oms_order_return_apply ADD CONSTRAINT fk_oms_order_return_apply_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_oms_payment_transaction_order_id'
    ) THEN
        ALTER TABLE oms_payment_transaction ADD CONSTRAINT fk_oms_payment_transaction_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_category_attribute_relation_product_category_id'
    ) THEN
        ALTER TABLE pms_product_category_attribute_relation ADD CONSTRAINT fk_pms_product_category_attribute_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_category_attribute_relation_product_attribute_id'
    ) THEN
        ALTER TABLE pms_product_category_attribute_relation ADD CONSTRAINT fk_pms_product_category_attribute_relation_product_attribute_id FOREIGN KEY (product_attribute_id) REFERENCES pms_product_attribute(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_comment_product_id'
    ) THEN
        ALTER TABLE pms_comment ADD CONSTRAINT fk_pms_comment_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_comment_reply_comment_id'
    ) THEN
        ALTER TABLE pms_comment_reply ADD CONSTRAINT fk_pms_comment_reply_comment_id FOREIGN KEY (comment_id) REFERENCES pms_comment(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_embedding_product_id'
    ) THEN
        ALTER TABLE pms_product_embedding ADD CONSTRAINT fk_pms_product_embedding_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_spend_discount_product_id'
    ) THEN
        ALTER TABLE pms_product_spend_discount ADD CONSTRAINT fk_pms_product_spend_discount_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_category_parent_id'
    ) THEN
        ALTER TABLE pms_product_category ADD CONSTRAINT fk_pms_product_category_parent_id FOREIGN KEY (parent_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_brand_id'
    ) THEN
        ALTER TABLE pms_product ADD CONSTRAINT fk_pms_product_brand_id FOREIGN KEY (brand_id) REFERENCES pms_brand(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_product_category_id'
    ) THEN
        ALTER TABLE pms_product ADD CONSTRAINT fk_pms_product_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_product_attribute_category_id'
    ) THEN
        ALTER TABLE pms_product ADD CONSTRAINT fk_pms_product_product_attribute_category_id FOREIGN KEY (product_attribute_category_id) REFERENCES pms_product_attribute_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_volume_discount_product_id'
    ) THEN
        ALTER TABLE pms_product_volume_discount ADD CONSTRAINT fk_pms_product_volume_discount_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_gallery_product_id'
    ) THEN
        ALTER TABLE pms_product_gallery ADD CONSTRAINT fk_pms_product_gallery_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_audit_log_product_id'
    ) THEN
        ALTER TABLE pms_product_audit_log ADD CONSTRAINT fk_pms_product_audit_log_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_product_approval_history_product_id'
    ) THEN
        ALTER TABLE pms_product_approval_history ADD CONSTRAINT fk_pms_product_approval_history_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_review_product_id'
    ) THEN
        ALTER TABLE pms_review ADD CONSTRAINT fk_pms_review_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_review_member_id'
    ) THEN
        ALTER TABLE pms_review ADD CONSTRAINT fk_pms_review_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_review_parent_id'
    ) THEN
        ALTER TABLE pms_review ADD CONSTRAINT fk_pms_review_parent_id FOREIGN KEY (parent_id) REFERENCES pms_review(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_review_attachments_review_id'
    ) THEN
        ALTER TABLE pms_review_attachments ADD CONSTRAINT fk_pms_review_attachments_review_id FOREIGN KEY (review_id) REFERENCES pms_review(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_history_coupon_id'
    ) THEN
        ALTER TABLE sms_coupon_history ADD CONSTRAINT fk_sms_coupon_history_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_history_member_id'
    ) THEN
        ALTER TABLE sms_coupon_history ADD CONSTRAINT fk_sms_coupon_history_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_history_order_id'
    ) THEN
        ALTER TABLE sms_coupon_history ADD CONSTRAINT fk_sms_coupon_history_order_id FOREIGN KEY (order_id) REFERENCES oms_order(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_product_category_relation_coupon_id'
    ) THEN
        ALTER TABLE sms_coupon_product_category_relation ADD CONSTRAINT fk_sms_coupon_product_category_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_product_category_relation_product_category_id'
    ) THEN
        ALTER TABLE sms_coupon_product_category_relation ADD CONSTRAINT fk_sms_coupon_product_category_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_product_relation_coupon_id'
    ) THEN
        ALTER TABLE sms_coupon_product_relation ADD CONSTRAINT fk_sms_coupon_product_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES sms_coupon(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_sms_coupon_product_relation_product_id'
    ) THEN
        ALTER TABLE sms_coupon_product_relation ADD CONSTRAINT fk_sms_coupon_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_pms_sku_stock_product_id'
    ) THEN
        ALTER TABLE pms_sku_stock ADD CONSTRAINT fk_pms_sku_stock_product_id FOREIGN KEY (product_id) REFERENCES pms_product(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_admin_login_log_admin_id'
    ) THEN
        ALTER TABLE ums_admin_login_log ADD CONSTRAINT fk_ums_admin_login_log_admin_id FOREIGN KEY (admin_id) REFERENCES ums_admin(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_admin_role_relation_admin_id'
    ) THEN
        ALTER TABLE ums_admin_role_relation ADD CONSTRAINT fk_ums_admin_role_relation_admin_id FOREIGN KEY (admin_id) REFERENCES ums_admin(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_admin_role_relation_role_id'
    ) THEN
        ALTER TABLE ums_admin_role_relation ADD CONSTRAINT fk_ums_admin_role_relation_role_id FOREIGN KEY (role_id) REFERENCES ums_role(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_member_login_log_member_id'
    ) THEN
        ALTER TABLE ums_member_login_log ADD CONSTRAINT fk_ums_member_login_log_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_member_product_category_relation_member_id'
    ) THEN
        ALTER TABLE ums_member_product_category_relation ADD CONSTRAINT fk_ums_member_product_category_relation_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_member_product_category_relation_product_category_id'
    ) THEN
        ALTER TABLE ums_member_product_category_relation ADD CONSTRAINT fk_ums_member_product_category_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_menu_parent_id'
    ) THEN
        ALTER TABLE ums_menu ADD CONSTRAINT fk_ums_menu_parent_id FOREIGN KEY (parent_id) REFERENCES ums_menu(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_resource_category_id'
    ) THEN
        ALTER TABLE ums_resource ADD CONSTRAINT fk_ums_resource_category_id FOREIGN KEY (category_id) REFERENCES ums_resource_category(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = 'fk_ums_member_receive_address_member_id'
    ) THEN
        ALTER TABLE ums_member_receive_address ADD CONSTRAINT fk_ums_member_receive_address_member_id FOREIGN KEY (member_id) REFERENCES ums_member(id);
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore errors (e.g. invalid data)
END $$;