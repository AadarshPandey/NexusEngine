import re
import json

def parse_products(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    categories = {}
    brands = set()
    products = []
    
    product_section = re.search(r'# 2\. Products — 152 Products(.*?)(?=# 3\. SKU Stock)', content, re.DOTALL)
    if product_section:
        lines = product_section.group(1).strip().split('\n')
        current_category = None
        for line in lines:
            line = line.strip()
            if line.startswith('## '):
                current_category = line.split('—')[0].replace('##', '').strip()
                if current_category not in categories:
                    categories[current_category] = set()
            elif line.startswith('|') and not line.startswith('| ID') and not line.startswith('| ---'):
                parts = [p.strip() for p in line.split('|')]
                if len(parts) >= 7:
                    p_id = parts[1]
                    brand = parts[2]
                    subcat = parts[3]
                    p_name = parts[4]
                    
                    categories[current_category].add(subcat)
                    brands.add(brand)
                    
                    products.append({
                        'p_id': p_id,
                        'brand': brand,
                        'category': current_category,
                        'subcategory': subcat,
                        'name': p_name
                    })

    skus = []
    sku_section = re.search(r'# 3\. SKU Stock — Exactly 400 SKUs(.*)', content, re.DOTALL)
    if sku_section:
        lines = sku_section.group(1).strip().split('\n')
        for line in lines:
            line = line.strip()
            if line.startswith('|') and not line.startswith('| SKU ID') and not line.startswith('| ---'):
                parts = [p.strip() for p in line.split('|')]
                if len(parts) >= 9:
                    s_id = parts[1]
                    p_id = parts[2]
                    sku_code = parts[3].replace('`', '')
                    sku_attrs = parts[5].replace('`', '')
                    price_str = parts[6].replace(',', '')
                    promo_str = parts[7].replace(',', '')
                    stock_str = parts[8].replace(',', '')
                    
                    price = float(price_str) if price_str != '—' else 0
                    promo = float(promo_str) if promo_str != '—' else "NULL"
                    stock = int(stock_str) if stock_str != '—' else 0
                    
                    skus.append({
                        's_id': s_id,
                        'p_id': p_id,
                        'sku_code': sku_code,
                        'attrs': sku_attrs,
                        'price': price,
                        'promo': promo,
                        'stock': stock
                    })

    return categories, list(brands), products, skus

categories, brands, products, skus = parse_products('/home/aadarsh/Documents/NexusEngine/allProducts.txt')

sql = []
sql.append("TRUNCATE TABLE pms_product, pms_product_category, pms_brand, pms_sku_stock, pms_product_attribute_category, pms_product_attribute, pms_product_attribute_value CASCADE;")

# 1. Insert Brands
brand_map = {}
for i, brand in enumerate(sorted(brands), start=1):
    brand_map[brand] = i
    name = brand.replace("'", "''")
    # Brand logo placeholder: http://localhost:9000/nexus-media/brands/brand_name.jpg
    logo_url = f"http://localhost:9000/nexus-media/brands/{name.replace(' ', '_').lower()}.jpg"
    sql.append(f"INSERT INTO pms_brand (id, name, first_letter, sort, factory_status, show_status, product_count, logo) VALUES ({i}, '{name}', '{name[0].upper()}', {i}, 1, 1, 10, '{logo_url}');")

# 2. Insert Categories
cat_map = {}
subcat_map = {}
cat_id = 1
for cat, subcats in categories.items():
    cat_map[cat] = cat_id
    cat_name = cat.replace("'", "''")
    icon_url = f"http://localhost:9000/nexus-media/categories/{cat_name.replace(' ', '_').lower()}.jpg"
    sql.append(f"INSERT INTO pms_product_category (id, parent_id, name, level, product_count, show_status, nav_status, icon) VALUES ({cat_id}, 0, '{cat_name}', 0, 10, 1, 1, '{icon_url}');")
    parent_id = cat_id
    cat_id += 1
    for subcat in subcats:
        subcat_map[f"{cat}_{subcat}"] = cat_id
        subcat_name = subcat.replace("'", "''")
        sql.append(f"INSERT INTO pms_product_category (id, parent_id, name, level, product_count, show_status, nav_status) VALUES ({cat_id}, {parent_id}, '{subcat_name}', 1, 10, 1, 1);")
        cat_id += 1

# 3. Insert Products
product_map = {} # p_id (e.g. P001) -> db id
for i, p in enumerate(products, start=1):
    product_map[p['p_id']] = i
    b_id = brand_map[p['brand']]
    c_id = subcat_map[f"{p['category']}_{p['subcategory']}"]
    p_name = p['name'].replace("'", "''")
    b_name = p['brand'].replace("'", "''")
    c_name = p['subcategory'].replace("'", "''")
    pic_url = f"http://localhost:9000/nexus-media/products/{p['p_id'].lower()}.jpg"
    
    sql.append(f"INSERT INTO pms_product (id, brand_id, product_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, brand_name, product_category_name, vendor_id) VALUES ({i}, {b_id}, {c_id}, '{p_name}', '{pic_url}', '{p['p_id']}', 0, 1, 1, 1, 1, 100, 0, '{b_name}', '{c_name}', 1);")

# 4. Insert SKUs
for i, s in enumerate(skus, start=1):
    db_p_id = product_map[s['p_id']]
    pic_url = f"http://localhost:9000/nexus-media/skus/{s['s_id'].lower()}.jpg"
    sql.append(f"INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, pic, sale, promotion_price, sku_attributes) VALUES ({i}, {db_p_id}, '{s['sku_code']}', {s['price']}, {s['stock']}, 10, '{pic_url}', 0, {s['promo']}, '{s['attrs']}');")

with open('/home/aadarsh/Documents/NexusEngine/scratch/seed_catalog.sql', 'w') as f:
    f.write('\n'.join(sql))

print("SQL generated successfully.")
