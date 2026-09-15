-- V8__update_minio_enterprise_paths.sql
-- FAANG/Enterprise pattern: Fully Qualified MinIO URLs structured by Domain & Entity ID

-- 1. Brands (Catalog Domain)
UPDATE pms_brand SET 
  logo = 'http://localhost:9000/nexus-media/public/catalog/brands/1/logo.png', 
  big_pic = 'http://localhost:9000/nexus-media/public/catalog/brands/1/banner.png' 
WHERE id = 1;

UPDATE pms_brand SET 
  logo = 'http://localhost:9000/nexus-media/public/catalog/brands/2/logo.png', 
  big_pic = 'http://localhost:9000/nexus-media/public/catalog/brands/2/banner.png' 
WHERE id = 2;

UPDATE pms_brand SET 
  logo = 'http://localhost:9000/nexus-media/public/catalog/brands/3/logo.png', 
  big_pic = 'http://localhost:9000/nexus-media/public/catalog/brands/3/banner.png' 
WHERE id = 3;

-- 2. Categories (Catalog Domain)
UPDATE pms_product_category SET icon = 'http://localhost:9000/nexus-media/public/catalog/categories/1/icon_electronics.png' WHERE id = 1;
UPDATE pms_product_category SET icon = 'http://localhost:9000/nexus-media/public/catalog/categories/2/icon_smartphone.png' WHERE id = 2;
UPDATE pms_product_category SET icon = 'http://localhost:9000/nexus-media/public/catalog/categories/3/icon_headphones.png' WHERE id = 3;
UPDATE pms_product_category SET icon = 'http://localhost:9000/nexus-media/public/catalog/categories/4/icon_clothing.png' WHERE id = 4;
UPDATE pms_product_category SET icon = 'http://localhost:9000/nexus-media/public/catalog/categories/5/icon_sneakers.png' WHERE id = 5;

-- 3. Products (Catalog Domain)
UPDATE pms_product SET 
  pic = 'http://localhost:9000/nexus-media/public/catalog/products/1/main.png',
  album_pics = 'http://localhost:9000/nexus-media/public/catalog/products/1/gallery1.png,http://localhost:9000/nexus-media/public/catalog/products/1/gallery2.png'
WHERE id = 1;

UPDATE pms_product SET 
  pic = 'http://localhost:9000/nexus-media/public/catalog/products/2/main.png',
  album_pics = 'http://localhost:9000/nexus-media/public/catalog/products/2/gallery1.png'
WHERE id = 2;

UPDATE pms_product SET 
  pic = 'http://localhost:9000/nexus-media/public/catalog/products/3/main.png',
  album_pics = 'http://localhost:9000/nexus-media/public/catalog/products/3/gallery1.png'
WHERE id = 3;

-- 4. SKUs (Catalog Domain)
UPDATE pms_sku_stock SET pic = 'http://localhost:9000/nexus-media/public/catalog/skus/APP-IP15P-BLK-256/sku_image.png' WHERE id = 1;
UPDATE pms_sku_stock SET pic = 'http://localhost:9000/nexus-media/public/catalog/skus/APP-IP15P-TIT-512/sku_image.png' WHERE id = 2;
UPDATE pms_sku_stock SET pic = 'http://localhost:9000/nexus-media/public/catalog/skus/SON-XM5-BLK/sku_image.png' WHERE id = 3;
UPDATE pms_sku_stock SET pic = 'http://localhost:9000/nexus-media/public/catalog/skus/NIK-AM270-WHT-10/sku_image.png' WHERE id = 4;

-- 5. Banners (Marketing Domain)

UPDATE sms_home_advertise SET pic = 'http://localhost:9000/nexus-media/public/marketing/banners/tech_sale.png' WHERE id = 1;
UPDATE sms_home_advertise SET pic = 'http://localhost:9000/nexus-media/public/marketing/banners/nike_promo.png' WHERE id = 2;

-- 6. Comments (UGC Domain) and Avatars (User Domain)
UPDATE pms_comment SET 
  pics = 'http://localhost:9000/nexus-media/public/ugc/reviews/1/iphone_review.png',
  member_icon = 'http://localhost:9000/nexus-media/public/users/customer1/avatar.png'
WHERE id = 1;

UPDATE pms_comment SET 
  member_icon = 'http://localhost:9000/nexus-media/public/users/default/avatar.png'
WHERE id = 2;

UPDATE pms_comment_reply SET 
  member_icon = 'http://localhost:9000/nexus-media/public/users/admin/avatar.png'
WHERE id = 1;
