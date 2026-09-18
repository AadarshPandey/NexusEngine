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

# 1. UmsMember.java
replace_in_file(
    f"{base_dir}/nexus-data-jpa/src/main/java/com/nexusengine/core/model/UmsMember.java",
    {
        "rewardPoints": "points",
        '"lifetime_reward_points"': '"lifetime_points"',
        "lifetimeRewardPoints": "lifetimePoints"
    },
    [
        r'\s*@Column\(name = "member_level_id"\)\s*private Long memberLevelId;',
        r'\s*@Schema\(title = "Growth"\)\s*private Integer experiencePoints;',
        r'\s*@Schema\(title = "Integration"\)'
    ]
)

# 2. OmsOrder.java
replace_in_file(
    f"{base_dir}/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsOrder.java",
    {
        "rewardPoints": "earnedPoints",
        "integrationAmount": "pointsDiscountAmount",
        '"integration_amount"': '"points_discount_amount"',
        "useIntegration": "usedPoints",
        '"use_integration"': '"used_points"'
    },
    [
        r'\s*private Integer experiencePoints;'
    ]
)

# 3. OmsOrderItem.java
replace_in_file(
    f"{base_dir}/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsOrderItem.java",
    {
        "integrationAmount": "pointsDiscountAmount",
        '"integration_amount"': '"points_discount_amount"',
        "giftIntegration": "earnedPoints",
        '"gift_integration"': '"earned_points"'
    },
    [
        r'\s*@Column\(name = "gift_growth"\)\s*private Integer giftGrowth;'
    ]
)

# 4. SmsCoupon.java
replace_in_file(
    f"{base_dir}/nexus-data-jpa/src/main/java/com/nexusengine/core/model/SmsCoupon.java",
    {},
    [
        r'\s*@Column\(name = "member_level"\)\s*@Schema\(title = "Member level"\)\s*private Integer memberLevel;'
    ]
)

# 5. PmsProduct.java
replace_in_file(
    f"{base_dir}/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PmsProduct.java",
    {
        "giftPoint": "giftPoints",
        '"gift_point"': '"gift_points"'
    },
    [
        r'\s*@Column\(name = "gift_growth"\)\s*private Integer giftGrowth;'
    ]
)

# 6. PmsProductOperateLog.java
replace_in_file(
    f"{base_dir}/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PmsProductOperateLog.java",
    {
        "giftPointOld": "giftPointsOld",
        "giftPointNew": "giftPointsNew",
        '"gift_point_old"': '"gift_points_old"',
        '"gift_point_new"': '"gift_points_new"'
    }
)

# 7. PmsProductParam.java
replace_in_file(
    f"{base_dir}/nexus-admin/src/main/java/com/nexusengine/core/dto/PmsProductParam.java",
    {},
    [
        r'\s*@Schema\(title = "Member price"\)\s*private List<PmsMemberPrice> memberPriceList;'
    ]
)
