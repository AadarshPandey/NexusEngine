-- V12__normalize_primary_keys.sql
-- Normalize Primary Keys for FAANG standards by removing hardcoded ID blocks

-- 1. Truncate tables with legacy hardcoded IDs
TRUNCATE TABLE sms_coupon CASCADE;
TRUNCATE TABLE cms_subject CASCADE;
TRUNCATE TABLE sms_home_recommend_subject CASCADE;

-- 2. Reset identity sequences
ALTER TABLE sms_coupon ALTER COLUMN id RESTART WITH 1;
ALTER TABLE cms_subject ALTER COLUMN id RESTART WITH 1;
ALTER TABLE sms_home_recommend_subject ALTER COLUMN id RESTART WITH 1;

-- 3. Insert fresh, clean data (letting Postgres auto-increment the IDs)
INSERT INTO sms_coupon (type, name, platform, count, amount, per_limit, min_point, start_time, end_time, use_type, note, publish_count, use_count, receive_count, enable_time, code, member_level) 
VALUES 
(0, 'Welcome Bonus 10% Off', 0, 1000, 10.00, 1, 0.00, NOW(), NOW() + INTERVAL '30 days', 0, 'For new users only', 1000, 0, 0, NOW(), 'WELCOME10', 0),
(1, 'Tech Week Mega Sale', 0, 500, 50.00, 1, 500.00, NOW(), NOW() + INTERVAL '7 days', 1, 'Valid on electronics', 500, 0, 0, NOW(), 'TECH50', 0),
(0, 'VIP Customer Discount', 0, 200, 100.00, 1, 1000.00, NOW(), NOW() + INTERVAL '90 days', 0, 'For our best customers', 200, 0, 0, NOW(), 'VIP100', 4);

INSERT INTO cms_subject (category_id, title, pic, product_count, recommend_status, create_time, collect_count, read_count, comment_count, album_pics, description, show_status, content, forward_count, category_name)
VALUES
(1, 'The Ultimate iPhone 15 Pro Review', 'http://localhost:9000/nexus-media/public/marketing/banners/tech_sale.png', 1, 1, NOW(), 120, 5000, 45, '', 'Deep dive into Apple''s latest titanium flagship.', 1, '<p>Titanium changes everything.</p>', 50, 'Electronics'),
(1, 'Top 5 Noise Cancelling Headphones', 'http://localhost:9000/nexus-media/public/marketing/banners/nike_promo.png', 5, 1, NOW(), 85, 3000, 20, '', 'Comparing Sony, Bose, and Apple.', 1, '<p>Sony takes the crown.</p>', 30, 'Electronics');

INSERT INTO sms_home_recommend_subject (subject_id, subject_name, recommend_status, sort)
VALUES
(1, 'The Ultimate iPhone 15 Pro Review', 1, 100),
(2, 'Top 5 Noise Cancelling Headphones', 1, 90);
