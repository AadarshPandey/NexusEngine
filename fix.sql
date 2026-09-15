BEGIN;

SELECT setval('public.pms_comment_id_seq', (SELECT MAX(id) FROM pms_comment));

-- 1. Fix factory_status
UPDATE pms_brand SET factory_status = 0 WHERE factory_status NOT IN (0, 1) OR factory_status IS NULL;

-- 2. Fix first_letter
UPDATE pms_brand SET first_letter = SUBSTRING(UPPER(name) FROM 1 FOR 1);

-- 3. Fix product_count
UPDATE pms_brand b
SET product_count = (SELECT COUNT(*) FROM pms_product p WHERE p.brand_id = b.id);

-- 4. Add a comment to products that have none
INSERT INTO pms_comment (product_id, product_name, content, create_time, member_nick_name, star, show_status, collect_couont, read_count, replay_count)
SELECT id, name, 'This is an excellent product, highly recommended!', NOW(), 'test_user', 5, 1, 0, 0, 0
FROM pms_product
WHERE id NOT IN (SELECT DISTINCT product_id FROM pms_comment WHERE product_id IS NOT NULL);

-- 5. Fix product_comment_count in pms_brand
UPDATE pms_brand b
SET product_comment_count = COALESCE((
    SELECT COUNT(c.id) 
    FROM pms_product p
    JOIN pms_comment c ON p.id = c.product_id
    WHERE p.brand_id = b.id
), 0);

COMMIT;
