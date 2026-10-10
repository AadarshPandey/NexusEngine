import re
import os

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

base_src = "NexusCore/nexus-admin-api/src/main/java/com/nexusengine/core"

lines = content.split('\n')
in_part42 = False
current_context = ""
current_layer = ""
moves = []

for line in lines:
    if "### 4.2 Target Structure" in line:
        in_part42 = True
        continue
    if in_part42 and "###" in line:
        break
        
    if in_part42:
        m_context = re.search(r'^├── ([a-z]+)/', line)
        if m_context:
            current_context = m_context.group(1)
            continue
            
        m_layer = re.search(r'^│   [├└]── ([a-z]+)/', line)
        if m_layer:
            current_layer = m_layer.group(1)
            continue
            
        m_file = re.search(r'^│   (?:│   |    )[├└]── ([A-Za-z0-9_]+)\.java\s+← was ([A-Za-z0-9_]+)', line)
            
        if m_file:
            new_name = m_file.group(1)
            old_name = m_file.group(2)
            if current_layer and current_context:
                moves.append((old_name, new_name, current_context, current_layer))
            
        m_kept = re.search(r'^│   (?:│   |    )[├└]── ([A-Za-z0-9_]+)\.java\s+← kept', line)
            
        if m_kept:
            name = m_kept.group(1)
            if current_layer and current_context:
                moves.append((name, name, current_context, current_layer))

# also validators - already moved

# additional services for infra - already moved

print(f"Total moves extracted: {len(moves)}")

for old_name, new_name, context, layer in moves:
    if layer == "service" and new_name.endswith("Impl"):
        old_path = os.path.join(base_src, "service/impl", f"{old_name}.java")
    else:
        old_path = os.path.join(base_src, layer, f"{old_name}.java")
        
    new_path = os.path.join(base_src, "admin", context, layer, f"{new_name}.java")
        
    if os.path.exists(old_path):
        os.makedirs(os.path.dirname(new_path), exist_ok=True)
        os.rename(old_path, new_path)
        print(f"Moved {old_path} -> {new_path}")
        
        # update package and class names
        with open(new_path, "r") as f:
            c = f.read()
            
        new_pkg = f"com.nexusengine.core.admin.{context}.{layer}"
        c = re.sub(r'package\s+com\.nexusengine\.core\.[a-z/.]+;', f'package {new_pkg};', c)
        
        if old_name != new_name:
            c = re.sub(r'\b' + old_name + r'\b', new_name, c)
            
        with open(new_path, "w") as f:
            f.write(c)

print("Admin services moved.")
