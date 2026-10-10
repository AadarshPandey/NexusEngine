import os
import re

os.chdir("NexusCore")
def process_module(module_path, prefix):
    # Get all context wildcards
    packages = []
    base_dir = f"{module_path.split('/')[0]}/src/main/java/com/nexusengine/core/{prefix}"
    if os.path.exists(base_dir):
        for item in os.listdir(base_dir):
            if os.path.isdir(os.path.join(base_dir, item)):
                if os.path.exists(os.path.join(base_dir, item, "dto")):
                    packages.append(f"com.nexusengine.core.{prefix}.{item}.dto.*")
                if os.path.exists(os.path.join(base_dir, item, "service")):
                    packages.append(f"com.nexusengine.core.{prefix}.{item}.service.*")
                if os.path.exists(os.path.join(base_dir, item, "dao")):
                    packages.append(f"com.nexusengine.core.{prefix}.{item}.dao.*")
                if os.path.exists(os.path.join(base_dir, item, "strategy")):
                    packages.append(f"com.nexusengine.core.{prefix}.{item}.strategy.*")
                if os.path.exists(os.path.join(base_dir, item, "controller")):
                    packages.append(f"com.nexusengine.core.{prefix}.{item}.controller.*")
                    
    # Also include domain entities and repos for tests
    domain_pkgs = [
        "com.nexusengine.core.domain.catalog.entity.*",
        "com.nexusengine.core.domain.ordering.entity.*",
        "com.nexusengine.core.domain.customer.entity.*",
        "com.nexusengine.core.domain.promotion.entity.*",
        "com.nexusengine.core.domain.iam.entity.*",
        "com.nexusengine.core.domain.catalog.repository.*",
        "com.nexusengine.core.domain.ordering.repository.*",
        "com.nexusengine.core.domain.customer.repository.*",
        "com.nexusengine.core.domain.promotion.repository.*",
        "com.nexusengine.core.domain.iam.repository.*",
    ]
    wildcards = "\n".join([f"import {p};" for p in domain_pkgs + packages]) + "\n"
    
    bad_prefixes = [
        f"import com.nexusengine.core.model.",
        f"import com.nexusengine.core.repository.",
        f"import com.nexusengine.core.service.",
        f"import com.nexusengine.core.dto.",
        f"import com.nexusengine.core.{prefix}.domain.",
        f"import com.nexusengine.core.{prefix}.service.",
        f"import com.nexusengine.core.{prefix}.dao.",
        f"import com.nexusengine.core.{prefix}.strategy.",
        f"import com.nexusengine.core.{prefix}.component.",
    ]
    
    for root, dirs, files in os.walk(module_path):
        for file in files:
            if not file.endswith(".java"): continue
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig = content
            
            lines = content.split("\n")
            new_lines = []
            for line in lines:
                if any(line.startswith(bp) for bp in bad_prefixes):
                    continue
                # strip FQN inline
                if not line.startswith("import ") and not line.startswith("package "):
                    line = line.replace("com.nexusengine.core.model.", "")
                    line = line.replace("com.nexusengine.core.repository.", "")
                    line = line.replace(f"com.nexusengine.core.{prefix}.service.", "")
                    line = line.replace(f"com.nexusengine.core.{prefix}.domain.", "")
                    line = line.replace("StagedEventProcessor", "OutboxEventProcessor")
                new_lines.append(line)
            
            content = "\n".join(new_lines)
            
            # insert new wildcards
            imports_end = content.find("import ")
            if imports_end != -1:
                end_of_line = content.find("\n", imports_end)
                content = content[:end_of_line+1] + wildcards + content[end_of_line+1:]

            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)

process_module("nexus-admin-api/src/test/java", "admin")
process_module("nexus-portal-api/src/test/java", "portal")
