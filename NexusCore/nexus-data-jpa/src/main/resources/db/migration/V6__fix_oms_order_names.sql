-- V6__fix_oms_order_names.sql
ALTER TABLE oms_order RENAME COLUMN growth TO experience_points;
ALTER TABLE oms_order RENAME COLUMN integration TO reward_points;
