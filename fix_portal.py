import os

replacements = {
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/component/FlashSaleOrderReceiver.java": {
        "findByFlashPromotionIdAndFlashPromotionSessionIdAndProductId": "findByFlashSaleIdAndSlotIdAndProductId"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java": {
        "findByMemberIdAndProductIdAndProductSkuId": "findByMemberIdAndProductIdAndSkuId"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalProductServiceImpl.java": {
        "findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual": "findByIsActiveAndStartsOnLessThanEqualAndEndsOnGreaterThanEqual"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/FlashSaleWarmupServiceImpl.java": {
        "findByFlashPromotionIdAndFlashPromotionSessionId": "findByFlashSaleIdAndSlotId"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java": {
        "findByProductIdAndParentIdIsNullAndStatus": "findByProductIdAndParentReviewIdIsNullAndModerationStatus"
    },
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/HomeServiceImpl.java": {
        "findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual": "findByIsActiveAndStartsOnLessThanEqualAndEndsOnGreaterThanEqual",
        "findByFlashPromotionIdAndFlashPromotionSessionId": "findByFlashSaleIdAndSlotId"
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
print("Portal files updated.")
