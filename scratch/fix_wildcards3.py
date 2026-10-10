import os
import re

os.chdir("NexusCore")
def get_existing_packages(module, prefix):
    packages = []
    base_dir = f"{module}/src/main/java/com/nexusengine/core/{prefix}"
    if not os.path.exists(base_dir): return packages
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
    return packages

def process_module(module_path, prefix):
    pkgs = get_existing_packages(module_path.split("/")[0], prefix)
    wildcards = "\n".join([f"import {p};" for p in pkgs]) + "\n"
    
    bad_prefixes = [
        f"import com.nexusengine.core.portal.domain.",
        f"import com.nexusengine.core.portal.service.",
        f"import com.nexusengine.core.portal.dao.",
        f"import com.nexusengine.core.portal.strategy.",
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
                new_lines.append(line)
            
            content = "\n".join(new_lines)
            
            # remove old wildcards if they are there, we'll re-add
            for p in pkgs:
                content = content.replace(f"import {p};\n", "")
            
            imports_end = content.find("import ")
            if imports_end != -1:
                end_of_line = content.find("\n", imports_end)
                content = content[:end_of_line+1] + wildcards + content[end_of_line+1:]

            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)

process_module("nexus-portal-api/src/main/java", "portal")
