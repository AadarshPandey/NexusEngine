import os
import re

os.chdir("NexusCore")
base_src = "nexus-search/src/main/java/com/nexusengine/core/search"
moves = [
    ("controller/EsProductController.java", "controller/ProductSearchController.java", "EsProductController", "ProductSearchController"),
    ("dao/EsProductDao.java", "dao/ProductSearchDao.java", "EsProductDao", "ProductSearchDao"),
    ("domain/EsProduct.java", "domain/ProductDocument.java", "EsProduct", "ProductDocument"),
    ("domain/EsProductAttributeValue.java", "domain/ProductAttributeValueDoc.java", "EsProductAttributeValue", "ProductAttributeValueDoc"),
    ("domain/EsProductRelatedInfo.java", "domain/ProductRelatedInfo.java", "EsProductRelatedInfo", "ProductRelatedInfo"),
    ("repository/EsProductRepository.java", "repository/ProductSearchRepository.java", "EsProductRepository", "ProductSearchRepository"),
    ("service/EsProductService.java", "service/ProductSearchService.java", "EsProductService", "ProductSearchService"),
    ("service/impl/EsProductServiceImpl.java", "service/impl/ProductSearchServiceImpl.java", "EsProductServiceImpl", "ProductSearchServiceImpl")
]

class_renames = {old_c: new_c for _, _, old_c, new_c in moves}

# First, move files and replace class declarations inside them
for old_rel, new_rel, old_c, new_c in moves:
    old_path = os.path.join(base_src, old_rel)
    new_path = os.path.join(base_src, new_rel)
    
    if os.path.exists(old_path):
        os.makedirs(os.path.dirname(new_path), exist_ok=True)
        os.rename(old_path, new_path)
        print(f"Moved {old_path} -> {new_path}")

# Second, update all files in nexus-search to replace old class names with new class names
for root, dirs, files in os.walk(base_src):
    for file in files:
        if not file.endswith(".java"): continue
        filepath = os.path.join(root, file)
        with open(filepath, "r") as f:
            content = f.read()
            
        orig = content
        
        for old_c, new_c in class_renames.items():
            content = re.sub(r'\b' + old_c + r'\b', new_c, content)
            
        if content != orig:
            with open(filepath, "w") as f:
                f.write(content)
