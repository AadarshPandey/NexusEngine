import os
import json
import re

with open("phase4_renames.json", "r") as f:
    renames = json.load(f)

dirs = ["NexusAdminPanel/src", "NexusFrontendWeb/src"]

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
    with open(filepath, 'r') as f:
        content = f.read()
    
    orig = content
    for r in safe_renames:
        old_c = r["old_camel"]
        new_c = r["new_camel"]
        content = re.sub(r'\b' + old_c + r'\b', new_c, content)
        
    if content != orig:
        with open(filepath, 'w') as f:
            f.write(content)

for d in dirs:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith(('.ts', '.tsx', '.js', '.jsx', '.vue')):
                process_file(os.path.join(root, file))

print("Safe frontend fields renamed!")
