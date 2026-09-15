-- Fix cms_subject_category
UPDATE cms_subject_category SET name = 'Tech Guides' WHERE id IN (1, 100, 200, 205);
UPDATE cms_subject_category SET name = 'Seasonal Sales' WHERE id IN (2, 101, 201, 206);
UPDATE cms_subject_category SET name = 'Product Reviews' WHERE id IN (3, 102, 202, 207);
UPDATE cms_subject_category SET name = 'Buying Guides' WHERE id IN (4, 103, 203, 208);
UPDATE cms_subject_category SET name = 'Brand Highlights' WHERE id IN (5, 104, 204, 209);

-- Fix cms_subject titles to look like real articles/themes
UPDATE cms_subject SET title = 'Top 10 Tech Gadgets of 2024' WHERE id = 1;
UPDATE cms_subject SET title = 'Summer Smartphone Sale' WHERE id = 2;
UPDATE cms_subject SET title = 'In-Depth Review: Apple vs Android' WHERE id = 3;
UPDATE cms_subject SET title = 'How to Choose the Right Laptop' WHERE id = 4;
UPDATE cms_subject SET title = 'Sony Brand Week Showcase' WHERE id = 5;
UPDATE cms_subject SET title = 'Smart Home Automation Guide' WHERE id = 100;
UPDATE cms_subject SET title = 'Black Friday Early Access' WHERE id = 101;
UPDATE cms_subject SET title = 'Camera Sensor Comparison' WHERE id = 102;
UPDATE cms_subject SET title = 'Ultimate Tablet Buying Guide' WHERE id = 103;
UPDATE cms_subject SET title = 'Apple Ecosystem Highlights' WHERE id = 104;
UPDATE cms_subject SET title = 'Future of Wearable Tech' WHERE id = 200;
UPDATE cms_subject SET title = 'Winter Clearance Event' WHERE id = 201;
UPDATE cms_subject SET title = 'Headphone Audio Quality Test' WHERE id = 202;
UPDATE cms_subject SET title = 'Monitor Setup Buying Guide' WHERE id = 203;
UPDATE cms_subject SET title = 'Samsung Innovation Day' WHERE id = 204;
UPDATE cms_subject SET title = 'PC Building Guide 2024' WHERE id = 205;
UPDATE cms_subject SET title = 'Cyber Monday Flash Deals' WHERE id = 206;
UPDATE cms_subject SET title = 'Review: Next-Gen Consoles' WHERE id = 207;
UPDATE cms_subject SET title = 'Best Budget TVs Guide' WHERE id = 208;
UPDATE cms_subject SET title = 'LG Display Technology' WHERE id = 209;

-- Sync category names into cms_subject
UPDATE cms_subject cs
SET category_name = csc.name
FROM cms_subject_category csc
WHERE cs.category_id = csc.id;

-- Fix sms_home_recommend_subject
UPDATE sms_home_recommend_subject shrs
SET subject_name = cs.title
FROM cms_subject cs
WHERE shrs.subject_id = cs.id;

-- Fix image paths for cms_subject
UPDATE cms_subject
SET pic = 'https://images.unsplash.com/photo-1499951360447-b19be8fe80f5?w=500&q=80',
    album_pics = 'https://images.unsplash.com/photo-1499951360447-b19be8fe80f5?w=500&q=80'
WHERE pic LIKE '%/images/pic%';

UPDATE cms_subject_category
SET icon = 'https://images.unsplash.com/photo-1499951360447-b19be8fe80f5?w=500&q=80'
WHERE icon LIKE '%/images/pic%';
