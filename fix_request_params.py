import os
import re

directory = 'NexusCore/nexus-admin/src/main/java/com/nexusengine/core/controller'

for root, dirs, files in os.walk(directory):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r') as f:
                content = f.read()
            
            # Find @RequestParam <Type> <name>
            new_content = re.sub(r'@RequestParam\s+([A-Z][a-zA-Z0-9_<>]*)\s+([a-zA-Z0-9_]+)', r'@RequestParam("\2") \1 \2', content)
            
            if new_content != content:
                with open(filepath, 'w') as f:
                    f.write(new_content)
                print(f"Updated RequestParams in {filepath}")

