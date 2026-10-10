import os
import re

# Gather all known classes in domain
domain_classes = {}
base_dir = "NexusCore/nexus-domain/src/main/java/com/nexusengine/core/domain"
for root, dirs, files in os.walk(base_dir):
    for f in files:
        if f.endswith(".java"):
            class_name = f.replace(".java", "")
            rel_path = os.path.relpath(os.path.join(root, f), base_dir)
            pkg = "com.nexusengine.core.domain." + os.path.dirname(rel_path).replace("/", ".")
            fqn = pkg + "." + class_name
            domain_classes[class_name] = fqn

# Also converters
for root, dirs, files in os.walk("NexusCore/nexus-domain/src/main/java/com/nexusengine/core/domain/shared/converter"):
    for f in files:
        if f.endswith(".java"):
            class_name = f.replace(".java", "")
            domain_classes[class_name] = "com.nexusengine.core.domain.shared.converter." + class_name

# Add missing imports to all java files in NexusCore
for root, dirs, files in os.walk("NexusCore"):
    for file in files:
        if file.endswith(".java"):
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
            
            # Find all words that look like class names
            # Start with capital letter, followed by letters/numbers
            words = set(re.findall(r'\b[A-Z][a-zA-Z0-9_]*\b', content))
            
            # Find existing imports
            imports = set(re.findall(r'import\s+([\w.]+);', content))
            imported_classes = {imp.split(".")[-1] for imp in imports}
            
            # current package
            pkg_match = re.search(r'package\s+([\w.]+);', content)
            current_pkg = pkg_match.group(1) if pkg_match else ""
            
            added_imports = []
            for word in words:
                if word in domain_classes:
                    fqn = domain_classes[word]
                    # If it's not imported, and not in the same package, and not the current class itself
                    if word not in imported_classes and current_pkg != fqn.rsplit(".", 1)[0] and fqn != current_pkg + "." + file.replace(".java", ""):
                        added_imports.append(f"import {fqn};")
                        imported_classes.add(word)
            
            if added_imports:
                # Add them after the package declaration
                if pkg_match:
                    new_imports_str = "\n".join(added_imports)
                    content = content.replace(pkg_match.group(0), pkg_match.group(0) + "\n\n" + new_imports_str)
                    with open(filepath, "w") as f:
                        f.write(content)
                        
print("Done adding missing imports.")
