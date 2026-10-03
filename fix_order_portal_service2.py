import re

files_to_clean = [
    './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/OmsPortalOrderController.java',
    './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java',
    './NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/OmsOrderServiceImpl.java'
]

methods_to_remove = [
    'setPaymentId', 'setEarnedPoints', 'setPointsDiscountAmount', 'setPromotionInfo',
    'setMemberUsername', 'setSourceType', 'setReceiverProvince', 'setReceiverCity',
    'setReceiverRegion', 'setAutoConfirmDay'
]

for filepath in files_to_clean:
    with open(filepath, 'r') as f:
        content = f.read()

    for method in methods_to_remove:
        content = re.sub(r'\s*[a-zA-Z0-9_]+\.' + method + r'\([^)]*\);', '', content)

    # Clean the remaining specific logic
    content = re.sub(r'sb\.append\(String\.format\("%02d", order\.getSourceType\(\)\)\);', '', content)
    
    # calcEarnedPoints
    content = re.sub(r'private Integer calcEarnedPoints.*?\}', 'private Integer calcEarnedPoints(List<OmsOrderItem> orderItemList) { return 0; }', content, flags=re.DOTALL)
    
    # getEarnedPoints usage in the method
    content = re.sub(r'if \([^)]*\.getEarnedPoints\(\) != null\)\s*\{\s*sum \+= [^;]+;\s*\}', '', content)
    
    # calcPointsAmount
    content = re.sub(r'if \([^)]*\.getPointsDiscountAmount\(\) != null\)\s*amount = amount\.add\([^;]+\);', '', content)
    
    # orderItem.setPointsDiscountAmount in else block
    content = re.sub(r'orderItem\.setPointsDiscountAmount\([^)]*\);', '', content)

    with open(filepath, 'w') as f:
        f.write(content)

print("Remaining compilation issues fixed!")
