import os
import re

base_src = "NexusCore/nexus-domain/src/main/java/com/nexusengine/core/domain"
base_test = "NexusCore/nexus-domain/src/test/java/com/nexusengine/core/repository"
new_test_base = "NexusCore/nexus-domain/src/test/java/com/nexusengine/core/domain"

# 1. find all new repositories in domain/
repo_mapping = {}
for root, dirs, files in os.walk(base_src):
    for f in files:
        if f.endswith("Repository.java"):
            # e.g. domain/catalog/repository/BrandRepository.java
            rel_path = os.path.relpath(os.path.join(root, f), base_src)
            # relative to domain/
            new_class_name = f.replace(".java", "")
            repo_mapping[new_class_name] = rel_path

# 2. find all test files
for f in os.listdir(base_test):
    if not f.endswith("RepositoryTest.java"):
        continue
    
    old_test_class = f.replace(".java", "")
    old_test_path = os.path.join(base_test, f)
    
    # We need to find which new class it corresponds to.
    # We know the logic: PmsBrandRepositoryTest -> BrandRepositoryTest.
    # The script in part 3.3 maps old -> new. We can parse the markdown again to get the exact mapping.
    pass

import sys

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as mf:
    content = mf.read()

old_to_new_test_path = {}
for line in content.split("\n"):
    if line.startswith("|") and "repository/" in line and "domain/" in line:
        parts = [p.strip().strip("`") for p in line.split("|") if p.strip()]
        if len(parts) >= 3 and parts[1].startswith("repository/"):
            old_repo = parts[1].replace("repository/", "").replace(".java", "")
            new_repo = os.path.basename(parts[2]).replace(".java", "")
            new_repo_path_rel = parts[2].replace("domain/", "") # e.g. catalog/repository/BrandRepository.java
            
            old_test = old_repo + "Test"
            new_test_class = new_repo + "Test"
            
            new_test_path_rel = new_repo_path_rel.replace(new_repo + ".java", new_test_class + ".java")
            
            old_to_new_test_path[old_test] = (new_test_class, new_test_path_rel)

for f in os.listdir(base_test):
    if not f.endswith("Test.java"):
        continue
    old_test_class = f.replace(".java", "")
    old_test_path = os.path.join(base_test, f)
    
    if old_test_class in old_to_new_test_path:
        new_test_class, new_test_path_rel = old_to_new_test_path[old_test_class]
        new_test_path = os.path.join(new_test_base, new_test_path_rel)
        
        os.makedirs(os.path.dirname(new_test_path), exist_ok=True)
        os.rename(old_test_path, new_test_path)
        print(f"Moved {old_test_class}.java -> {new_test_path_rel}")
        
        # update package and class name
        with open(new_test_path, "r") as tf:
            test_content = tf.read()
        
        new_package = "com.nexusengine.core.domain." + os.path.dirname(new_test_path_rel).replace("/", ".")
        test_content = re.sub(r'package\s+com\.nexusengine\.core\.repository;', f'package {new_package};', test_content)
        
        if old_test_class != new_test_class:
            test_content = re.sub(r'\b' + old_test_class + r'\b', new_test_class, test_content)
            
            # we also need to update the repository field if it exists
            # e.g., PmsBrandRepository pmsBrandRepository -> BrandRepository brandRepository
            # But maybe let's just do imports later with standard refactoring or regex
            pass
            
        with open(new_test_path, "w") as tf:
            tf.write(test_content)
    else:
        print(f"No mapping found for {old_test_class}")

print("Done moving test files.")
