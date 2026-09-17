import json
import random

# Constants
MINIO_BASE = "http://localhost:9000/nexus-media/public/catalog"

def esc(s):
    if s is None: return "NULL"
    s = str(s).replace("'", "''")
    return f"'{s}'"

def generate():
    sql = []
    
    # 1. Truncate Tables
    tables = [
        "pms_product_media", "pms_sku_stock", "pms_product_attribute_value", 
        "pms_product", "pms_product_attribute", "pms_product_attribute_category", 
        "pms_product_category", "pms_brand", "sys_banner"
    ]
    sql.append("-- Truncate tables")
    for t in tables:
        sql.append(f"TRUNCATE TABLE {t} CASCADE;")
    sql.append("")

    # 2. Brands
    brands = {
        "Electronics": ["Apple", "Samsung", "Google", "Sony", "OnePlus", "Dell", "Lenovo", "ASUS", "Logitech"],
        "Fashion": ["Nike", "Adidas", "Puma", "Levi's", "Uniqlo", "Zara", "H&M", "Ray-Ban", "The North Face"],
        "Home & Kitchen": ["IKEA", "Philips", "Dyson", "LG", "Bosch", "Prestige", "Hawkins", "KENT"],
        "Beauty": ["L'Oréal", "Maybelline", "Nivea", "Clinique", "The Ordinary", "Dove", "Neutrogena"],
        "Sports": ["Under Armour", "Decathlon", "Wilson", "Yonex"],
        "Books": ["Penguin", "O'Reilly", "McGraw Hill", "Pearson", "Moleskine"],
        "Grocery": ["Nestlé", "Coca-Cola", "Kellogg's", "Unilever", "Britannia", "Tata", "Amul"],
        "Automotive": ["Michelin", "Castrol", "3M", "Motul"]
    }
    
    brand_map = {}
    brand_id = 1
    sql.append("-- Brands")
    for cat, b_list in brands.items():
        for b in b_list:
            slug = b.lower().replace(" ", "-").replace("'", "")
            logo = f"{MINIO_BASE}/brands/{brand_id}-{slug}/logo.webp"
            banner = f"{MINIO_BASE}/brands/{brand_id}-{slug}/banner.webp"
            sql.append(f"INSERT INTO pms_brand (id, name, first_letter, sort, factory_status, show_status, product_count, product_comment_count, logo, big_pic, brand_story) VALUES ({brand_id}, {esc(b)}, {esc(b[0])}, 100, 1, 1, 10, 100, {esc(logo)}, {esc(banner)}, {esc(b + ' brand story')});")
            brand_map[b] = brand_id
            brand_id += 1
    sql.append("")

    # 3. Categories
    categories = {
        "Electronics": ["Smartphones", "Laptops", "Headphones", "Smartwatches", "Cameras"],
        "Fashion": ["Men's Clothing", "Women's Clothing", "Footwear", "Accessories"],
        "Home & Kitchen": ["Furniture", "Kitchen Appliances", "Home Appliances"],
        "Beauty & Personal Care": ["Skincare", "Hair Care", "Makeup", "Grooming"],
        "Sports & Fitness": ["Running", "Gym Equipment", "Yoga", "Outdoor"],
        "Books & Stationery": ["Fiction", "Non-Fiction", "Technology", "Notebooks"],
        "Grocery": ["Beverages", "Snacks", "Breakfast", "Pantry"],
        "Automotive": ["Car Accessories", "Car Care", "Replacement Parts"]
    }
    
    cat_map = {}
    cat_id = 1
    sql.append("-- Categories")
    for l0, l1_list in categories.items():
        l0_id = cat_id
        slug = l0.lower().replace(" ", "-").replace("&", "and")
        icon = f"{MINIO_BASE}/categories/{l0_id}-{slug}/icon.svg"
        sql.append(f"INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) VALUES ({l0_id}, NULL, {esc(l0)}, 0, 100, 'items', 1, 1, 100, {esc(icon)}, {esc(l0)}, {esc('All ' + l0)});")
        cat_map[l0] = l0_id
        cat_id += 1
        for l1 in l1_list:
            l1_id = cat_id
            l1_slug = l1.lower().replace(" ", "-").replace("&", "and")
            l1_icon = f"{MINIO_BASE}/categories/{l1_id}-{l1_slug}/icon.svg"
            sql.append(f"INSERT INTO pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) VALUES ({l1_id}, {l0_id}, {esc(l1)}, 1, 50, 'items', 1, 1, 100, {esc(l1_icon)}, {esc(l1)}, {esc(l1 + ' category')});")
            cat_map[l1] = {"id": l1_id, "parent": l0_id}
            cat_id += 1
    sql.append("")

    # 4. Product Attribute Categories
    attr_cats = {
        "Smartphone Specs": ["Color", "Storage", "RAM"],
        "Laptop Specs": ["Color", "Storage", "RAM"],
        "Audio Specs": ["Color"],
        "Apparel": ["Size", "Color"],
        "Footwear": ["Shoe Size", "Color"],
        "Beauty Shades": ["Shade"],
        "Books": ["Format"],
        "Grocery": ["Pack Size"],
        "Automotive": ["Volume"]
    }
    
    attr_cat_map = {}
    attr_map = {}
    attr_cat_id = 1
    attr_id = 1
    sql.append("-- Attribute Categories & Attributes")
    for ac_name, a_list in attr_cats.items():
        sql.append(f"INSERT INTO pms_product_attribute_category (id, name, attribute_count, param_count) VALUES ({attr_cat_id}, {esc(ac_name)}, {len(a_list)}, 0);")
        attr_cat_map[ac_name] = attr_cat_id
        for attr_name in a_list:
            # Type 0 = attribute (SKU variant)
            sql.append(f"INSERT INTO pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, filter_type, search_type, related_status, hand_add_status, type) VALUES ({attr_id}, {attr_cat_id}, {esc(attr_name)}, 2, 1, '', 100, 1, 1, 1, 0, 0);")
            attr_map[f"{ac_name}_{attr_name}"] = attr_id
            attr_id += 1
        attr_cat_id += 1
    sql.append("")

    # 5. Products & SKUs & Media
    product_templates = [
        {"cat": "Smartphones", "brand": "Apple", "name": "iPhone 15 Pro", "attr_cat": "Smartphone Specs", "price": 999, "variants": {"Color": ["Space Black", "Titanium", "Silver"], "Storage": ["128GB", "256GB", "512GB"], "RAM": ["8GB"]}},
        {"cat": "Smartphones", "brand": "Samsung", "name": "Galaxy S24 Ultra", "attr_cat": "Smartphone Specs", "price": 1199, "variants": {"Color": ["Titanium Gray", "Titanium Black"], "Storage": ["256GB", "512GB", "1TB"], "RAM": ["12GB"]}},
        {"cat": "Laptops", "brand": "Apple", "name": "MacBook Pro 16", "attr_cat": "Laptop Specs", "price": 2499, "variants": {"Color": ["Space Gray", "Silver"], "Storage": ["512GB", "1TB"], "RAM": ["16GB", "32GB"]}},
        {"cat": "Laptops", "brand": "Dell", "name": "XPS 15", "attr_cat": "Laptop Specs", "price": 1899, "variants": {"Color": ["Platinum Silver", "Frost"], "Storage": ["512GB", "1TB"], "RAM": ["16GB", "32GB"]}},
        {"cat": "Headphones", "brand": "Sony", "name": "WH-1000XM5", "attr_cat": "Audio Specs", "price": 398, "variants": {"Color": ["Black", "Silver", "Midnight Blue"]}},
        {"cat": "Headphones", "brand": "Apple", "name": "AirPods Pro 2", "attr_cat": "Audio Specs", "price": 249, "variants": {"Color": ["White"]}},
        {"cat": "Men's Clothing", "brand": "Levi's", "name": "501 Original Fit Jeans", "attr_cat": "Apparel", "price": 79.50, "variants": {"Color": ["Dark Wash", "Light Wash", "Black"], "Size": ["30x30", "32x32", "34x32"]}},
        {"cat": "Women's Clothing", "brand": "Zara", "name": "Linen Blend Dress", "attr_cat": "Apparel", "price": 59.90, "variants": {"Color": ["White", "Beige", "Olive"], "Size": ["XS", "S", "M", "L"]}},
        {"cat": "Footwear", "brand": "Nike", "name": "Air Max 270", "attr_cat": "Footwear", "price": 160, "variants": {"Color": ["Black/White", "Triple Black", "White/Volt"], "Shoe Size": ["8", "9", "10", "11"]}},
        {"cat": "Footwear", "brand": "Adidas", "name": "Ultraboost 1.0", "attr_cat": "Footwear", "price": 190, "variants": {"Color": ["Core Black", "Cloud White"], "Shoe Size": ["8", "9", "10", "11", "12"]}},
        {"cat": "Makeup", "brand": "Maybelline", "name": "Fit Me Matte + Poreless Foundation", "attr_cat": "Beauty Shades", "price": 8.99, "variants": {"Shade": ["110 Porcelain", "120 Classic Ivory", "220 Natural Beige", "330 Toffee"]}},
        {"cat": "Makeup", "brand": "L'Oréal", "name": "Voluminous Lash Paradise", "attr_cat": "Beauty Shades", "price": 11.99, "variants": {"Shade": ["Blackest Black", "Mystic Black", "Brown"]}},
        {"cat": "Technology", "brand": "O'Reilly", "name": "Designing Data-Intensive Applications", "attr_cat": "Books", "price": 45.99, "variants": {"Format": ["Paperback", "Kindle", "Hardcover"]}},
        {"cat": "Fiction", "brand": "Penguin", "name": "Dune", "attr_cat": "Books", "price": 18.00, "variants": {"Format": ["Paperback", "Hardcover", "Mass Market Paperback"]}},
        {"cat": "Beverages", "brand": "Coca-Cola", "name": "Coca-Cola Zero Sugar", "attr_cat": "Grocery", "price": 5.99, "variants": {"Pack Size": ["12-Pack Cans", "2-Liter Bottle", "6-Pack Bottles"]}},
        {"cat": "Snacks", "brand": "Kellogg's", "name": "Pringles Original", "attr_cat": "Grocery", "price": 2.49, "variants": {"Pack Size": ["Standard Can", "Snack Stack"]}},
        {"cat": "Car Care", "brand": "Castrol", "name": "EDGE 5W-30 Advanced Full Synthetic", "attr_cat": "Automotive", "price": 29.98, "variants": {"Volume": ["1 Quart", "5 Quart"]}},
        {"cat": "Furniture", "brand": "IKEA", "name": "BILLY Bookcase", "attr_cat": "Apparel", "price": 89.00, "variants": {"Color": ["White", "Black-brown", "Oak veneer"], "Size": ["31 1/2x11x79 1/2"]}}
    ]

    expanded_products = []
    expanded_products.extend(product_templates)
    
    for brand in brands["Electronics"]:
        if brand not in ["Apple", "Samsung"]:
            expanded_products.append({"cat": "Laptops", "brand": brand, "name": f"{brand} ProBook", "attr_cat": "Laptop Specs", "price": random.randint(800, 1500), "variants": {"Color": ["Silver", "Black"], "Storage": ["256GB", "512GB"], "RAM": ["8GB", "16GB"]}})
    for brand in brands["Fashion"]:
        if brand not in ["Nike", "Adidas", "Levi's", "Zara"]:
            expanded_products.append({"cat": "Men's Clothing", "brand": brand, "name": f"{brand} Essential Tee", "attr_cat": "Apparel", "price": random.randint(20, 50), "variants": {"Color": ["Black", "White", "Navy", "Gray"], "Size": ["S", "M", "L", "XL"]}})

    prod_id = 1
    sku_id = 1
    media_id = 1
    attr_val_id = 1
    
    sql.append("-- Products, SKUs, Media, Attribute Values")
    for p in expanded_products:
        c_info = cat_map[p["cat"]]
        b_id = brand_map[p["brand"]]
        ac_id = attr_cat_map[p["attr_cat"]]
        slug = p["name"].lower().replace(" ", "-").replace("+", "plus")
        pic = f"{MINIO_BASE}/products/{prod_id}-{slug}/main.webp"
        sn = f"{p['brand'][:3].upper()}-{p['name'][:5].upper()}-{prod_id}"
        
        sql.append(f"INSERT INTO pms_product (id, brand_id, product_category_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, sub_title, description, brand_name, product_category_name) VALUES ({prod_id}, {b_id}, {c_info['id']}, {ac_id}, {esc(p['name'])}, {esc(pic)}, {esc(sn)}, 0, 1, 1, 1, 1, 100, {random.randint(10, 1000)}, {esc('Premium ' + p['name'])}, {esc('High quality ' + p['name'] + ' by ' + p['brand'])}, {esc(p['brand'])}, {esc(p['cat'])});")
        
        sql.append(f"INSERT INTO pms_product_media (id, product_id, media_type, media_url, sort_order) VALUES ({media_id}, {prod_id}, 'IMAGE', {esc(pic)}, 0);")
        media_id += 1
        for i in range(1, 4):
            gal_pic = f"{MINIO_BASE}/products/{prod_id}-{slug}/gallery/0{i}.webp"
            sql.append(f"INSERT INTO pms_product_media (id, product_id, media_type, media_url, sort_order) VALUES ({media_id}, {prod_id}, 'IMAGE', {esc(gal_pic)}, {i});")
            media_id += 1

        import itertools
        keys = list(p["variants"].keys())
        val_lists = [p["variants"][k] for k in keys]
        combinations = list(itertools.product(*val_lists))
        
        for k, vals in p["variants"].items():
            a_id = attr_map[f"{p['attr_cat']}_{k}"]
            sql.append(f"INSERT INTO pms_product_attribute_value (id, product_id, product_attribute_id, value) VALUES ({attr_val_id}, {prod_id}, {a_id}, {esc(','.join(vals))});")
            attr_val_id += 1
            
        for combo in combinations:
            sku_code = f"{sn}-" + "-".join([str(x)[:3].upper().replace(" ", "") for x in combo])
            sp_data = [{"key": keys[i], "value": str(combo[i])} for i in range(len(keys))]
            sp_data_json = json.dumps(sp_data)
            
            sku_price = p["price"]
            if "512GB" in combo: sku_price += 200
            if "1TB" in combo: sku_price += 400
            if "32GB" in combo: sku_price += 300
            
            stock = random.randint(10, 500)
            sku_pic = f"{MINIO_BASE}/skus/{sku_code}/main.webp"
            
            sql.append(f"INSERT INTO pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, pic, sale, promotion_price, lock_stock, sku_attributes) VALUES ({sku_id}, {prod_id}, {esc(sku_code)}, {sku_price}, {stock}, 10, {esc(sku_pic)}, {random.randint(0, 50)}, {sku_price * 0.95}, 0, {esc(sp_data_json)}::jsonb);")
            sku_id += 1

        prod_id += 1
    sql.append("")

    # 6. Sys Banners
    sql.append("-- Banners")
    banners = [
        {"name": "Electronics Mega Sale", "pic": f"{MINIO_BASE}/marketing/banners/electronics-sale.webp", "url": "/category/1"},
        {"name": "Summer Fashion 2026", "pic": f"{MINIO_BASE}/marketing/banners/summer-fashion.webp", "url": "/category/4"},
        {"name": "Home Upgrade", "pic": f"{MINIO_BASE}/marketing/banners/home-upgrade.webp", "url": "/category/10"},
        {"name": "Beauty Essentials", "pic": f"{MINIO_BASE}/marketing/banners/beauty-essentials.webp", "url": "/category/14"}
    ]
    banner_id = 1
    for b in banners:
        sql.append(f"INSERT INTO sys_banner (id, name, type, pic, start_time, end_time, status, click_count, order_count, url, note, sort) VALUES ({banner_id}, {esc(b['name'])}, 1, {esc(b['pic'])}, '2025-01-01 00:00:00', '2026-12-31 23:59:59', 1, {random.randint(100, 1000)}, {random.randint(10, 100)}, {esc(b['url'])}, 'Promotional banner', 100);")
        banner_id += 1
        
    sql.append("\n-- Update sequences")
    sql.append(f"SELECT setval('pms_product_id_seq', {prod_id});")
    sql.append(f"SELECT setval('pms_sku_stock_id_seq', {sku_id});")
    sql.append(f"SELECT setval('sms_home_advertise_id_seq', {banner_id});")
    sql.append(f"SELECT setval('pms_brand_id_seq', {brand_id});")
    sql.append(f"SELECT setval('pms_product_category_id_seq', {cat_id});")
    sql.append(f"SELECT setval('pms_product_attribute_category_id_seq', {attr_cat_id});")
    sql.append(f"SELECT setval('pms_product_attribute_id_seq', {attr_id});")
    sql.append(f"SELECT setval('pms_product_attribute_value_id_seq', {attr_val_id});")
    sql.append(f"SELECT setval('pms_product_media_id_seq', {media_id});")

    # Write file
    with open('/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-data-jpa/src/main/resources/db/migration/V29__seed_full_catalog.sql', 'w') as f:
        f.write("\n".join(sql))

if __name__ == '__main__':
    generate()
