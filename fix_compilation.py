import re

def replace_in_file(filepath, old, new):
    with open(filepath, 'r') as f:
        content = f.read()
    content = content.replace(old, new)
    with open(filepath, 'w') as f:
        f.write(content)

replace_in_file("./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/HomeServiceImpl.java", "getMediaList", "getGalleryList")
replace_in_file("./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalProductServiceImpl.java", "getMediaList", "getGalleryList")
replace_in_file("./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java", "setMediaList", "setAttachmentList")
replace_in_file("./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/domain/ReviewParam.java", "mediaList", "attachmentList")

