-- V2__add_foreign_keys.sql
-- Add foreign keys to maintain referential integrity without modifying JPA entities

-- Admin and Role Relationships
ALTER TABLE ums_admin_role_relation 
    ADD CONSTRAINT fk_ums_admin_role_relation_admin_id 
    FOREIGN KEY (admin_id) REFERENCES ums_admin(id) ON DELETE CASCADE;

ALTER TABLE ums_admin_role_relation 
    ADD CONSTRAINT fk_ums_admin_role_relation_role_id 
    FOREIGN KEY (role_id) REFERENCES ums_role(id) ON DELETE CASCADE;

ALTER TABLE ums_role_resource_relation 
    ADD CONSTRAINT fk_ums_role_resource_relation_role_id 
    FOREIGN KEY (role_id) REFERENCES ums_role(id) ON DELETE CASCADE;

ALTER TABLE ums_role_resource_relation 
    ADD CONSTRAINT fk_ums_role_resource_relation_resource_id 
    FOREIGN KEY (resource_id) REFERENCES ums_resource(id) ON DELETE CASCADE;

-- Products and Categories
ALTER TABLE pms_product 
    ADD CONSTRAINT fk_pms_product_brand_id 
    FOREIGN KEY (brand_id) REFERENCES pms_brand(id) ON DELETE SET NULL;

ALTER TABLE pms_product 
    ADD CONSTRAINT fk_pms_product_category_id 
    FOREIGN KEY (product_category_id) REFERENCES pms_product_category(id) ON DELETE SET NULL;

ALTER TABLE pms_sku_stock 
    ADD CONSTRAINT fk_pms_sku_stock_product_id 
    FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

-- Orders and Cart
ALTER TABLE oms_order_item 
    ADD CONSTRAINT fk_oms_order_item_order_id 
    FOREIGN KEY (order_id) REFERENCES oms_order(id) ON DELETE CASCADE;

ALTER TABLE oms_cart_item 
    ADD CONSTRAINT fk_oms_cart_item_member_id 
    FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;

-- Reviews
ALTER TABLE pms_review 
    ADD CONSTRAINT fk_pms_review_product_id 
    FOREIGN KEY (product_id) REFERENCES pms_product(id) ON DELETE CASCADE;

ALTER TABLE pms_review 
    ADD CONSTRAINT fk_pms_review_member_id 
    FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE;
