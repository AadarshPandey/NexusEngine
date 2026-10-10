import os

files_to_fix = [
    "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/PmsProductAttributeRepository.java",
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/PortalProductDao.java",
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalProductServiceImpl.java",
    "NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductAttributeServiceImpl.java",
    "NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductAttributeCategoryServiceImpl.java"
]

for filepath in files_to_fix:
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Order matters! Replace the longer one first
    content = content.replace("findByProductAttributeCategoryIdAndType", "findByProductTypeIdAndKind")
    content = content.replace("findByProductAttributeCategoryId", "findByProductTypeId")
    
    with open(filepath, 'w') as f:
        f.write(content)

print("Replacement complete.")
