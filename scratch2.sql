-- Fix ums_member_level
UPDATE ums_member_level SET name = 'Bronze Member', note = 'Basic membership level' WHERE id IN (1, 100, 200, 205);
UPDATE ums_member_level SET name = 'Silver Member', note = 'Silver tier membership' WHERE id IN (2, 101, 201, 206);
UPDATE ums_member_level SET name = 'Gold Member', note = 'Gold tier membership' WHERE id IN (3, 102, 202, 207);
UPDATE ums_member_level SET name = 'Platinum Member', note = 'Platinum tier membership' WHERE id IN (4, 103, 203, 208);
UPDATE ums_member_level SET name = 'Diamond Member', note = 'Diamond tier membership' WHERE id IN (5, 104, 204, 209);

-- Fix pms_member_price
UPDATE pms_member_price SET member_level_id = 1, member_level_name = 'Bronze Member' WHERE id IN (1, 100, 200);
UPDATE pms_member_price SET member_level_id = 2, member_level_name = 'Silver Member' WHERE id IN (2, 101, 201);
UPDATE pms_member_price SET member_level_id = 3, member_level_name = 'Gold Member' WHERE id IN (3, 102, 202);
UPDATE pms_member_price SET member_level_id = 4, member_level_name = 'Platinum Member' WHERE id IN (4, 103, 203);
UPDATE pms_member_price SET member_level_id = 5, member_level_name = 'Diamond Member' WHERE id IN (5, 104, 204);
-- Update the rest randomly just in case there are other ids
UPDATE pms_member_price SET member_level_id = 3, member_level_name = 'Gold Member' WHERE member_level_id NOT IN (1, 2, 3, 4, 5);

