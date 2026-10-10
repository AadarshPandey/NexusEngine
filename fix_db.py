import json

with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

sql = []

for data in mappings.values():
    new_schema = data["new_schema"]
    new_name = data["new_name"]
    
    for old_col, c_map in data.get("columns", {}).items():
        new_col = c_map["new_name"]
        ctype = c_map.get("type", "")
        
        if ctype == "boolean":
            sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} DROP DEFAULT;")
            sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} TYPE boolean USING CASE WHEN {new_col}::int=1 THEN true ELSE false END;")
            sql.append(f"ALTER TABLE {new_schema}.{new_name} ALTER COLUMN {new_col} SET DEFAULT false;")

with open("fix_db.sql", "w") as f:
    f.write("\n".join(sql))
