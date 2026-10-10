import os
import re

os.chdir("NexusCore")
def process_module(module_path):
    for root, dirs, files in os.walk(module_path):
        for file in files:
            if not file.endswith(".java"): continue
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig = content
            
            # Remove FQN prefixes that are no longer valid, relying on wildcard imports
            # Note: We should NOT replace it inside import statements!
            
            lines = content.split("\n")
            for i, line in enumerate(lines):
                if line.startswith("import "): continue
                if line.startswith("package "): continue
                
                line = line.replace("com.nexusengine.core.model.", "")
                line = line.replace("com.nexusengine.core.repository.", "")
                line = line.replace("com.nexusengine.core.service.", "")
                line = line.replace("com.nexusengine.core.dto.", "")
                lines[i] = line
                
            content = "\n".join(lines)
            
            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)
                print(f"Removed FQN in {filepath}")

process_module("nexus-admin-api/src/main/java")
process_module("nexus-portal-api/src/main/java")
