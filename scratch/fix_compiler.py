import os
import re

def replace_in_file(filepath, replacements):
    if not os.path.exists(filepath):
        print(f"Skipping {filepath} (does not exist)")
        return
    with open(filepath, 'r') as f:
        content = f.read()
            
    for old, new in replacements.items():
        content = content.replace(old, new)
        
    with open(filepath, 'w') as f:
        f.write(content)
    print(f"Updated {filepath}")

base_dir = '/home/aadarsh/Documents/NexusEngine/NexusCore'

replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java",
    {
        "cartPromotionItem.getPoints()": "cartPromotionItem.getEarnedPoints()",
        "order.setUsePoints": "order.setUsedPoints",
        "currentMember.setEarnedPoints(0)": "currentMember.setPoints(0)",
        "timeOutOrder.getUsePoints()": "timeOutOrder.getUsedPoints()",
        "cancelOrder.getUsePoints()": "cancelOrder.getUsedPoints()",
        "order.setEarnedPoints(orderParam.getUsePoints())": "order.setEarnedPoints(orderParam.getUsePoints())"
    }
)

replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/domain/OmsOrderDetail.java",
    {
        "useIntegration": "usedPoints"
    }
)

replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/UmsMemberService.java",
    {
        "updateIntegration": "updatePoints"
    }
)

replace_in_file(
    f"{base_dir}/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberServiceImpl.java",
    {
        "updateIntegration": "updatePoints"
    }
)
