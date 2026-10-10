import os
import re

directory = 'NexusCore/nexus-admin/src/main/java/com/nexusengine/core/controller'

pattern = re.compile(r'@PathVariable\s+([A-Z][a-zA-Z0-9_<>]*)\s+([a-zA-Z0-9_]+)')

for root, dirs, files in os.walk(directory):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r') as f:
                content = f.read()
            
            # Find all @PathVariable <Type> <name>
            # Replace with @PathVariable("<name>") <Type> <name>
            # But only if it doesn't already have parentheses like @PathVariable("id")
            
            new_content = content
            # We want to replace `@PathVariable Long id` with `@PathVariable("id") Long id`
            # Note: We should avoid replacing @PathVariable("id") Long id which might be matched if we are not careful.
            
            # regex to match @PathVariable followed by space (no parenthesis), then type, then name
            # lookbehind or just simple replacement
            
            new_content = re.sub(r'@PathVariable\s+([A-Z][a-zA-Z0-9_<>]*)\s+([a-zA-Z0-9_]+)', r'@PathVariable("\2") \1 \2', new_content)
            
            if new_content != content:
                with open(filepath, 'w') as f:
                    f.write(new_content)
                print(f"Updated {filepath}")

