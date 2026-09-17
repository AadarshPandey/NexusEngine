import random

categories = {
    "Electronics": ["Smartphones", "Laptops", "Tablets", "Headphones & Earbuds", "Smartwatches", "Cameras", "Monitors", "Keyboards & Mice"],
    "Fashion": ["Men's Clothing", "Women's Clothing", "Footwear", "Accessories"],
    "Home & Kitchen": ["Furniture", "Kitchen Appliances", "Home Appliances", "Kitchenware"],
    "Beauty & Personal Care": ["Skincare", "Hair Care", "Makeup", "Grooming"],
    "Sports & Fitness": ["Running", "Gym Equipment", "Yoga", "Outdoor"],
    "Books & Stationery": ["Fiction", "Non-Fiction", "Technology", "Notebooks & Supplies"],
    "Grocery": ["Beverages", "Snacks", "Breakfast", "Pantry"],
    "Automotive": ["Car Accessories", "Car Care", "Replacement Parts"]
}

brands_by_cat = {
    "Electronics": ["Apple", "Samsung", "Google", "Sony", "OnePlus", "Nothing", "Dell", "Lenovo", "ASUS", "Logitech", "boAt", "Noise", "Portronics", "Zebronics", "Lava", "Micromax"],
    "Fashion": ["Nike", "Adidas", "Puma", "Levi's", "Uniqlo", "Zara", "H&M", "Ray-Ban", "The North Face", "FabIndia", "Manyavar", "W for Woman", "Allen Solly", "Peter England", "Raymond", "Van Heusen"],
    "Home & Kitchen": ["IKEA", "Philips", "Dyson", "LG", "Bosch", "Prestige", "Hawkins", "KENT", "Bajaj", "Havells", "Usha", "Butterfly", "Crompton", "Wonderchef"],
    "Beauty & Personal Care": ["L'Oréal", "Maybelline", "Nivea", "Clinique", "The Ordinary", "Dove", "Neutrogena", "Lakmé", "Mamaearth", "Minimalist", "Forest Essentials", "Plum", "Nykaa", "Biotique"],
    "Sports & Fitness": ["Under Armour", "Decathlon", "Wilson", "Yonex", "Nivia", "Cosco", "SS", "SG", "Vector X"],
    "Books & Stationery": ["Penguin Random House", "O'Reilly", "McGraw Hill", "Pearson", "Moleskine", "Rupa Publications", "S. Chand", "Arihant", "Amar Chitra Katha", "Crossword"],
    "Grocery": ["Nestlé", "Coca-Cola", "Kellogg's", "Unilever", "Britannia", "Tata", "Amul", "Parle", "ITC", "Dabur", "Patanjali", "Mother Dairy", "Aashirvaad", "Fortune", "MTR"],
    "Automotive": ["Michelin", "Castrol", "3M", "Motul", "CEAT", "MRF", "Apollo Tyres", "TVS", "Bosch India", "Gulf Oi"]
}

sql = []
sql.append("-- V2: Seed Full E-Commerce Catalog")
sql.append("")

# 1. Categories
cat_id_map = {}
cat_id = 1
for parent_name, children in categories.items():
    sql.append(f"INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) VALUES ({cat_id}, 0, '{parent_name}', 0, 0, 'piece', 1, 1, 0, NULL, '{parent_name.lower()}', '{parent_name} items') ON CONFLICT DO NOTHING;")
    parent_id = cat_id
    cat_id_map[parent_name] = parent_id
    cat_id += 1
    
    for child in children:
        sql.append(f"INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) VALUES ({cat_id}, {parent_id}, '{child}', 1, 0, 'piece', 1, 1, 0, NULL, '{child.lower()}', '{child} items') ON CONFLICT DO NOTHING;")
        cat_id_map[f"{parent_name}-{child}"] = cat_id
        cat_id += 1

sql.append("")
sql.append(f"SELECT setval('pms_product_category_id_seq', {cat_id});")
sql.append("")

# 2. Brands
brand_id = 1
brand_id_map = {}
for cat, brands in brands_by_cat.items():
    for brand in brands:
        # Escape quotes
        brand_esc = brand.replace("'", "''")
        first_letter = brand[0].upper()
        sql.append(f"INSERT INTO pms_brand (id, name, first_letter, sort, factory_status, show_status, product_count, product_comment_count, logo, big_pic, brand_story) VALUES ({brand_id}, '{brand_esc}', '{first_letter}', 0, 1, 1, 0, 0, NULL, NULL, 'Story of {brand_esc}') ON CONFLICT DO NOTHING;")
        brand_id_map[brand] = brand_id
        brand_id += 1

sql.append("")
sql.append(f"SELECT setval('pms_brand_id_seq', {brand_id});")
sql.append("")

# 3. Product Attributes Categories
attr_categories = [
    "Smartphone Specs", "Laptop Specs", "Audio Specs", "Shoe Sizes", "Apparel Sizes", 
    "Beauty Shades", "Book Editions", "Grocery Pack", "Display Specs", "General Variants"
]
attr_cat_map = {}
attr_cat_id = 1
for attr in attr_categories:
    sql.append(f"INSERT INTO pms_product_attribute_category (id, name, attribute_count, param_count) VALUES ({attr_cat_id}, '{attr}', 2, 0) ON CONFLICT DO NOTHING;")
    attr_cat_map[attr] = attr_cat_id
    attr_cat_id += 1

sql.append("")
sql.append(f"SELECT setval('pms_product_attribute_category_id_seq', {attr_cat_id});")
sql.append("")

# 4. Product Attributes
attributes_def = {
    "Smartphone Specs": ["Color", "Storage"],
    "Laptop Specs": ["RAM", "Storage", "Color"],
    "Audio Specs": ["Color"],
    "Shoe Sizes": ["Size (US)", "Color"],
    "Apparel Sizes": ["Size", "Color"],
    "Beauty Shades": ["Shade"],
    "Book Editions": ["Format"],
    "Grocery Pack": ["Pack Size"],
    "Display Specs": ["Size (inches)", "Resolution"],
    "General Variants": ["Color"]
}

attr_id_map = {}
attr_id = 1
for cat, attrs in attributes_def.items():
    for a in attrs:
        # type 0 for variant/specification, input_type 1 for select
        sql.append(f"INSERT INTO pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, filter_type, search_type, related_status, hand_add_status, type) VALUES ({attr_id}, {attr_cat_map[cat]}, '{a}', 1, 1, 'Value1,Value2', 0, 1, 1, 1, 0, 0) ON CONFLICT DO NOTHING;")
        attr_id_map[f"{cat}-{a}"] = attr_id
        attr_id += 1

sql.append("")
sql.append(f"SELECT setval('pms_product_attribute_id_seq', {attr_id});")
sql.append("")

# 5. Products and SKUs
products_def = [
    {"name": "iPhone 17", "brand": "Apple", "cat": "Electronics-Smartphones", "attr_cat": "Smartphone Specs", "variants": {"Color": ["Black", "Silver"], "Storage": ["128GB", "256GB"]}, "price": 80000},
    {"name": "Galaxy S25", "brand": "Samsung", "cat": "Electronics-Smartphones", "attr_cat": "Smartphone Specs", "variants": {"Color": ["Phantom Black", "Cream"], "Storage": ["256GB", "512GB"]}, "price": 75000},
    {"name": "MacBook Pro M5", "brand": "Apple", "cat": "Electronics-Laptops", "attr_cat": "Laptop Specs", "variants": {"RAM": ["16GB", "32GB"], "Storage": ["512GB", "1TB"], "Color": ["Space Gray"]}, "price": 150000},
    {"name": "Air Max 2026", "brand": "Nike", "cat": "Fashion-Footwear", "attr_cat": "Shoe Sizes", "variants": {"Size (US)": ["8", "9", "10"], "Color": ["Black", "White"]}, "price": 12000},
    {"name": "Ultraboost 26", "brand": "Adidas", "cat": "Fashion-Footwear", "attr_cat": "Shoe Sizes", "variants": {"Size (US)": ["7", "8", "9"], "Color": ["Core Black"]}, "price": 14000},
    {"name": "Clean Code", "brand": "Pearson", "cat": "Books & Stationery-Technology", "attr_cat": "Book Editions", "variants": {"Format": ["Paperback", "Hardcover", "Kindle"]}, "price": 500},
    {"name": "Coca-Cola Zero", "brand": "Coca-Cola", "cat": "Grocery-Beverages", "attr_cat": "Grocery Pack", "variants": {"Pack Size": ["300ml", "750ml", "1.25L", "2.25L"]}, "price": 40},
    {"name": "Supima Cotton T-Shirt", "brand": "Uniqlo", "cat": "Fashion-Men's Clothing", "attr_cat": "Apparel Sizes", "variants": {"Size": ["S", "M", "L", "XL"], "Color": ["White", "Black"]}, "price": 1500},
    {"name": "WH-1000XM6", "brand": "Sony", "cat": "Electronics-Headphones & Earbuds", "attr_cat": "Audio Specs", "variants": {"Color": ["Black", "Silver", "Blue"]}, "price": 25000},
    {"name": "V15 Detect Absolute", "brand": "Dyson", "cat": "Home & Kitchen-Home Appliances", "attr_cat": "General Variants", "variants": {"Color": ["Yellow/Nickel"]}, "price": 55000}
]

import itertools
import json

prod_id = 1
sku_id = 1
attr_val_id = 1
media_id = 1

for p in products_def:
    b_id = brand_id_map[p["brand"]]
    c_id = cat_id_map[p["cat"]]
    ac_id = attr_cat_map[p["attr_cat"]]
    
    # insert product
    name_esc = p["name"].replace("'", "''")
    product_sn = f"{p['brand'][:3].upper()}-{name_esc[:3].upper()}-{prod_id}"
    
    # pic field format
    pic_url = f"http://localhost:9000/nexus-media/public/catalog/products/{prod_id}-{name_esc.lower().replace(' ', '-')}/main.webp"
    
    sql.append(f"INSERT INTO pms_product (id, brand_id, product_category_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, unit, weight, keywords, detail_title) VALUES ({prod_id}, {b_id}, {c_id}, {ac_id}, '{name_esc}', '{pic_url}', '{product_sn}', 0, 1, 1, 1, 1, 0, 0, 'piece', 500, '{name_esc.lower()}', '{name_esc} Details') ON CONFLICT DO NOTHING;")
    
    # media
    sql.append(f"INSERT INTO pms_product_media (id, product_id, media_type, media_url, sort_order) VALUES ({media_id}, {prod_id}, 'image', '{pic_url}', 0) ON CONFLICT DO NOTHING;")
    media_id += 1
    
    # generate SKUs by cartesian product of variants
    keys = list(p["variants"].keys())
    val_lists = [p["variants"][k] for k in keys]
    combinations = list(itertools.product(*val_lists))
    
    for combo in combinations:
        sku_attrs = []
        sku_code_parts = [product_sn]
        
        for idx, key in enumerate(keys):
            val = combo[idx]
            sku_attrs.append({"key": key, "value": val})
            sku_code_parts.append(val[:3].upper())
            
            # product attribute values
            attr_db_id = attr_id_map[f"{p['attr_cat']}-{key}"]
            sql.append(f"INSERT INTO pms_product_attribute_value (id, product_id, product_attribute_id, value) VALUES ({attr_val_id}, {prod_id}, {attr_db_id}, '{val}') ON CONFLICT DO NOTHING;")
            attr_val_id += 1
        
        sku_code = "-".join(sku_code_parts)
        sku_attrs_json = json.dumps(sku_attrs).replace("'", "''")
        price = p["price"] + random.randint(0, 5000) # slight variation
        
        sku_pic = f"http://localhost:9000/nexus-media/public/catalog/skus/{sku_code}/main.webp"
        sql.append(f"INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, pic, sale, sku_attributes) VALUES ({sku_id}, {prod_id}, '{sku_code}', {price}, {random.randint(10, 100)}, 5, '{sku_pic}', 0, '{sku_attrs_json}'::jsonb) ON CONFLICT DO NOTHING;")
        sku_id += 1
        
    prod_id += 1

sql.append("")
sql.append(f"SELECT setval('pms_product_id_seq', {prod_id});")
sql.append(f"SELECT setval('pms_sku_stock_id_seq', {sku_id});")
sql.append(f"SELECT setval('pms_product_attribute_value_id_seq', {attr_val_id});")
sql.append(f"SELECT setval('pms_product_media_id_seq', {media_id});")
sql.append("")

with open('/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql', 'w') as f:
    f.write("\n".join(sql))

print("V2 generated successfully.")
