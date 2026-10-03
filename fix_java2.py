import re

def process_file(filepath):
    with open(filepath, 'r') as f: content = f.read()
    
    # Simple replaces for single line setters
    single_line_setters = [
        'order.setMemberUsername', 'order.setAutoConfirmDay', 'order.setPromotionInfo',
        'order.setReceiverProvince', 'order.setReceiverCity', 'order.setReceiverRegion',
        'order.setSourceType', 'dbOrder.setPaymentId'
    ]
    for s in single_line_setters:
        content = re.sub(r'^\s*'+s+r'\(.*?\);\s*$', '', content, flags=re.MULTILINE)

    # OmsOrderServiceImpl specific
    content = re.sub(r'order\.setReceiverProvince\(.*?\);', '', content)
    content = re.sub(r'order\.setReceiverCity\(.*?\);', '', content)
    content = re.sub(r'order\.setReceiverRegion\(.*?\);', '', content)

    # OmsOrderServiceImpl vendor logic
    if 'OmsOrderServiceImpl' in filepath and 'admin' in filepath:
        content = re.sub(r'Long vendorId = admin != null \? admin\.getVendorId\(\) : null;\s*if \(vendorId != null\) \{\s*return orderRepository\.findByVendorIdOrderByCreateTimeDesc\(vendorId\);\s*\}', '', content)
        
    # Points logic in PortalOrder
    if 'OmsPortalOrderServiceImpl' in filepath:
        # 1. orderItem.setPointsDiscountAmount
        content = re.sub(r'orderItem\.setPointsDiscountAmount\([^)]*\);', '', content)
        
        # 2. calcPointsAmount block -> return 0
        content = re.sub(r'if \(item\.getPointsDiscountAmount\(\) != null\)[^;]+;', '', content)
        
        # 3. getUsePointsAmount -> already returns 0
        
        # 4. calcEarnedPoints
        content = re.sub(r'if \(orderItem\.getEarnedPoints\(\) != null\)\s*\{\s*sum \+= [^;]+;\s*\}', '', content)
        
        # 5. order.setEarnedPoints, order.setPointsDiscountAmount, order.setUsedPoints
        content = re.sub(r'order\.setEarnedPoints\([^)]*\);', '', content)
        content = re.sub(r'order\.setPointsDiscountAmount\([^)]*\);', '', content)
        content = re.sub(r'order\.setUsedPoints\([^)]*\);', '', content)
        
        # 6. subtract points from math
        content = content.replace('.subtract(order.getPointsDiscountAmount())', '')
        content = content.replace('.subtract(orderItem.getPointsDiscountAmount())', '')
        
        # 7. member.getPoints() and memberService.updatePoints()
        content = re.sub(r'if \(timeOutOrder\.omsOrder\(\)\.getUsedPoints\(\) != null\) \{.*?(?=\})\}', '', content, flags=re.DOTALL)
        content = re.sub(r'if \(cancelOrder\.getUsedPoints\(\) != null\) \{.*?(?=\})\}', '', content, flags=re.DOTALL)
        content = re.sub(r'if \(orderParam\.usePoints\(\) != null\) \{.*?deleteCartItemList', 'deleteCartItemList', content, flags=re.DOTALL)
        
        # 8. sourceType formatting
        content = re.sub(r'sb\.append\(String\.format\("%02d", order\.getSourceType\(\)\)\);', 'sb.append("01");', content)
        
    with open(filepath, 'w') as f: f.write(content)

process_file('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java')
process_file('./NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/OmsOrderServiceImpl.java')
process_file('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/OmsPortalOrderController.java')
print("Processed!")
