import os
import re
import json

with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"

for filename in os.listdir(model_dir):
    if not filename.endswith(".java"): continue
    filepath = os.path.join(model_dir, filename)
    with open(filepath, "r") as f:
        content = f.read()

    table_match = re.search(r'@Table\(\s*name\s*=\s*"([^"]+)"(?:\s*,\s*schema\s*=\s*"([^"]+)")?\s*\)', content)
    if not table_match:
        continue
        
    old_table = table_match.group(1)
    
    mapping = None
    for m in mappings.values():
        if m.get("old_name") == old_table:
            mapping = m
            break
            
    if not mapping:
        continue
        
    new_schema = mapping["new_schema"]
    new_table = mapping["new_name"]
    
    content = re.sub(
        r'@Table\(\s*name\s*=\s*"([^"]+)"(?:\s*,\s*schema\s*=\s*"([^"]+)")?\s*\)',
        f'@Table(name = "{new_table}", schema = "{new_schema}")',
        content
    )
    
    for old_col, c_map in mapping.get("columns", {}).items():
        new_col = c_map["new_name"]
        if old_col != new_col:
            if f'name = "{old_col}"' in content:
                content = re.sub(r'name\s*=\s*"' + old_col + r'"', f'name = "{new_col}"', content)
            else:
                parts = old_col.split('_')
                old_camel = parts[0] + ''.join(x.title() for x in parts[1:])
                # Safer regex, escape old_camel
                esc_camel = re.escape(old_camel)
                pattern = r'(private\s+[A-Za-z0-9_<>.\[\]]+\s+' + esc_camel + r'\s*;)'
                content = re.sub(pattern, f'@Column(name = "{new_col}")\n    \\1', content)

    with open(filepath, "w") as f:
        f.write(content)

print("Entities @Table and @Column renamed!")
