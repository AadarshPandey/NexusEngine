-- Move tables to specific schemas

CREATE SCHEMA IF NOT EXISTS catalog;
CREATE SCHEMA IF NOT EXISTS orders;
CREATE SCHEMA IF NOT EXISTS users;
CREATE SCHEMA IF NOT EXISTS marketing;
CREATE SCHEMA IF NOT EXISTS ai;
CREATE SCHEMA IF NOT EXISTS system;
CREATE SCHEMA IF NOT EXISTS integration;
CREATE SCHEMA IF NOT EXISTS migration;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_brand'
    ) THEN
        ALTER TABLE public.pms_brand SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_brand' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_comment'
    ) THEN
        ALTER TABLE public.pms_comment SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_comment' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_comment_reply'
    ) THEN
        ALTER TABLE public.pms_comment_reply SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_comment_reply' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product'
    ) THEN
        ALTER TABLE public.pms_product SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_attribute'
    ) THEN
        ALTER TABLE public.pms_product_attribute SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_attribute' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_attribute_category'
    ) THEN
        ALTER TABLE public.pms_product_attribute_category SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_attribute_category' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_attribute_value'
    ) THEN
        ALTER TABLE public.pms_product_attribute_value SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_attribute_value' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_category'
    ) THEN
        ALTER TABLE public.pms_product_category SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_category' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_category_attribute_relation'
    ) THEN
        ALTER TABLE public.pms_product_category_attribute_relation SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_category_attribute_relation' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_full_reduction'
    ) THEN
        ALTER TABLE public.pms_product_full_reduction SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_full_reduction' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_ladder'
    ) THEN
        ALTER TABLE public.pms_product_ladder SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_ladder' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_media'
    ) THEN
        ALTER TABLE public.pms_product_media SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_media' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_operate_log'
    ) THEN
        ALTER TABLE public.pms_product_operate_log SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_operate_log' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_verify_record'
    ) THEN
        ALTER TABLE public.pms_product_verify_record SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_verify_record' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_review'
    ) THEN
        ALTER TABLE public.pms_review SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_review' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_review_media'
    ) THEN
        ALTER TABLE public.pms_review_media SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_review_media' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_sku_stock'
    ) THEN
        ALTER TABLE public.pms_sku_stock SET SCHEMA catalog;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_sku_stock' 
          AND n.nspname = 'catalog' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA catalog';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_cart_item'
    ) THEN
        ALTER TABLE public.oms_cart_item SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_cart_item' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_company_address'
    ) THEN
        ALTER TABLE public.oms_company_address SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_company_address' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_order'
    ) THEN
        ALTER TABLE public.oms_order SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_order' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_order_item'
    ) THEN
        ALTER TABLE public.oms_order_item SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_order_item' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_order_operate_history'
    ) THEN
        ALTER TABLE public.oms_order_operate_history SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_order_operate_history' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_order_return_apply'
    ) THEN
        ALTER TABLE public.oms_order_return_apply SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_order_return_apply' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_order_return_reason'
    ) THEN
        ALTER TABLE public.oms_order_return_reason SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_order_return_reason' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_order_setting'
    ) THEN
        ALTER TABLE public.oms_order_setting SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_order_setting' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='oms_payment_transaction'
    ) THEN
        ALTER TABLE public.oms_payment_transaction SET SCHEMA orders;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'oms_payment_transaction' 
          AND n.nspname = 'orders' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA orders';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_admin'
    ) THEN
        ALTER TABLE public.ums_admin SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_admin' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_admin_login_log'
    ) THEN
        ALTER TABLE public.ums_admin_login_log SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_admin_login_log' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_admin_role_relation'
    ) THEN
        ALTER TABLE public.ums_admin_role_relation SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_admin_role_relation' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_member'
    ) THEN
        ALTER TABLE public.ums_member SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_member' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_member_login_log'
    ) THEN
        ALTER TABLE public.ums_member_login_log SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_member_login_log' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_member_product_category_relation'
    ) THEN
        ALTER TABLE public.ums_member_product_category_relation SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_member_product_category_relation' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_member_receive_address'
    ) THEN
        ALTER TABLE public.ums_member_receive_address SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_member_receive_address' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_menu'
    ) THEN
        ALTER TABLE public.ums_menu SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_menu' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_resource'
    ) THEN
        ALTER TABLE public.ums_resource SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_resource' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_resource_category'
    ) THEN
        ALTER TABLE public.ums_resource_category SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_resource_category' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_role'
    ) THEN
        ALTER TABLE public.ums_role SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_role' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_role_menu_relation'
    ) THEN
        ALTER TABLE public.ums_role_menu_relation SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_role_menu_relation' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ums_role_resource_relation'
    ) THEN
        ALTER TABLE public.ums_role_resource_relation SET SCHEMA users;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ums_role_resource_relation' 
          AND n.nspname = 'users' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA users';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_coupon'
    ) THEN
        ALTER TABLE public.sms_coupon SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_coupon' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_coupon_brand_relation'
    ) THEN
        ALTER TABLE public.sms_coupon_brand_relation SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_coupon_brand_relation' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_coupon_history'
    ) THEN
        ALTER TABLE public.sms_coupon_history SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_coupon_history' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_coupon_product_category_relation'
    ) THEN
        ALTER TABLE public.sms_coupon_product_category_relation SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_coupon_product_category_relation' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_coupon_product_relation'
    ) THEN
        ALTER TABLE public.sms_coupon_product_relation SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_coupon_product_relation' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_flash_promotion'
    ) THEN
        ALTER TABLE public.sms_flash_promotion SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_flash_promotion' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_flash_promotion_log'
    ) THEN
        ALTER TABLE public.sms_flash_promotion_log SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_flash_promotion_log' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_flash_promotion_product_relation'
    ) THEN
        ALTER TABLE public.sms_flash_promotion_product_relation SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_flash_promotion_product_relation' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sms_flash_promotion_session'
    ) THEN
        ALTER TABLE public.sms_flash_promotion_session SET SCHEMA marketing;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sms_flash_promotion_session' 
          AND n.nspname = 'marketing' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA marketing';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='ai_chat_conversation'
    ) THEN
        ALTER TABLE public.ai_chat_conversation SET SCHEMA ai;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'ai_chat_conversation' 
          AND n.nspname = 'ai' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA ai';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='vector_store'
    ) THEN
        ALTER TABLE public.vector_store SET SCHEMA ai;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'vector_store' 
          AND n.nspname = 'ai' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA ai';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='pms_product_embedding'
    ) THEN
        ALTER TABLE public.pms_product_embedding SET SCHEMA ai;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'pms_product_embedding' 
          AND n.nspname = 'ai' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA ai';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='sys_banner'
    ) THEN
        ALTER TABLE public.sys_banner SET SCHEMA system;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'sys_banner' 
          AND n.nspname = 'system' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA system';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='outbox_event'
    ) THEN
        ALTER TABLE public.outbox_event SET SCHEMA integration;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'outbox_event' 
          AND n.nspname = 'integration' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA integration';
    END LOOP;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema='public' AND table_name='flyway_schema_history'
    ) THEN
        ALTER TABLE public.flyway_schema_history SET SCHEMA migration;
    END IF;
END $$;


DO $$
DECLARE
    seq_record RECORD;
BEGIN
    FOR seq_record IN 
        SELECT s.relname as sequence_name
        FROM pg_class s
        JOIN pg_depend d ON d.objid = s.oid
        JOIN pg_class t ON d.refobjid = t.oid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE s.relkind = 'S' 
          AND t.relname = 'flyway_schema_history' 
          AND n.nspname = 'migration' -- because table is already moved
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq_record.sequence_name || ' SET SCHEMA migration';
    END LOOP;
END $$;
