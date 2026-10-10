import re
import os

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

base_test = "NexusCore/nexus-portal-api/src/test/java/com/nexusengine/core"

lines = content.split('\n')
in_part53 = False
moves = []

for line in lines:
    if "### 5.3 Portal Test Restructure" in line:
        in_part53 = True
        continue
    if in_part53 and "###" in line:
        break
        
    if in_part53 and line.startswith("|") and "`" in line:
        parts = [p.strip().strip("`") for p in line.split("|") if p.strip()]
        if len(parts) >= 2 and parts[0].endswith(".java"):
            moves.append((parts[0], parts[1]))

for old_rel, new_rel in moves:
    old_path = os.path.join(base_test, "portal", old_rel) if not old_rel.startswith("portal") else os.path.join(base_test, old_rel)
    new_path = os.path.join(base_test, new_rel)
    
    if os.path.exists(old_path):
        os.makedirs(os.path.dirname(new_path), exist_ok=True)
        os.rename(old_path, new_path)
        print(f"Moved {old_path} -> {new_path}")
        
        # update package
        with open(new_path, "r") as f:
            c = f.read()
            
        new_pkg = "com.nexusengine.core." + os.path.dirname(new_rel).replace("/", ".")
        c = re.sub(r'package\s+com\.nexusengine\.core\.portal\.[a-z/.]+;', f'package {new_pkg};', c)
        
        old_name = os.path.basename(old_rel).replace(".java", "")
        new_name = os.path.basename(new_rel).replace(".java", "")
        if old_name != new_name:
            c = re.sub(r'\b' + old_name + r'\b', new_name, c)
            
        with open(new_path, "w") as f:
            f.write(c)

print("Portal tests moved.")
