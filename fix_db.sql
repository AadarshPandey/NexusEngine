-- Fix oms_order
UPDATE oms_order
SET delivery_company = 'FedEx',
    bill_content = 'Electronics and Accessories',
    bill_header = 'Personal',
    bill_receiver_email = 'customer@example.com',
    bill_receiver_phone = '1234567890',
    coupon_amount = COALESCE(coupon_amount, 0.00),
    use_integration = COALESCE(use_integration, 0),
    note = 'Standard delivery',
    modify_time = NOW()
WHERE delivery_company IS NULL OR delivery_company = '';

-- Fix oms_order_item
UPDATE oms_order_item ooi
SET product_brand = pp.brand_name,
    product_category_id = pp.product_category_id,
    product_pic = pp.pic,
    product_sn = pp.product_sn,
    product_sku_code = CONCAT(pp.product_sn, '-SKU-', ooi.id),
    product_attr = '[{"key":"Color","value":"Black"}]',
    gift_growth = 10,
    gift_integration = 10
FROM pms_product pp
WHERE ooi.product_id = pp.id 
  AND (ooi.product_brand IS NULL OR ooi.product_brand = '');

-- Fix relative image paths and nulls in pms_product
UPDATE pms_product
SET pic = CASE 
            WHEN pic IS NULL OR pic = '' THEN 'https://images.unsplash.com/photo-1523206489230-c012c64b2b48?w=500&q=80'
            WHEN pic LIKE '%/images/pic%' THEN 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80'
            ELSE pic 
          END,
    album_pics = CASE 
                   WHEN album_pics IS NULL OR album_pics = '' THEN 'https://images.unsplash.com/photo-1523206489230-c012c64b2b48?w=500&q=80'
                   WHEN album_pics LIKE '%/images/pic%' THEN 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80'
                   ELSE album_pics 
                 END,
    promotion_start_time = COALESCE(promotion_start_time, NOW()),
    promotion_end_time = COALESCE(promotion_end_time, NOW() + INTERVAL '30 days')
WHERE pic IS NULL OR pic = '' OR pic LIKE '%/images/pic%'
   OR album_pics IS NULL OR album_pics = '' OR album_pics LIKE '%/images/pic%'
   OR promotion_start_time IS NULL;

-- Fix relative image paths in ums_admin
UPDATE ums_admin
SET icon = 'https://ui-avatars.com/api/?name=Admin&background=random'
WHERE icon LIKE '%/images/pic%';

-- Insert dummy vectors into pms_product_embedding
INSERT INTO pms_product_embedding (product_id, embedding)
SELECT id, array_fill(0.1, ARRAY[1536])::vector
FROM pms_product
ON CONFLICT (product_id) DO NOTHING;

