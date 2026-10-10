import re
import os

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

def parse_tree(content, section_header, target_prefix):
    lines = content.split('\n')
    in_section = False
    current_context = ""
    current_layer = ""
    moves = []

    for line in lines:
        if section_header in line:
            in_section = True
            continue
        if in_section and line.startswith("###"):
            break
            
        if in_section:
            m_context = re.search(r'^├── ([a-z]+)/', line)
            if m_context:
                current_context = m_context.group(1)
                continue
                
            m_layer = re.search(r'^│   ├── ([a-z]+)/', line)
            if m_layer:
                current_layer = m_layer.group(1)
                continue
                
            m_layer2 = re.search(r'^│   └── ([a-z]+)/', line)
            if m_layer2:
                current_layer = m_layer2.group(1)
                continue
                
            m_file = re.search(r'^│   │   ├── ([A-Za-z0-9_]+)\.java\s+← was ([A-Za-z0-9_]+)', line)
            if not m_file:
                m_file = re.search(r'^│   │   └── ([A-Za-z0-9_]+)\.java\s+← was ([A-Za-z0-9_]+)', line)
                
            if m_file:
                new_name = m_file.group(1)
                old_name = m_file.group(2)
                if current_layer and current_context:
                    moves.append((old_name, new_name, current_context, current_layer, target_prefix))
                
            m_kept = re.search(r'^│   │   ├── ([A-Za-z0-9_]+)\.java\s+← kept', line)
            if not m_kept:
                m_kept = re.search(r'^│   │   └── ([A-Za-z0-9_]+)\.java\s+← kept', line)
                
            if m_kept:
                name = m_kept.group(1)
                if current_layer and current_context:
                    moves.append((name, name, current_context, current_layer, target_prefix))
                    
    return moves

moves = parse_tree(content, "### 4.2 Target Structure", "admin")
moves += parse_tree(content, "### 5.2 Target Structure", "portal")

# additional infra for admin
infra_services = ["S3Service", "S3ServiceImpl"]
for svc in infra_services:
    moves.append((svc, svc, "infra", "service", "admin"))
    
# Create map of old class to (new class, new package)
rename_map = {}
for old, new, ctx, layer, prefix in moves:
    rename_map[old] = (new, f"com.nexusengine.core.{prefix}.{ctx}.{layer}")
    
# Process test directories
base_dirs = [
    "NexusCore/nexus-admin-api/src/test/java/com/nexusengine/core",
    "NexusCore/nexus-portal-api/src/test/java/com/nexusengine/core"
]

for base_test in base_dirs:
    if not os.path.exists(base_test):
        continue
        
    for root, dirs, files in list(os.walk(base_test)):
        for file in files:
            if not file.endswith("Test.java") and not file.endswith("Tests.java"):
                continue
                
            old_test_class = file[:-5]
            # determine corresponding main class
            # e.g., UmsRoleControllerTest -> UmsRoleController
            main_class_candidates = [
                old_test_class.replace("Test", ""),
                old_test_class.replace("IntegrationTest", ""),
                old_test_class.replace("Tests", "")
            ]
            
            main_class = None
            for cand in main_class_candidates:
                if cand in rename_map:
                    main_class = cand
                    break
                    
            if not main_class:
                print(f"Warning: Could not determine main class for test {file}")
                continue
                
            new_main_class, new_main_pkg = rename_map[main_class]
            new_test_class = old_test_class.replace(main_class, new_main_class)
            
            old_path = os.path.join(root, file)
            # The test package should mirror the main package
            new_pkg = new_main_pkg
            rel_path = new_pkg.replace("com.nexusengine.core.", "").replace(".", "/")
            new_path = os.path.join(base_test, rel_path, f"{new_test_class}.java")
            
            if old_path != new_path:
                os.makedirs(os.path.dirname(new_path), exist_ok=True)
                os.rename(old_path, new_path)
                print(f"Moved {old_path} -> {new_path}")
                
            # update file contents
            with open(new_path, "r") as f:
                c = f.read()
                
            # update package declaration
            c = re.sub(r'package\s+com\.nexusengine\.core\.[a-zA-Z0-9_.]+;', f'package {new_pkg};', c)
            
            # update imports and references
            if old_test_class != new_test_class:
                c = re.sub(r'\b' + old_test_class + r'\b', new_test_class, c)
            if main_class != new_main_class:
                c = re.sub(r'\b' + main_class + r'\b', new_main_class, c)
                
            # we also need to fix other renamed classes used in this test
            # using a simple replace for all known renames (order by length desc to avoid partial matches)
            sorted_renames = sorted(rename_map.items(), key=lambda x: len(x[0]), reverse=True)
            for old_c, (new_c, pkg) in sorted_renames:
                if old_c != new_c and old_c in c:
                    c = re.sub(r'\b' + old_c + r'\b', new_c, c)
                    
            with open(new_path, "w") as f:
                f.write(c)

print("Tests restructured.")
