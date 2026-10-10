import json
import re

with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

def to_camel(s):
    parts = s.split('_')
    return parts[0] + ''.join(x.title() for x in parts[1:])

def to_pascal(s):
    parts = s.split('_')
    return ''.join(x.title() for x in parts)

replacements = []

for table, data in mappings.items():
    for old_col, c_map in data.get("columns", {}).items():
        new_col = c_map["new_name"]
        
        old_camel = to_camel(old_col)
        new_camel = to_camel(new_col)
        
        if old_camel != new_camel:
            replacements.append({
                "old_camel": old_camel,
                "new_camel": new_camel,
                "old_pascal": to_pascal(old_col),
                "new_pascal": to_pascal(new_col)
            })

# Remove duplicates
unique_replacements = []
seen = set()
for r in replacements:
    if (r["old_camel"], r["new_camel"]) not in seen:
        seen.add((r["old_camel"], r["new_camel"]))
        unique_replacements.append(r)

print(f"Found {len(unique_replacements)} field renames")

with open("phase4_renames.json", "w") as f:
    json.dump(unique_replacements, f, indent=2)
