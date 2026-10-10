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
            
            # fix lombok annotations
            lombok_annotations = ["Slf4j", "RequiredArgsConstructor", "Getter", "Setter", "Data", "Builder", "NoArgsConstructor", "AllArgsConstructor"]
            
            for ann in lombok_annotations:
                if f"@lombok.extern.slf4j.{ann}" in content:
                    content = content.replace(f"@lombok.extern.slf4j.{ann}", f"@{ann}")
                    if f"import lombok.extern.slf4j.{ann};" not in content:
                        content = re.sub(r'package (.*?);\n', f'package \\1;\n\nimport lombok.extern.slf4j.{ann};\n', content, count=1)
                        
                if f"@lombok.{ann}" in content:
                    content = content.replace(f"@lombok.{ann}", f"@{ann}")
                    if f"import lombok.{ann};" not in content:
                        content = re.sub(r'package (.*?);\n', f'package \\1;\n\nimport lombok.{ann};\n', content, count=1)
            
            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)

process_module("nexus-admin-api/src/main/java")
process_module("nexus-portal-api/src/main/java")
