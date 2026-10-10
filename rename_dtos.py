import os
import json
import re

with open("phase4_renames.json", "r") as f:
    renames = json.load(f)

dirs = ["NexusCore"]

# We only rename fields, getters, setters, and method arguments (maybe).
# To be safe, we will just rename field declarations, getters, and setters.
# Also, we might have to rename MapStruct @Mapping(source="old", target="new")
# but mapstruct uses strings. We can just replace `"old_camel"` with `"new_camel"` in @Mapping

def process_file(filepath):
    if "/model/" in filepath:
        return
        
    with open(filepath, 'r') as f:
        content = f.read()
    
    orig = content
    for r in renames:
        old_c = r["old_camel"]
        new_c = r["new_camel"]
        old_p = r["old_pascal"]
        new_p = r["new_pascal"]
        
        if not re.match(r'^[a-zA-Z_$][a-zA-Z0-9_$]*$', old_c):
            continue
            
        # Field: private Type name;
        # Wait, could be protected or public or no modifier
        content = re.sub(r'(private|protected|public)?(\s+[A-Za-z0-9_<>,?\[\]\s]+?\s+)\b' + old_c + r'\b(\s*[;=])', r'\1\2' + new_c + r'\3', content)
        
        # Getter: public Type getName()
        content = re.sub(r'(public|protected|private)?(\s+[A-Za-z0-9_<>,?\[\]\s]+?\s+)\bget' + old_p + r'\b(\s*\()', r'\1\2get' + new_p + r'\3', content)
        content = re.sub(r'(public|protected|private)?(\s+[A-Za-z0-9_<>,?\[\]\s]+?\s+)\bis' + old_p + r'\b(\s*\()', r'\1\2is' + new_p + r'\3', content)
        
        # Setter: public void setName(Type name)
        content = re.sub(r'(public|protected|private)?(\s+(void|[A-Za-z0-9_<>,?\[\]]+)\s+)\bset' + old_p + r'\b(\s*\()', r'\1\2set' + new_p + r'\3', content)
        
        # In MapStruct annotations: @Mapping(source = "oldName", target = "...")
        content = re.sub(r'(@Mapping\s*\([^)]*?(?:source|target)\s*=\s*")' + old_c + r'(")', r'\1' + new_c + r'\2', content)

    if content != orig:
        with open(filepath, 'w') as f:
            f.write(content)

for d in dirs:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith('.java'):
                process_file(os.path.join(root, file))

print("DTOs and other Java fields renamed safely!")
