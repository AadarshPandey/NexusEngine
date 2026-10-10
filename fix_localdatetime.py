import os
import re

root_dir = "NexusCore"

for root, _, files in os.walk(root_dir):
    for file in files:
        if file.endswith(".java"):
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig_content = content
            
            # Replace java.time.LocalDateTime.now() with java.time.OffsetDateTime.now() in setters
            content = re.sub(r'set([A-Za-z0-9_]+)\(java\.time\.LocalDateTime\.now\(\)\)', r'set\1(java.time.OffsetDateTime.now())', content)
            
            # Replace LocalDateTime.now() with OffsetDateTime.now() in setters
            if re.search(r'set([A-Za-z0-9_]+)\(LocalDateTime\.now\(\)\)', content):
                content = re.sub(r'set([A-Za-z0-9_]+)\(LocalDateTime\.now\(\)\)', r'set\1(OffsetDateTime.now())', content)
                # Ensure import is present
                if "import java.time.OffsetDateTime;" not in content:
                    content = content.replace("import java.time.LocalDateTime;", "import java.time.LocalDateTime;\nimport java.time.OffsetDateTime;")
            
            if content != orig_content:
                with open(filepath, "w") as f:
                    f.write(content)
                print(f"Fixed {filepath}")

print("Done")
