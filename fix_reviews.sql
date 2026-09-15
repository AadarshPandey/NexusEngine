BEGIN;

TRUNCATE TABLE pms_review_media RESTART IDENTITY;
TRUNCATE TABLE pms_review RESTART IDENTITY CASCADE;

INSERT INTO pms_review (product_id, member_id, rating, content, like_count, status, created_time, parent_id)
SELECT 
    id, 
    1, 
    5, 
    'The quality is amazing, completely exceeded my expectations. Very satisfied with the purchase!', 
    10, 
    1, 
    NOW(), 
    NULL
FROM pms_product;

INSERT INTO pms_review (product_id, member_id, rating, content, like_count, status, created_time, parent_id)
SELECT 
    id, 
    2, 
    4, 
    'Good value for money. Shipping was very fast.', 
    5, 
    1, 
    NOW() - INTERVAL '1 day', 
    NULL
FROM pms_product
WHERE id % 2 = 0;

UPDATE pms_brand b
SET product_comment_count = COALESCE((
    SELECT COUNT(r.id) 
    FROM pms_product p
    JOIN pms_review r ON p.id = r.product_id
    WHERE p.brand_id = b.id
), 0);

COMMIT;
