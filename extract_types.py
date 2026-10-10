import re
import json

file_path = "/home/aadarsh/Documents/NexusEngine/schema_redesign.md"
with open(file_path, "r") as f:
    content = f.read()

table_pattern = re.compile(r"#### `([a-zA-Z0-9_]+)\.([a-zA-Z0-9_]+)` → `([a-zA-Z0-9_]+)\.([a-zA-Z0-9_]+)`")
tables = {}

current_table = None

for line in content.split("\n"):
    m = table_pattern.search(line)
    if m:
        old_schema, old_name, new_schema, new_name = m.groups()
        current_table = {
            "old_schema": old_schema,
            "old_name": old_name,
            "new_schema": new_schema,
            "new_name": new_name,
            "columns": {}
        }
        tables[old_name] = current_table
        continue
    
    if current_table and line.startswith("| "):
        parts = [p.strip() for p in line.split("|")]
        if len(parts) >= 5:
            old_col_raw = parts[1]
            new_col_raw = parts[3]
            new_type_raw = parts[4]
            if old_col_raw in ("Old column", "---"):
                continue
            
            old_cols = [x.strip() for x in old_col_raw.split("/")]
            new_cols_raw = [x.strip() for x in new_col_raw.split("/")]
            
            for i, old_col in enumerate(old_cols):
                old_col_clean = old_col.replace(" ✓", "")
                if old_col_clean == "—" or "~~DROP~~" in new_col_raw:
                    continue
                
                if i < len(new_cols_raw):
                    new_col_clean = new_cols_raw[i].replace(" ✓", "").replace("**", "")
                    if new_col_clean != "—":
                        # find the type in new_type_raw
                        # Just grab the first word roughly as the type
                        type_match = re.search(r'([a-zA-Z0-9_]+)', new_type_raw)
                        col_type = type_match.group(1) if type_match else ""
                        
                        current_table["columns"][old_col_clean] = {
                            "new_name": new_col_clean,
                            "type": col_type,
                            "full_type": new_type_raw
                        }
                        
print(json.dumps(tables, indent=2))
