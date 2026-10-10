import os
import json
import re

with open("phase4_renames.json", "r") as f:
    renames = json.load(f)

dirs = ["NexusCore"]

blacklist = ["type", "status", "name", "url", "note", "model", "ip", "address", "hidden"]

safe_renames = []
for r in renames:
    old_c = r["old_camel"]
    if not re.match(r'^[a-zA-Z_$][a-zA-Z0-9_$]*$', old_c):
        continue
    if old_c in blacklist:
        continue
    safe_renames.append(r)

def process_file(filepath):
    # skip model directory because rename_entities.py already handled it perfectly
    if "/model/" in filepath:
        return
        
    with open(filepath, 'r') as f:
        content = f.read()
    
    orig = content
    for r in safe_renames:
        old_c = r["old_camel"]
        new_c = r["new_camel"]
        old_p = r["old_pascal"]
        new_p = r["new_pascal"]
        
        # Replace fields, json keys
        content = re.sub(r'\b' + old_c + r'\b', new_c, content)
        # Replace getters/setters/class names
        content = re.sub(r'\b' + old_p + r'\b', new_p, content)
        
    if content != orig:
        with open(filepath, 'w') as f:
            f.write(content)

for d in dirs:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith('.java') or file.endswith('.xml'):
                process_file(os.path.join(root, file))

print("Safe java fields renamed!")
