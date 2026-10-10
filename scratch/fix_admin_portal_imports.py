import re
import os

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

renames = []
for line in content.split("\n"):
    if "← was " in line and ".java" in line:
        left, right = line.split("← was ")
        new_file = left.split("├──")[-1].strip()
        if not new_file:
            new_file = left.split("└──")[-1].strip()
        if not new_file:
            new_file = left.split("│")[-1].strip()
        # remove anything before last space in new_file just in case
        new_file = new_file.split()[-1]
        
        new_class = new_file.replace(".java", "")
        old_class = right.strip().replace(".java", "")
        
        renames.append((old_class, new_class))

os.chdir("NexusCore")
new_fqns = {}
for search_dir in ["nexus-admin-api/src/main/java", "nexus-portal-api/src/main/java"]:
    for root, dirs, files in os.walk(search_dir):
        for file in files:
            if file.endswith(".java"):
                class_name = file[:-5]
                pkg = root.replace("/", ".").split("src.main.java.")[-1]
                new_fqns[class_name] = f"{pkg}.{class_name}"

mapping = []
for old_class, new_class in renames:
    if new_class in new_fqns:
        mapping.append((old_class, new_class, new_fqns[new_class]))
    else:
        print(f"Warning: {new_class} not found in NexusCore")

mapping.sort(key=lambda x: len(x[0]), reverse=True)

def process_file(filepath):
    with open(filepath, "r") as f:
        content = f.read()
    orig_content = content
    
    for old_class, new_class, new_fqn in mapping:
        content = re.sub(r'import\s+[\w\.]+\.' + old_class + r'\s*;', f'import {new_fqn};', content)
        
        if old_class != new_class:
            content = re.sub(r'\b' + old_class + r'\b', new_class, content)
            old_var = old_class[0].lower() + old_class[1:]
            new_var = new_class[0].lower() + new_class[1:]
            if old_var != new_var:
                content = re.sub(r'\b' + old_var + r'\b', new_var, content)

    if orig_content != content:
        with open(filepath, "w") as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk("."):
    if "target" in root or ".git" in root:
        continue
    for file in files:
        if file.endswith(".java"):
            process_file(os.path.join(root, file))

print(f"Loaded {len(mapping)} mappings.")
