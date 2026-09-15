-- V10__migrate_reviews.sql
INSERT INTO pms_review (id, product_id, member_id, parent_id, rating, content, created_time, like_count, status) VALUES
(1, 1, 1, NULL, 5, 'The camera on this phone is absolutely breathtaking. Battery lasts all day easily.', NOW(), 24, 1);

INSERT INTO pms_review_media (id, review_id, sort_order, media_type, media_url, thumbnail_url) VALUES
(1, 1, 0, 'IMAGE', 'http://localhost:9000/nexus-media/public/ugc/reviews/1/iphone_review.png', NULL);
