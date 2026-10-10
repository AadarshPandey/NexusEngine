import os

files_to_fix = [
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/HomeServiceImpl.java",
    "NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductCategoryServiceImpl.java",
    "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/PromotionalBannerRepository.java",
    "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/PmsProductCategoryRepository.java"
]

for filepath in files_to_fix:
    with open(filepath, 'r') as f:
        content = f.read()
    
    content = content.replace("OrderBySortDesc", "OrderByDisplayOrderDesc")
    content = content.replace("findByTypeAndStatusOrderBy", "findByPlacementAndIsActiveOrderBy")
    content = content.replace("findByStatusOrderBy", "findByIsActiveOrderBy")
    
    with open(filepath, 'w') as f:
        f.write(content)

print("Replacement complete.")
