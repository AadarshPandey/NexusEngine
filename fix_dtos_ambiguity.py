import re
import os
import json

with open("phase4_renames.json", "r") as f:
    renames = json.load(f)

# Build map of old_method -> list of possible new_methods
old_to_new_methods = {}
for r in renames:
    old_p = r["old_pascal"]
    new_p = r["new_pascal"]
    for prefix in ["get", "set", "is"]:
        old_m = prefix + old_p
        new_m = prefix + new_p
        if old_m not in old_to_new_methods:
            old_to_new_methods[old_m] = set()
        old_to_new_methods[old_m].add(new_m)

# Helper to find java file for a fully qualified class name
def find_java_file(fqn):
    # e.g., com.nexusengine.core.dto.SomeDTO
    parts = fqn.split('.')
    path_suffix = "/".join(parts) + ".java"
    # Search in NexusCore
    for root, dirs, files in os.walk("NexusCore"):
        for file in files:
            if file == parts[-1] + ".java":
                full_path = os.path.join(root, file)
                if path_suffix in full_path:
                    return full_path
    return None

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
        
        if i + 2 >= len(mvn_output):
            continue
            
        symbol_line = mvn_output[i+1]
        location_line = mvn_output[i+2]
        
        method_match = re.search(r'symbol:\s+method\s+([A-Za-z0-9_]+)', symbol_line)
        if not method_match:
            continue
        old_method = method_match.group(1)
        
        class_match = re.search(r'location:\s+(?:variable.*?of type|class|interface)\s+([A-Za-z0-9_.]+)', location_line)
        if not class_match:
            class_name = os.path.basename(filepath).replace(".java", "")
            class_fqn = None
        else:
            class_fqn = class_match.group(1)
            
        if old_method in old_to_new_methods:
            possible_new = old_to_new_methods[old_method]
            if len(possible_new) == 1:
                new_method = list(possible_new)[0]
            else:
                new_method = None
                if class_fqn:
                    # check which method exists in the target class
                    class_file = find_java_file(class_fqn)
                    if class_file and os.path.exists(class_file):
                        with open(class_file, "r") as cf:
                            class_content = cf.read()
                        valid_methods = []
                        for m in possible_new:
                            if m + "(" in class_content or m + " (" in class_content:
                                valid_methods.append(m)
                        if len(valid_methods) == 1:
                            new_method = valid_methods[0]
                if not new_method:
                    print(f"Ambiguous or unresolved: {old_method} in {class_fqn or 'this class'} at {filepath}:{line_num}")
                    continue
            
            if filepath not in fixes:
                fixes[filepath] = {}
            if line_num not in fixes[filepath]:
                fixes[filepath][line_num] = []
                
            fixes[filepath][line_num].append((old_method, new_method))

for filepath, line_fixes in fixes.items():
    if not os.path.exists(filepath):
        continue
        
    with open(filepath, "r") as f:
        lines = f.readlines()
        
    for line_num, replacements in line_fixes.items():
        idx = line_num - 1
        if idx < len(lines):
            for old_m, new_m in replacements:
                lines[idx] = re.sub(r'\b' + old_m + r'\b', new_m, lines[idx])
                
    with open(filepath, "w") as f:
        f.writelines(lines)
        
    print(f"Fixed {len(line_fixes)} DTO lines in {filepath}")

