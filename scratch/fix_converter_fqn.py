import os

# Replace com.nexusengine.core.model.converter with com.nexusengine.core.domain.shared.converter in all java files
for root, dirs, files in os.walk("NexusCore"):
    for file in files:
        if file.endswith(".java"):
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig = content
            content = content.replace("com.nexusengine.core.model.converter.", "com.nexusengine.core.domain.shared.converter.")
            
            if orig != content:
                with open(filepath, "w") as f:
                    f.write(content)
                    print(f"Fixed {filepath}")
