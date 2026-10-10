import re
import os
import json

# 1. Load mappings
with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

def to_camel(s):
    parts = s.split('_')
    return parts[0] + ''.join(x.title() for x in parts[1:])

def to_pascal(s):
    parts = s.split('_')
    return ''.join(x.title() for x in parts)

# 2. Build class_method_map
# class_method_map["PmsSkuStock"]["setPrice"] = "setListPrice"
class_method_map = {}

for table, data in mappings.items():
    new_table_name = data["new_name"]
    # We don't have the Java class name directly, but we can guess it
    # PmsSkuStock etc. Actually, let's reverse from model_dir
    pass

# Better: parse java files to map class name to new_name
model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"
class_to_table = {}
for filename in os.listdir(model_dir):
    if not filename.endswith(".java"): continue
    filepath = os.path.join(model_dir, filename)
    with open(filepath, "r") as f:
        content = f.read()
    class_match = re.search(r'public\s+class\s+([A-Za-z0-9_]+)', content)
    table_match = re.search(r'@Table\(\s*name\s*=\s*"([^"]+)"', content)
    if class_match and table_match:
        class_to_table[class_match.group(1)] = table_match.group(1)

for class_name, table_name in class_to_table.items():
    mapping = None
    for m in mappings.values():
        if m["new_name"] == table_name:
            mapping = m
            break
    
    if not mapping: continue
    
    class_method_map[class_name] = {}
    for old_col, c_map in mapping.get("columns", {}).items():
        new_col = c_map["new_name"]
        
        old_camel = to_camel(old_col)
        new_camel = to_camel(new_col)
        
        if not re.match(r'^[a-zA-Z_$][a-zA-Z0-9_$]*$', old_camel):
            continue
            
        if old_camel != new_camel:
            old_get = "get" + to_pascal(old_col)
            new_get = "get" + to_pascal(new_col)
            old_set = "set" + to_pascal(old_col)
            new_set = "set" + to_pascal(new_col)
            
            class_method_map[class_name][old_get] = new_get
            class_method_map[class_name][old_set] = new_set

# Also some booleans use "is" prefix
for class_name, methods in list(class_method_map.items()):
    for old_m, new_m in list(methods.items()):
        if old_m.startswith("get"):
            is_old = "is" + old_m[3:]
            is_new = "is" + new_m[3:]
            class_method_map[class_name][is_old] = is_new


# 3. Read maven output from a file
with open("NexusCore/mvn_output.txt", "r") as f:
    mvn_output = f.readlines()

fixes = {}

for i, line in enumerate(mvn_output):
    if "COMPILATION ERROR" in line:
        continue
    
    match = re.search(r'\[ERROR\] (.*?\.java):\[(\d+),\d+\] cannot find symbol', line)
    if match:
        filepath = match.group(1)
        line_num = int(match.group(2))
        
        symbol_line = mvn_output[i+1]
        location_line = mvn_output[i+2]
        
        method_match = re.search(r'symbol:\s+method\s+([A-Za-z0-9_]+)', symbol_line)
        if not method_match:
            continue
        old_method = method_match.group(1)
        
        class_match = re.search(r'location:\s+(?:variable.*?of type|class|interface)\s+([A-Za-z0-9_.]+)', location_line)
        if not class_match:
            # Maybe the method is on the current class (this)
            # Find the class of the current file
            class_name = os.path.basename(filepath).replace(".java", "")
        else:
            class_fqn = class_match.group(1)
            class_name = class_fqn.split('.')[-1]
            
        if class_name in class_method_map and old_method in class_method_map[class_name]:
            new_method = class_method_map[class_name][old_method]
            
            if filepath not in fixes:
                fixes[filepath] = {}
            if line_num not in fixes[filepath]:
                fixes[filepath][line_num] = []
                
            fixes[filepath][line_num].append((old_method, new_method))

# 4. Apply fixes
for filepath, line_fixes in fixes.items():
    if not os.path.exists(filepath):
        continue
        
    with open(filepath, "r") as f:
        lines = f.readlines()
        
    for line_num, replacements in line_fixes.items():
        idx = line_num - 1
        if idx < len(lines):
            for old_m, new_m in replacements:
                # regex to replace method call
                lines[idx] = re.sub(r'\b' + old_m + r'\b', new_m, lines[idx])
                
    with open(filepath, "w") as f:
        f.writelines(lines)
        
    print(f"Fixed {len(line_fixes)} lines in {filepath}")

print("Done")
