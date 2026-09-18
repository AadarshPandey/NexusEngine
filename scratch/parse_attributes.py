import re

def parse_attributes():
    with open('/home/aadarsh/Documents/NexusEngine/allAttributes.txt', 'r') as f:
        content = f.read()
    
    # We want to find tables with L1 ID | Category | Attributes
    lines = content.split('\n')
    
    attr_categories = {}
    
    parsing_table = False
    for line in lines:
        if '| L1 ID |' in line:
            parsing_table = True
            continue
        if parsing_table and line.startswith('|---'):
            continue
        if parsing_table and line.startswith('|'):
            parts = [p.strip() for p in line.split('|')]
            if len(parts) >= 4 and parts[1].isdigit():
                cat_id = int(parts[1])
                cat_name = parts[2]
                attrs_raw = parts[3].split(',')
                attrs = []
                for a in attrs_raw:
                    a = a.strip()
                    is_sku = '*' in a
                    a_name = a.replace('*', '').strip()
                    attrs.append({'name': a_name, 'is_sku': is_sku})
                attr_categories[cat_id] = {'name': cat_name, 'attributes': attrs}
            elif not parts[1].isdigit():
                pass # Probably table header or something else
        elif parsing_table and not line.strip():
            parsing_table = False
            
    return attr_categories

def generate_sql(attr_categories):
    sql = []
    
    # Clear existing
    sql.append("TRUNCATE TABLE pms_product_attribute_category CASCADE;")
    sql.append("TRUNCATE TABLE pms_product_attribute CASCADE;")
    sql.append("TRUNCATE TABLE pms_product_category_attribute_relation CASCADE;")
    
    attr_id_counter = 1
    
    for cat_id, data in attr_categories.items():
        cat_name = data['name'].replace("'", "''")
        attrs = data['attributes']
        
        # 1. Insert attribute category (using the same ID as product category for simplicity)
        sql.append(f"INSERT INTO pms_product_attribute_category (id, name, attribute_count, param_count) VALUES ({cat_id}, '{cat_name} Attributes', {len([a for a in attrs if a['is_sku']])}, {len([a for a in attrs if not a['is_sku']])});")
        
        # 2. Insert attributes
        for attr in attrs:
            attr_type = 0 if attr['is_sku'] else 1
            input_type = 0 # manual
            select_type = 0
            
            sql.append(f"INSERT INTO pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, filter_type, search_type, related_status, hand_add_status, type) VALUES ({attr_id_counter}, {cat_id}, '{attr['name'].replace("'", "''")}', {select_type}, {input_type}, '', 0, 0, 0, 0, 0, {attr_type});")
            
            # 3. Insert relation
            sql.append(f"INSERT INTO pms_product_category_attribute_relation (product_category_id, product_attribute_id) VALUES ({cat_id}, {attr_id_counter});")
            
            attr_id_counter += 1
            
        # 4. Update products in this category to use this attribute category
        sql.append(f"UPDATE pms_product SET product_attribute_category_id = {cat_id} WHERE product_category_id = {cat_id};")
        
    return '\n'.join(sql)

if __name__ == "__main__":
    data = parse_attributes()
    sql = generate_sql(data)
    with open('/home/aadarsh/Documents/NexusEngine/scratch/attributes.sql', 'w') as f:
        f.write(sql)
    print(f"Generated {len(data)} attribute categories SQL.")
