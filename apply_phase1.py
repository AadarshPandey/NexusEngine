import os
import re
import json

with open("mappings.json", "r") as f:
    mappings = json.load(f)

# Also add the join tables
mappings["ums_admin_role_relation"] = {
    "old_schema": "users", "old_name": "ums_admin_role_relation",
    "new_schema": "iam", "new_name": "admin_user_role", "columns": {}
}
mappings["ums_role_menu_relation"] = {
    "old_schema": "users", "old_name": "ums_role_menu_relation",
    "new_schema": "iam", "new_name": "role_menu", "columns": {}
}
mappings["ums_role_resource_relation"] = {
    "old_schema": "users", "old_name": "ums_role_resource_relation",
    "new_schema": "iam", "new_name": "role_permission", "columns": {}
}
mappings["sms_coupon_product_relation"] = {
    "old_schema": "marketing", "old_name": "sms_coupon_product_relation",
    "new_schema": "promotion", "new_name": "coupon_product", "columns": {}
}
mappings["sms_coupon_product_category_relation"] = {
    "old_schema": "marketing", "old_name": "sms_coupon_product_category_relation",
    "new_schema": "promotion", "new_name": "coupon_category", "columns": {}
}

for m in mappings.values():
    if "columns" in m:
        m["columns"] = {k: v for k, v in m["columns"].items() if not k.startswith("Old") and not k.startswith("Phase") and not k.startswith("`") and not k.startswith("**")}

def camel_to_snake(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()

model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"

for filename in os.listdir(model_dir):
    if not filename.endswith(".java"): continue
    filepath = os.path.join(model_dir, filename)
    with open(filepath, "r") as f:
        content = f.read()
    
    table_match = re.search(r'@Table\(\s*name\s*=\s*"([^"]+)"\s*(?:,\s*schema\s*=\s*"([^"]+)")?\s*\)', content)
    if not table_match:
        table_match = re.search(r'@Table\(\s*schema\s*=\s*"([^"]+)"\s*,\s*name\s*=\s*"([^"]+)"\s*\)', content)
        if table_match:
            old_schema = table_match.group(1)
            old_name = table_match.group(2)
        else:
            continue
    else:
        old_name = table_match.group(1)
        old_schema = table_match.group(2) if table_match.group(2) else ""

    if old_name not in mappings:
        continue
    
    mapping = mappings[old_name]
    new_schema = mapping["new_schema"]
    new_name = mapping["new_name"]
    
    new_table_str = f'@Table(name = "{new_name}", schema = "{new_schema}")'
    content = content.replace(table_match.group(0), new_table_str)
    
    field_pattern = re.compile(r'((?:@[A-Za-z0-9_.]+\s*(?:\([^)]*\))?\s*)*)(private\s+[A-Za-z0-9_<>]+\s+([A-Za-z0-9_]+)\s*;)')
    
    new_content = ""
    last_idx = 0
    
    for match in field_pattern.finditer(content):
        start = match.start()
        end = match.end()
        annotations = match.group(1)
        field_decl = match.group(2)
        field_name = match.group(3)
        
        # also match @jakarta.persistence.Column
        col_match = re.search(r'@[A-Za-z0-9_.]*Column\([^)]*name\s*=\s*"([^"]+)"[^)]*\)', annotations)
        if col_match:
            old_col = col_match.group(1)
        else:
            old_col = camel_to_snake(field_name)
            
        new_col = mapping["columns"].get(old_col)
        
        if new_col and new_col != old_col:
            if col_match:
                # Replace name="..." inside the @Column
                old_col_full = col_match.group(0)
                new_col_full = re.sub(r'name\s*=\s*"([^"]+)"', f'name = "{new_col}"', old_col_full)
                new_annotations = annotations.replace(old_col_full, new_col_full)
            else:
                if annotations:
                    new_annotations = annotations + f'    @Column(name = "{new_col}")\n'
                else:
                    new_annotations = f'    @Column(name = "{new_col}")\n'
                
                if not new_annotations.startswith("    ") and not new_annotations.startswith("\t"):
                    new_annotations = "    " + new_annotations.lstrip()
            
            new_field_str = new_annotations + field_decl
            
            new_content += content[last_idx:start] + new_field_str
            last_idx = end
        else:
            new_content += content[last_idx:end]
            last_idx = end
            
    new_content += content[last_idx:]
    
    with open(filepath, "w") as f:
        f.write(new_content)
    print(f"Updated {filename}")

print("Done")
