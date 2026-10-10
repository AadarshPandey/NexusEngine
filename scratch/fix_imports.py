import re
import os

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

renames = []
for line in content.split("\n"):
    if line.startswith("|") and "`" in line:
        parts = [p.strip().strip("`") for p in line.split("|") if p.strip()]
        if len(parts) >= 3 and parts[1].endswith(".java"):
            renames.append((parts[1], parts[2]))

mapping = []
for old_path, new_path in renames:
    old_class = os.path.basename(old_path).replace(".java", "")
    new_class = os.path.basename(new_path).replace(".java", "")
    
    # Old fully qualified
    if old_path.startswith("model/"):
        old_fqn = "com.nexusengine.core.model." + old_class
    elif old_path.startswith("repository/"):
        old_fqn = "com.nexusengine.core.repository." + old_class
    else:
        continue
        
    new_pkg = "com.nexusengine.core." + os.path.dirname(new_path).replace("/", ".")
    new_fqn = new_pkg + "." + new_class
    
    mapping.append((old_class, new_class, old_fqn, new_fqn, new_pkg))

# Sort mapping by length of old_class descending to avoid partial matches
mapping.sort(key=lambda x: len(x[0]), reverse=True)

def process_file(filepath):
    with open(filepath, "r") as f:
        content = f.read()
        
    orig_content = content
    
    for old_class, new_class, old_fqn, new_fqn, new_pkg in mapping:
        # replace import
        content = content.replace(f"import {old_fqn};", f"import {new_fqn};")
        
        # if the class name changed, we must replace it in the code
        if old_class != new_class:
            # Word boundary regex to replace class names
            content = re.sub(r'\b' + old_class + r'\b', new_class, content)
            
            # also replace variable names if they match the class name but camelCase
            old_var = old_class[0].lower() + old_class[1:]
            new_var = new_class[0].lower() + new_class[1:]
            if old_var != new_var:
                content = re.sub(r'\b' + old_var + r'\b', new_var, content)

    if orig_content != content:
        with open(filepath, "w") as f:
            f.write(content)

# Process all java files
for root, dirs, files in os.walk("NexusCore"):
    for file in files:
        if file.endswith(".java"):
            process_file(os.path.join(root, file))

print("Done fixing imports and usages.")
