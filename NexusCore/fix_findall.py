import os

base = "/home/aadarsh/Documents/NexusEngine/NexusCore"

files_to_limit = [
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/OmsOrderReturnApplyServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsBrandServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsCouponHistoryServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsCouponServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsFlashPromotionServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsFlashPromotionSessionServiceImpl.java",
    "nexus-portal/src/main/java/com/nexusengine/core/portal/dao/PortalProductDao.java",
    "nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalProductServiceImpl.java",
    "nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsProductSemanticSearchServiceImpl.java"
]

files_to_comment_small = [
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/CmsPrefrenceAreaServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/CmsSubjectServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/OmsCompanyAddressServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/UmsMemberLevelServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/UmsMenuServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/UmsResourceServiceImpl.java",
    "nexus-admin/src/main/java/com/nexusengine/core/service/impl/UmsRoleServiceImpl.java"
]

files_to_comment_bulk = [
    "nexus-search/src/main/java/com/nexusengine/core/search/dao/EsProductDao.java"
]

import re

for rel in files_to_limit:
    path = os.path.join(base, rel)
    if not os.path.exists(path):
        continue
    with open(path, "r") as f:
        content = f.read()
    
    # Add import if missing
    if "import org.springframework.data.domain.PageRequest;" not in content:
        content = content.replace("import org.springframework.stereotype.Service;", "import org.springframework.stereotype.Service;\nimport org.springframework.data.domain.PageRequest;")
        
    # Replace .findAll() with .findAll(PageRequest.of(0, 1000)).getContent()
    content = re.sub(r'([a-zA-Z0-9_]+Repository)\.findAll\(\)', r'\1.findAll(PageRequest.of(0, 1000)).getContent()', content)
    
    with open(path, "w") as f:
        f.write(content)

for rel in files_to_comment_small:
    path = os.path.join(base, rel)
    if not os.path.exists(path):
        continue
    with open(path, "r") as f:
        content = f.read()
    
    # Add comment for small reference table
    content = re.sub(r'([a-zA-Z0-9_]+Repository)\.findAll\(\)', r'/* findAll() is acceptable for small reference tables */\n        \1.findAll()', content)
    
    with open(path, "w") as f:
        f.write(content)

for rel in files_to_comment_bulk:
    path = os.path.join(base, rel)
    if not os.path.exists(path):
        continue
    with open(path, "r") as f:
        content = f.read()
    
    # Add comment for bulk indexing
    content = re.sub(r'([a-zA-Z0-9_]+Repository)\.findAll\(\)', r'/* Intentional unbounded findAll for bulk ES indexing */ \1.findAll()', content)
    
    with open(path, "w") as f:
        f.write(content)

print("Done python script")
