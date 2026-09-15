BEGIN;

-- 1. Clear dependent table
TRUNCATE TABLE pms_comment_replay RESTART IDENTITY;

-- 2. Clear comments table
TRUNCATE TABLE pms_comment RESTART IDENTITY CASCADE;

-- 3. Add fresh realistic comments to products
-- Add one comment to all products
INSERT INTO pms_comment (product_id, product_name, content, create_time, member_nick_name, star, show_status, collect_couont, read_count, replay_count)
SELECT 
    id, 
    name, 
    'The quality is amazing, completely exceeded my expectations. Very satisfied with the purchase!', 
    NOW(), 
    'Alice_Customer', 
    5, 
    1, 
    0, 
    10, 
    0
FROM pms_product;

-- Add a second comment to half of the products (even IDs)
INSERT INTO pms_comment (product_id, product_name, content, create_time, member_nick_name, star, show_status, collect_couont, read_count, replay_count)
SELECT 
    id, 
    name, 
    'Good value for money. Shipping was very fast.', 
    NOW() - INTERVAL '1 day', 
    'Bob_Customer', 
    4, 
    1, 
    2, 
    5, 
    0
FROM pms_product
WHERE id % 2 = 0;

-- 4. Update the product_comment_count in pms_brand
UPDATE pms_brand b
SET product_comment_count = COALESCE((
    SELECT COUNT(c.id) 
    FROM pms_product p
    JOIN pms_comment c ON p.id = c.product_id
    WHERE p.brand_id = b.id
), 0);

COMMIT;
