import os
import re

replacements = {
    "OmsOrderRepository.java": {
        "OrderByCreatedAtDesc": "OrderByCreatedAtDesc" # already OK, wait: was create_time
    },
    "SmsCouponHistoryRepository.java": {
        "findByMemberIdAndUseStatus": "findByMemberIdAndStatus",
        "findByCouponIdAndUseStatus": "findByCouponIdAndStatus"
    },
    "SmsFlashPromotionSessionRepository.java": {
        "findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual": "findFirstByIsActiveAndStartsAtLocalLessThanEqualAndEndsAtLocalGreaterThanEqual"
    },
    "SmsFlashPromotionRepository.java": {
        "findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual": "findByIsActiveAndStartsOnLessThanEqualAndEndsOnGreaterThanEqual"
    },
    "SmsFlashPromotionProductRelationRepository.java": {
        "findByFlashPromotionIdAndFlashPromotionSessionId": "findByFlashSaleIdAndSlotId",
        "findByFlashPromotionIdAndFlashPromotionSessionIdAndProductId": "findByFlashSaleIdAndSlotIdAndProductId"
    },
    "PmsReviewRepository.java": {
        "findByProductIdAndParentIdIsNullAndStatus": "findByProductIdAndParentReviewIdIsNullAndModerationStatus",
        "findByParentIdAndStatus": "findByParentReviewIdAndModerationStatus"
    },
    "OmsCartItemRepository.java": {
        "findByMemberIdAndProductIdAndProductSkuId": "findByMemberIdAndProductIdAndSkuId"
    }
}

base_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository"
for filename, repls in replacements.items():
    filepath = os.path.join(base_dir, filename)
    if os.path.exists(filepath):
        with open(filepath, 'r') as f:
            content = f.read()
        for old, new in repls.items():
            content = content.replace(old, new)
        with open(filepath, 'w') as f:
            f.write(content)

print("Repos updated.")
