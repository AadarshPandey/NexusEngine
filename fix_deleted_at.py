import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    orig = content
    
    # 1. Update entities
    if filepath.endswith("PmsProduct.java") or filepath.endswith("OmsOrder.java"):
        content = re.sub(r'private Integer deletedAt;', 'private java.time.OffsetDateTime deletedAt;', content)
    
    # 2. Update setters and getters
    content = re.sub(r'\bsetDeletedAt\(0\)', 'setDeletedAt(null)', content)
    content = re.sub(r'\bsetDeletedAt\(1\)', 'setDeletedAt(java.time.OffsetDateTime.now())', content)
    content = re.sub(r'\bgetDeletedAt\(\)\s*==\s*0', 'getDeletedAt() == null', content)
    
    # PmsProductServiceImpl special case
    content = re.sub(
        r'p\.setDeletedAt\(deleteStatus\);',
        r'p.setDeletedAt(deleteStatus == 1 ? java.time.OffsetDateTime.now() : null);',
        content
    )
    
    if content != orig:
        with open(filepath, 'w') as f:
            f.write(content)
        print("Fixed", filepath)

for root, _, files in os.walk("NexusCore"):
    for file in files:
        if file.endswith(".java"):
            process_file(os.path.join(root, file))
