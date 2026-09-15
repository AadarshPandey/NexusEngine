import re

# Read the output of grep
with open('fks.txt', 'r') as f:
    lines = f.readlines()

tables = {}

for line in lines:
    match = re.search(r'model/([^.]+)\.java:\s*@Column\(name = "([^"]+)"\)', line)
    if match:
        entity = match.group(1)
        column = match.group(2)
        
        # Convert entity CamelCase to snake_case table name
        table_name = re.sub(r'(?<!^)(?=[A-Z])', '_', entity).lower()
        
        # Guess the target table from the column name (e.g. member_id -> ums_member, product_id -> pms_product)
        # This requires some heuristics because member_id could be ums_member, role_id could be ums_role, etc.
        # Let's map prefixes
        target_table = None
        if column == "member_id": target_table = "ums_member"
        elif column == "admin_id": target_table = "ums_admin"
        elif column == "role_id": target_table = "ums_role"
        elif column == "resource_id": target_table = "ums_resource"
        elif column == "product_id": target_table = "pms_product"
        elif column == "brand_id": target_table = "pms_brand"
        elif column == "product_category_id": target_table = "pms_product_category"
        elif column == "order_id": target_table = "oms_order"
        elif column == "coupon_id": target_table = "sms_coupon"
        elif column == "subject_id": target_table = "cms_subject"
        
        if target_table:
            if table_name not in tables:
                tables[table_name] = []
            tables[table_name].append((column, target_table))

sql = "-- V3__add_all_foreign_keys.sql\n\n"
for table, fks in tables.items():
    for column, target in fks:
        # Check if we already added it in V2
        if table == "ums_admin_role_relation" and column in ["admin_id", "role_id"]: continue
        if table == "ums_role_resource_relation" and column in ["role_id", "resource_id"]: continue
        if table == "pms_product" and column in ["brand_id", "product_category_id"]: continue
        if table == "pms_sku_stock" and column == "product_id": continue
        if table == "oms_order_item" and column == "order_id": continue
        if table == "oms_cart_item" and column == "member_id": continue
        if table == "pms_review" and column in ["product_id", "member_id"]: continue
        
        fk_name = f"fk_{table}_{column}"
        sql += f"ALTER TABLE {table} ADD CONSTRAINT {fk_name} FOREIGN KEY ({column}) REFERENCES {target}(id) ON DELETE CASCADE;\n\n"

with open('NexusCore/nexus-data-jpa/src/main/resources/db/migration/V3__add_all_foreign_keys.sql', 'w') as f:
    f.write(sql)
