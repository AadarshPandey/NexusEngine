import os
import re

def replace_in_file(filepath, replacements, drops=None):
    if not os.path.exists(filepath):
        print(f"Skipping {filepath} (does not exist)")
        return
    with open(filepath, 'r') as f:
        content = f.read()
    
    if drops:
        for drop in drops:
            content = re.sub(drop, '', content, flags=re.DOTALL)
            
    for old, new in replacements.items():
        content = content.replace(old, new)
        
    with open(filepath, 'w') as f:
        f.write(content)
    print(f"Updated {filepath}")

base_dir = '/home/aadarsh/Documents/NexusEngine/NexusCore'

# PmsProductServiceImpl
replace_in_file(
    f"{base_dir}/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java",
    {},
    [
        r'import com\.nexusengine\.core\.repository\.PmsMemberPriceRepository;\n',
        r'import com\.nexusengine\.core\.model\.PmsMemberPrice;\n',
        r'\s*private final PmsMemberPriceRepository memberPriceRepository;',
        r'\s*saveMemberPriceList\(productParam\.getMemberPriceList\(\), productId\);',
        r'\s*memberPriceRepository\.deleteByProductId\(id\);',
        r'\s*saveMemberPriceList\(productParam\.getMemberPriceList\(\), id\);',
        r'\s*private void saveMemberPriceList\(List<PmsMemberPrice> dataList, Long productId\) \{.*?\n\s*\}'
    ]
)

# UmsMemberServiceImpl
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberServiceImpl.java",
    {
        "setRewardPoints": "setPoints",
        "updateIntegration": "updatePoints",
        "getRewardPoints": "getPoints"
    },
    [
        r'import com\.nexusengine\.core\.repository\.UmsMemberLevelRepository;\n',
        r'import com\.nexusengine\.core\.model\.UmsMemberLevel;\n',
        r'\s*private final UmsMemberLevelRepository memberLevelRepository;',
        r'\s*List<UmsMemberLevel> memberLevelList = memberLevelRepository\.findByDefaultStatus\(1\);\n\s*if \(!CollectionUtils\.isEmpty\(memberLevelList\)\) \{\n\s*umsMember\.setMemberLevelId\(memberLevelList\.get\(0\)\.getId\(\)\);\n\s*\}'
    ]
)

# UmsMemberService interface
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/UmsMemberService.java",
    {
        "updateIntegration(Long id, Integer integration)": "updatePoints(Long id, Integer points)"
    }
)

# CartPromotionItem.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/domain/CartPromotionItem.java",
    {
        "rewardPoints": "earnedPoints",
    },
    [
        r'\s*private Integer experiencePoints;'
    ]
)

# ConfirmOrderResult.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/domain/ConfirmOrderResult.java",
    {
        "memberIntegration": "memberPoints"
    }
)

# OrderParam.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/domain/OrderParam.java",
    {
        "useIntegration": "usePoints"
    }
)

# OmsPromotionServiceImpl.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPromotionServiceImpl.java",
    {
        "setRewardPoints": "setEarnedPoints",
        "getGiftPoint": "getGiftPoints"
    },
    [
        r'\s*cartPromotionItem\.setExperiencePoints\(promotionProduct\.getGiftGrowth\(\)\);'
    ]
)

# OmsPortalOrderServiceImpl.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java",
    {
        "setMemberIntegration": "setMemberPoints",
        "getRewardPoints": "getPoints",
        "getUseIntegration": "getUsePoints",
        "getIntegrationAmount": "getPointsDiscountAmount",
        "setIntegrationAmount": "setPointsDiscountAmount",
        "setRewardPoints": "setEarnedPoints",
        "setGiftIntegration": "setEarnedPoints",
        "updateIntegration": "updatePoints",
        "calcGifIntegration": "calcEarnedPoints",
        "Integration": "Points",
        "integration": "points"
    },
    [
        r'\s*orderItem\.setGiftGrowth\(cartPromotionItem\.getExperiencePoints\(\)\);',
        r'\s*order\.setExperiencePoints\(calcGiftGrowth\(orderItemList\)\);',
        r'\s*private Integer calcGiftGrowth\(List<OmsOrderItem> orderItemList\) \{.*?\n\s*\}'
    ]
)
