import subprocess
import json

def run_query(query):
    result = subprocess.run(
        ["docker", "exec", "-i", "postgres", "psql", "-U", "postgres", "-d", "nexuscore", "-t", "-c", query],
        capture_output=True, text=True
    )
    if result.returncode != 0:
        print(f"Error running query: {query}")
        print(result.stderr)
        return []
    lines = [line.strip() for line in result.stdout.strip().split('\n') if line.strip()]
    data = []
    for line in lines:
        parts = line.split('|')
        data.append([p.strip() for p in parts])
    return data

# 1. Get all attributes
# id | product_attribute_category_id | name | type
attrs_raw = run_query("SELECT id, product_attribute_category_id, name, type FROM pms_product_attribute;")
attrs_by_cat = {}
for a in attrs_raw:
    if len(a) < 4: continue
    a_id, c_id, a_name, a_type = int(a[0]), int(a[1]), a[2], int(a[3])
    if c_id not in attrs_by_cat:
        attrs_by_cat[c_id] = []
    attrs_by_cat[c_id].append({'id': a_id, 'name': a_name, 'type': a_type})

# 2. Get all products
# id | product_attribute_category_id | name
prods_raw = run_query("SELECT id, product_attribute_category_id, name FROM pms_product;")

# 3. Get all SKUs to parse their jsonb
# product_id | sku_attributes
skus_raw = run_query("SELECT product_id, sku_attributes::text FROM pms_sku_stock WHERE sku_attributes IS NOT NULL ;")
sku_attrs_by_product = {}
for s in skus_raw:
    if len(s) < 2: continue
    p_id = int(s[0])
    try:
        j = json.loads(s[1])
    except:
        continue
    if p_id not in sku_attrs_by_product:
        sku_attrs_by_product[p_id] = {}
    for k, v in j.items():
        k_lower = k.lower().strip()
        if k_lower not in sku_attrs_by_product[p_id]:
            sku_attrs_by_product[p_id][k_lower] = set()
        sku_attrs_by_product[p_id][k_lower].add(str(v))

sql = ["TRUNCATE TABLE pms_product_attribute_value CASCADE;"]

val_id = 1
for p in prods_raw:
    if len(p) < 3: continue
    p_id = int(p[0])
    c_id_str = p[1]
    p_name = p[2]
    if not c_id_str:
        continue
    c_id = int(c_id_str)
    
    if c_id not in attrs_by_cat:
        continue
        
    for a in attrs_by_cat[c_id]:
        a_id = a['id']
        a_name = a['name']
        a_type = a['type']
        
        val = ""
        if a_type == 0:
            # SKU attribute. Let's see if we have variations in SKU JSON
            k_lower = a_name.lower().strip()
            # fuzzy match if exact doesn't exist
            matched_key = None
            if p_id in sku_attrs_by_product:
                for k in sku_attrs_by_product[p_id]:
                    if k == k_lower or k_lower in k or k in k_lower:
                        matched_key = k
                        break
            if matched_key:
                val = ",".join(sorted(list(sku_attrs_by_product[p_id][matched_key])))
            else:
                val = "Standard"
        else:
            # Parameter attribute (type=1)
            # Generate a sensible dummy value
            if 'size' in a_name.lower() or 'dimension' in a_name.lower():
                val = "Standard Size"
            elif 'processor' in a_name.lower():
                val = "Latest Gen Processor"
            elif 'water' in a_name.lower() or 'gps' in a_name.lower() or 'anc' in a_name.lower():
                val = "Yes"
            elif 'type' in a_name.lower() or 'material' in a_name.lower() or 'ingredient' in a_name.lower():
                val = "Premium Material"
            elif 'battery' in a_name.lower():
                val = "24 Hours"
            elif 'resolution' in a_name.lower():
                val = "4K UHD"
            elif 'camera' in a_name.lower():
                val = "Advanced Dual Camera"
            elif 'finish' in a_name.lower():
                val = "Matte Finish"
            else:
                val = "Standard"
                
        # Escape quotes
        val = val.replace("'", "''")
        
        sql.append(f"INSERT INTO pms_product_attribute_value (id, product_id, product_attribute_id, value) VALUES ({val_id}, {p_id}, {a_id}, '{val}');")
        val_id += 1

sql.append("SELECT setval('pms_product_attribute_value_id_seq', (SELECT COALESCE(MAX(id), 1) FROM pms_product_attribute_value));")

with open("/home/aadarsh/Documents/NexusEngine/scratch/attr_values.sql", 'w') as f:
    f.write('\n'.join(sql))
print(f"Generated {val_id-1} attribute values SQL.")
