-- V7__insert_sensible_ecommerce_data.sql

-- 1. Freight Templates
INSERT INTO pms_freight_template (id, name, charge_type, base_weight, base_shipping_fee, incremental_weight_unit, incremental_fee, dest) VALUES 
(1, 'Standard Shipping', 0, 1.0, 5.00, 1.0, 2.00, 'Global'),
(2, 'Express Air Delivery', 0, 1.0, 15.00, 1.0, 5.00, 'Domestic');

-- 2. Preference Areas (removed pic because it does not exist)
INSERT INTO cms_preference_area (id, name, sub_title, sort, show_status) VALUES
(1, 'Tech Gadgets of 2026', 'The latest and greatest in tech', 100, 1),
(2, 'Summer Activewear', 'Stay fit and cool', 90, 1);

-- 3. Brands
INSERT INTO pms_brand (id, name, first_letter, sort, factory_status, show_status, product_count, product_comment_count, logo, big_pic, brand_story) VALUES
(1, 'Apple', 'A', 100, 1, 1, 1, 10, '/media/brands/apple_logo.png', '/media/brands/apple_banner.png', 'Think Different. Innovative technology for everyday life.'),
(2, 'Sony', 'S', 90, 1, 1, 1, 5, '/media/brands/sony_logo.png', '/media/brands/sony_banner.png', 'To fill the world with emotion, through the power of creativity and technology.'),
(3, 'Nike', 'N', 80, 1, 1, 1, 0, '/media/brands/nike_logo.png', '/media/brands/nike_banner.png', 'Just Do It. Inspiring athletes worldwide.');

-- 4. Product Categories
INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) VALUES
(1, NULL, 'Electronics', 0, 2, 'item', 1, 1, 100, '/media/categories/icon_electronics.png', 'tech,gadgets,electronics', 'Latest technology and gadgets'),
(2, 1, 'Smartphones', 1, 1, 'device', 1, 1, 100, '/media/categories/icon_smartphone.png', 'phone,mobile,smartphone', 'Mobile communication devices'),
(3, 1, 'Headphones', 1, 1, 'pair', 1, 1, 90, '/media/categories/icon_headphones.png', 'audio,headphones,music', 'Premium audio equipment'),
(4, NULL, 'Clothing & Shoes', 0, 1, 'item', 1, 1, 80, '/media/categories/icon_clothing.png', 'apparel,fashion,shoes', 'Fashion and activewear'),
(5, 4, 'Sneakers', 1, 1, 'pair', 1, 1, 100, '/media/categories/icon_sneakers.png', 'shoes,sneakers,running', 'Athletic and casual footwear');

-- 5. Product Attribute Categories
INSERT INTO pms_product_attribute_category (id, name, attribute_count, param_count) VALUES
(1, 'Smartphone Specs', 2, 0),
(2, 'Audio Specs', 1, 0),
(3, 'Shoe Sizes', 1, 0);

-- 6. Product Attributes
INSERT INTO pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, filter_type, search_type, related_status, hand_add_status, type) VALUES
(1, 1, 'Color', 2, 1, 'Space Black,Titanium,Silver', 100, 1, 1, 1, 0, 0),
(2, 1, 'Storage', 2, 1, '128GB,256GB,512GB,1TB', 90, 1, 1, 1, 0, 0),
(3, 2, 'Color', 2, 1, 'Black,Silver,Midnight Blue', 100, 1, 1, 1, 0, 0),
(4, 3, 'Size (US)', 2, 1, '8,9,10,11,12', 100, 1, 1, 1, 0, 0);

-- 7. Products (Fixed recommand_status and made sure all columns match exactly)
INSERT INTO pms_product (id, brand_id, product_category_id, freight_template_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommand_status, verify_status, sort, sale, price, promotion_price, gift_growth, gift_point, use_point_limit, sub_title, description, original_price, stock, low_stock, unit, weight, preview_status, service_ids, keywords, note, album_pics, detail_title, detail_desc, detail_html, detail_mobile_html, promotion_start_time, promotion_end_time, promotion_per_limit, promotion_type, product_category_name, brand_name) VALUES
(1, 1, 2, 2, 1, 'iPhone 15 Pro', '/media/products/iphone_15_pro.png', 'APP-IP15P-2026', 0, 1, 1, 1, 1, 100, 1500, 999.00, 949.00, 100, 100, 50, 'Forged in titanium', 'The most advanced iPhone ever.', 1099.00, 5000, 100, 'device', 187.0, 1, '1,2,3', 'apple,iphone,smartphone', 'Bestseller', '/media/products/iphone_15_pro_gallery1.png,/media/products/iphone_15_pro_gallery2.png', 'iPhone 15 Pro Design', 'Aerospace-grade titanium design', '<p>Incredible detailed HTML here</p>', '<p>Mobile HTML</p>', NULL, NULL, 0, 0, 'Smartphones', 'Apple'),
(2, 2, 3, 1, 2, 'Sony WH-1000XM5', '/media/products/sony_xm5.png', 'SON-XM5-2026', 0, 1, 1, 1, 1, 90, 850, 398.00, 348.00, 40, 40, 20, 'Industry Leading Noise Canceling', 'Premium wireless headphones', 448.00, 2000, 50, 'pair', 250.0, 1, '1,2', 'sony,headphones,audio', 'Highly Rated', '/media/products/sony_xm5_gallery1.png', 'Sony XM5 Noise Canceling', 'Next generation audio processing', '<p>Detailed audio specs</p>', '<p>Mobile specs</p>', NULL, NULL, 0, 0, 'Headphones', 'Sony'),
(3, 3, 5, 1, 3, 'Nike Air Max 270', '/media/products/nike_air_max.png', 'NIK-AM270-2026', 0, 1, 0, 1, 1, 80, 2300, 150.00, NULL, 15, 15, 10, 'Legendary Air', 'Unmatched comfort and style.', 150.00, 1200, 20, 'pair', 800.0, 1, '1,2,3', 'nike,shoes,sneakers', 'Summer Collection', '/media/products/nike_air_max_gallery1.png', 'Nike Air Max Comfort', 'All-day comfort with Max Air unit', '<p>Shoe HTML</p>', '<p>Mobile Shoe HTML</p>', NULL, NULL, 0, 0, 'Sneakers', 'Nike');

-- 8. SKU Stock
INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, pic, sale, promotion_price, lock_stock, sp_data) VALUES
(1, 1, 'APP-IP15P-BLK-256', 999.00, 2000, 100, '/media/products/iphone_15_pro_black.png', 800, 949.00, 0, '[{"key":"Color","value":"Space Black"},{"key":"Storage","value":"256GB"}]'),
(2, 1, 'APP-IP15P-TIT-512', 1199.00, 1500, 50, '/media/products/iphone_15_pro_titanium.png', 400, 1149.00, 0, '[{"key":"Color","value":"Titanium"},{"key":"Storage","value":"512GB"}]'),
(3, 2, 'SON-XM5-BLK', 398.00, 1000, 50, '/media/products/sony_xm5_black.png', 500, 348.00, 0, '[{"key":"Color","value":"Black"}]'),
(4, 3, 'NIK-AM270-WHT-10', 150.00, 500, 10, '/media/products/nike_air_max_white.png', 120, NULL, 0, '[{"key":"Size (US)","value":"10"}]');

-- 9. Home Advertise (Banners)
INSERT INTO sms_home_advertise (id, name, type, pic, start_time, end_time, status, click_count, order_count, url, note, sort) VALUES
(1, 'Black Friday Tech Sale', 1, '/media/banners/banner_tech_sale.png', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1, 10450, 520, '/category/1', 'Huge discounts on electronics', 100),
(2, 'Step Up Your Game with Nike', 1, '/media/banners/banner_nike.png', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1, 8430, 310, '/category/5', 'New sneaker collection', 90);

-- 10. Comments (Reviews)
INSERT INTO pms_comment (id, product_id, member_nick_name, product_name, star, member_ip, show_status, product_attribute, likes_count, read_count, content, pics, member_icon, reply_count, create_time) VALUES
(1, 1, 'customer1', 'iPhone 15 Pro', 5, '192.168.1.1', 1, 'Color: Space Black, Storage: 256GB', 24, 150, 'The camera on this phone is absolutely breathtaking. Battery lasts all day easily.', '/media/reviews/iphone_review.png', '/media/avatars/customer1_avatar.png', 1, NOW()),
(2, 2, 'tech_guru', 'Sony WH-1000XM5', 5, '192.168.1.2', 1, 'Color: Black', 12, 85, 'Best noise cancellation I have ever used. Perfect for flights.', NULL, '/media/avatars/default_avatar.png', 0, NOW());

-- 11. Comment Replies
INSERT INTO pms_comment_reply (id, comment_id, member_nick_name, member_icon, content, create_time, type) VALUES
(1, 1, 'admin', '/media/avatars/admin_avatar.png', 'Thank you for your fantastic review! We are glad you love the new camera.', NOW(), 0);

-- 12. Product Verify Records
INSERT INTO pms_product_verify_record (id, product_id, create_time, reviewer_name, status, detail) VALUES
(1, 1, NOW(), 'admin', 1, 'Approved for Black Friday promotion'),
(2, 2, NOW(), 'admin', 1, 'Audio specs verified'),
(3, 3, NOW(), 'admin', 1, 'Stock confirmed');

