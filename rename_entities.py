import os
import re
import json

with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

def to_camel(s):
    parts = s.split('_')
    return parts[0] + ''.join(x.title() for x in parts[1:])

def to_pascal(s):
    parts = s.split('_')
    return ''.join(x.title() for x in parts)

model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"

for filename in os.listdir(model_dir):
    if not filename.endswith(".java"): continue
    filepath = os.path.join(model_dir, filename)
    with open(filepath, "r") as f:
        content = f.read()

    # Identify class name
    class_match = re.search(r'public\s+class\s+([A-Za-z0-9_]+)', content)
    if not class_match:
        continue
    class_name = class_match.group(1)

    table_match = re.search(r'@Table\(\s*name\s*=\s*"([^"]+)"\s*,\s*schema\s*=\s*"([^"]+)"\s*\)', content)
    if not table_match:
        continue
    current_new_name = table_match.group(1)
    
    mapping = None
    for m in mappings.values():
        if m["new_name"] == current_new_name:
            mapping = m
            break
            
    if not mapping:
        continue
        
    # Rename fields inside the entity
    for old_col, c_map in mapping.get("columns", {}).items():
        new_col = c_map["new_name"]
        
        old_camel = to_camel(old_col)
        new_camel = to_camel(new_col)
        
        if not re.match(r'^[a-zA-Z_$][a-zA-Z0-9_$]*$', old_camel):
            continue
            
        if old_camel != new_camel:
            # Rename private field declaration
            # Must be careful to match exact field
            pattern = r'private\s+([A-Za-z0-9_<>.]+)\s+' + old_camel + r'\s*;'
            replacement = r'private \1 ' + new_camel + ';'
            content = re.sub(pattern, replacement, content)
            
            # Since lombok handles getters/setters, we only need to rename the field.
            # But what if there are manual getters/setters? 
            # We can also rename those if they exist in the file.
            old_get = "get" + to_pascal(old_col)
            new_get = "get" + to_pascal(new_col)
            old_set = "set" + to_pascal(old_col)
            new_set = "set" + to_pascal(new_col)
            
            content = re.sub(r'\b' + old_get + r'\b', new_get, content)
            content = re.sub(r'\b' + old_set + r'\b', new_set, content)

    with open(filepath, "w") as f:
        f.write(content)

print("Entities fields renamed!")
