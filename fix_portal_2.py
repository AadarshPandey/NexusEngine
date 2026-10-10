import os

replacements = {
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/HomeServiceImpl.java": {
        "findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual": "findFirstByIsActiveAndStartsAtLocalLessThanEqualAndEndsAtLocalGreaterThanEqual"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java": {
        "findByMemberIdAndUseStatus": "findByMemberIdAndStatus"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java": {
        "findByParentIdAndStatus": "findByParentReviewIdAndModerationStatus"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberCouponServiceImpl.java": {
        "findByMemberIdAndUseStatus": "findByMemberIdAndStatus"
    }
}

for filepath, repls in replacements.items():
    if os.path.exists(filepath):
        with open(filepath, 'r') as f:
            content = f.read()
        for old, new in repls.items():
            content = content.replace(old, new)
        with open(filepath, 'w') as f:
            f.write(content)
print("Portal files 2 updated.")
