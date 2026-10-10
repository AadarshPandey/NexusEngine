import re
markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

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
        m_context = re.search(r'├── ([a-z]+)/', line)
        if m_context:
            current_context = m_context.group(1)
            continue
            
        m_layer = re.search(r'│   ├── ([a-z]+)/', line)
        if m_layer:
            current_layer = m_layer.group(1)
            continue
            
        m_layer2 = re.search(r'│   └── ([a-z]+)/', line)
        if m_layer2:
            current_layer = m_layer2.group(1)
            continue
            
        m_file = re.search(r'│   │   ├── ([A-Za-z0-9_]+)\.java\s+← was ([A-Za-z0-9_]+)', line)
        if not m_file:
            m_file = re.search(r'│   │   └── ([A-Za-z0-9_]+)\.java\s+← was ([A-Za-z0-9_]+)', line)
            
        if m_file:
            new_name = m_file.group(1)
            old_name = m_file.group(2)
            if current_layer:
                moves.append((old_name, new_name, current_context, current_layer))
print("Moves count:", len(moves))
print(moves[:5])
