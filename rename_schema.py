import os
import re

def process_file(filepath):
    if not os.path.exists(filepath): return
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    # Replacements:
    # 1. Java @Table(schema = "catalog") -> "pms"
    if filepath.endswith('.java'):
        new_content = re.sub(r'schema\s*=\s*"catalog"', 'schema = "pms"', content)
    # 2. SQL files: 
    # SCHEMA catalog -> SCHEMA pms
    # catalog. -> pms.
    # 'catalog' -> 'pms'
    elif filepath.endswith('.sql'):
        new_content = re.sub(r'\bcatalog\b', 'pms', content)
        new_content = re.sub(r"'catalog'", "'pms'", new_content)
        new_content = re.sub(r'\"catalog\"', '"pms"', new_content)
    else:
        new_content = content

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)

for root, dirs, files in os.walk("./NexusCore"):
    for file in files:
        if file.endswith(".java") or file.endswith(".sql"):
            process_file(os.path.join(root, file))

process_file("./resource_setup/postgres_data.sql")
