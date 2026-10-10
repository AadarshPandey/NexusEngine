import re
import os

markdown_file = "BackendMigrationPlan.md"
base_dir = "NexusCore/nexus-domain/src/main/java/com/nexusengine/core"
test_dir = "NexusCore/nexus-domain/src/test/java/com/nexusengine/core"

with open(markdown_file, "r") as f:
    content = f.read()

# Extract tables from part 3.2 and 3.3
# Each row has something like: | 1 | `model/PmsBrand.java` | `domain/catalog/entity/Brand.java` | `Brand` |
# Or for tests: | `repository/PmsBrandRepositoryTest.java` | `domain/catalog/repository/BrandRepositoryTest.java` |

renames = []
# Find all table rows
for line in content.split("\n"):
    if line.startswith("|") and "`" in line:
        parts = [p.strip().strip("`") for p in line.split("|") if p.strip()]
        if len(parts) >= 3 and (parts[1].endswith(".java") or (len(parts) == 2 and parts[0].endswith(".java"))):
            # tests table has 2 cols, others have 4 or more, wait tests has | Current Test Package | New Test Package |
            if len(parts) == 2 and parts[0].endswith(".java"):
                renames.append((parts[0], parts[1], True))
            elif len(parts) >= 3 and parts[1].endswith(".java"):
                renames.append((parts[1], parts[2], False))

for old_path, new_path, is_test in renames:
    src_base = test_dir if is_test else base_dir
    old_full = os.path.join(src_base, old_path)
    new_full = os.path.join(src_base, new_path)
    
    # If the file hasn't been moved yet but exists in model/ or repository/
    if os.path.exists(old_full):
        os.makedirs(os.path.dirname(new_full), exist_ok=True)
        os.rename(old_full, new_full)
        print(f"Moved {old_path} -> {new_path}")
        
        # We also need to update the package declaration and class name
        with open(new_full, "r") as f:
            java_content = f.read()
        
        # update package
        new_package = "com.nexusengine.core." + os.path.dirname(new_path).replace("/", ".")
        java_content = re.sub(r'package\s+com\.nexusengine\.core\.[a-z.]+;', f'package {new_package};', java_content)
        
        # update class name
        old_class = os.path.basename(old_path).replace(".java", "")
        new_class = os.path.basename(new_path).replace(".java", "")
        
        if old_class != new_class:
            java_content = re.sub(r'\b' + old_class + r'\b', new_class, java_content)
            
        with open(new_full, "w") as f:
            f.write(java_content)
    else:
        # maybe already moved?
        pass

print("Done moving files.")
