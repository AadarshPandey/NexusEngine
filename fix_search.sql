-- Undelete all products and publish them so they appear in search and category pages
UPDATE pms_product
SET delete_status = 0,
    publish_status = 1,
    verify_status = 1,
    keywords = LOWER(name),
    sub_title = name,
    product_category_id = 1
WHERE delete_status = 1 OR publish_status = 0 OR keywords LIKE 'ValidData%' OR keywords LIKE 'Sample%';

-- Fix pms_product_category as well so the category list isn't showing products
UPDATE pms_product_category SET name = 'Computers & Tablets', description = 'Laptops and tablets' WHERE id IN (3, 100, 200, 205);
UPDATE pms_product_category SET name = 'Smartphones', description = 'Mobile devices' WHERE id IN (4, 101, 201, 206);
UPDATE pms_product_category SET name = 'Audio & Headphones', description = 'Headphones and speakers' WHERE id IN (5, 102, 202, 207);
UPDATE pms_product_category SET name = 'TV & Home Theater', description = 'Televisions and home theater' WHERE id IN (103, 203, 208);
UPDATE pms_product_category SET name = 'Gaming Consoles', description = 'Consoles and accessories' WHERE id IN (104, 204, 209);
