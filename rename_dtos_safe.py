import os
import json
import re

with open("phase4_renames.json", "r") as f:
    renames = json.load(f)

dirs = ["NexusCore"]

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
            
        # VERY strict regex
        # 1. Private fields
        content = re.sub(r'\b(private|protected)\s+([A-Za-z0-9_<>,?\[\]]+)\s+' + old_c + r'\s*;', r'\1 \2 ' + new_c + r';', content)
        
        # 2. Getters
        content = re.sub(r'\bpublic\s+([A-Za-z0-9_<>,?\[\]]+)\s+get' + old_p + r'\s*\(', r'public \1 get' + new_p + r'(', content)
        content = re.sub(r'\bpublic\s+([A-Za-z0-9_<>,?\[\]]+)\s+is' + old_p + r'\s*\(', r'public \1 is' + new_p + r'(', content)
        
        # 3. Setters
        content = re.sub(r'\bpublic\s+void\s+set' + old_p + r'\s*\(', r'public void set' + new_p + r'(', content)
        
        # 4. MapStruct @Mapping
        content = re.sub(r'(@Mapping\s*\([^)]*?(?:source|target)\s*=\s*")' + old_c + r'(")', r'\1' + new_c + r'\2', content)
        
        # 5. @JsonProperty or @Schema if they explicitly refer to the name
        # Actually they usually don't need changing unless they are explicitly mapped.
        # Wait! Swagger annotations usually rely on field name, no need to touch them.

    if content != orig:
        with open(filepath, 'w') as f:
            f.write(content)

for d in dirs:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith('.java') and ('/dto/' in root or '/domain/' in root or '/param/' in root or 'Mapper' in file):
                process_file(os.path.join(root, file))

print("Safe DTOs renamed!")
