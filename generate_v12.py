import json
import re

with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

sql = []
sql.append("-- V12__schema_redesign.sql")
sql.append("-- Preserves data while applying Phase 1-4 Schema Redesign")
sql.append("")

schemas = set([m["new_schema"] for m in mappings.values()])
for schema in schemas:
    sql.append(f"CREATE SCHEMA IF NOT EXISTS {schema};")

sql.append("")

for table, data in mappings.items():
    old_schema = data["old_schema"]
    old_name = data["old_name"]
    new_schema = data["new_schema"]
    new_name = data["new_name"]

    if old_schema != new_schema:
        sql.append(f"ALTER TABLE {old_schema}.{old_name} SET SCHEMA {new_schema};")
    
    if old_name != new_name:
        sql.append(f"ALTER TABLE {new_schema}.{old_name} RENAME TO {new_name};")

    for old_col, c_map in data.get("columns", {}).items():
        new_col = c_map["new_name"]
        
        # some old_cols had markdown like **name**, let's filter
        if not re.match(r'^[a-zA-Z_$][a-zA-Z0-9_$]*$', old_col):
            continue

        if old_col != new_col:
            sql.append(f"ALTER TABLE {new_schema}.{new_name} RENAME COLUMN {old_col} TO {new_col};")
        
        ctype = c_map.get("type", "")
        full_type = c_map.get("full_type", "")
        
        # boolean casting
        if ctype == "boolean":
            sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} TYPE boolean USING CASE WHEN {new_col}=1 THEN true ELSE false END;")
        
        # smallint casting
        elif ctype == "smallint":
            sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} TYPE smallint;")

        elif ctype == "timestamptz":
            if new_col == "deleted_at":
                sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} TYPE timestamptz USING CASE WHEN {new_col}=1 THEN CURRENT_TIMESTAMP ELSE NULL END;")
            else:
                sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} TYPE timestamptz USING {new_col} AT TIME ZONE 'UTC';")

    # Phase 2: Add version column if it was one of the 4 entities
    if new_name in ["product", "product_sku", "customer_order", "coupon"]:
        # ensure idempotency with IF NOT EXISTS (postgres 11+ doesn't have it for ADD COLUMN easily but it's V12 so it runs once)
        sql.append(f"ALTER TABLE {new_schema}.{new_name} ADD COLUMN version bigint DEFAULT 0 NOT NULL;")

sql.append("")
sql.append("-- Phase 3: Drops")
sql.append("ALTER TABLE catalog.product DROP COLUMN IF EXISTS brand_name;")
sql.append("ALTER TABLE catalog.product DROP COLUMN IF EXISTS product_category_name;")
sql.append("ALTER TABLE catalog.product_sku DROP COLUMN IF EXISTS sp1;")
sql.append("ALTER TABLE catalog.product_sku DROP COLUMN IF EXISTS sp2;")
sql.append("ALTER TABLE catalog.product_sku DROP COLUMN IF EXISTS sp3;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_pic;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_name;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_brand;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_sn;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_sub_title;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_sku_code;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS product_category_id;")
# cart item hard delete
sql.append("DELETE FROM ordering.cart_item WHERE delete_status = 1;")
sql.append("ALTER TABLE ordering.cart_item DROP COLUMN IF EXISTS delete_status;")


with open("NexusCore/nexus-data-jpa/src/main/resources/db/migration/V12__schema_redesign.sql", "w") as f:
    f.write("\n".join(sql))

print("Generated V12__schema_redesign.sql")
