DO $$
DECLARE
    rec RECORD;
    new_id INTEGER := 1;
BEGIN
    FOR rec IN SELECT id FROM pms_product_attribute_category ORDER BY id ASC LOOP
        -- Update the related tables to point to the new ID
        UPDATE pms_product_attribute SET product_attribute_category_id = new_id WHERE product_attribute_category_id = rec.id;
        UPDATE pms_product SET product_attribute_category_id = new_id WHERE product_attribute_category_id = rec.id;
        
        -- Finally update the category itself
        UPDATE pms_product_attribute_category SET id = new_id WHERE id = rec.id;
        
        new_id := new_id + 1;
    END LOOP;
END $$;

SELECT setval('pms_product_attribute_category_id_seq', (SELECT MAX(id) FROM pms_product_attribute_category));
