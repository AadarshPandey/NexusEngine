import re

filepath = './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java'
with open(filepath, 'r') as f:
    content = f.read()

# 1. Clean order.set...
content = re.sub(r'\s*order\.setMemberUsername\([^)]+\);', '', content)
content = re.sub(r'\s*order\.setAutoConfirmDay\([^)]+\);', '', content)
content = re.sub(r'\s*order\.setPromotionInfo\([^)]+\);', '', content)
content = re.sub(r'\s*order\.setReceiverProvince\([^)]+\);', '', content)
content = re.sub(r'\s*order\.setReceiverCity\([^)]+\);', '', content)
content = re.sub(r'\s*order\.setReceiverRegion\([^)]+\);', '', content)

# 2. Points block logic (order generation)
content = re.sub(r'if \(orderParam\.usePoints\(\) == null\) \{.*?\}\s*else\s*\{.*?\}', '', content, flags=re.DOTALL)
content = re.sub(r'if \(orderParam\.usePoints\(\) != null\) \{.*?(?=deleteCartItemList)', '', content, flags=re.DOTALL)
content = re.sub(r'\s*order\.setEarnedPoints\([^)]+\);', '', content)

# 3. Pay amount calculation
content = content.replace('.subtract(order.getPointsDiscountAmount())', '')

# 4. Remove orderItem points discount block
content = re.sub(r'if \(orderParam\.usePoints\(\) == null\) \{.*?\} else \{.*?\}', '', content, flags=re.DOTALL)
content = content.replace('.subtract(orderItem.getPointsDiscountAmount())', '')

# 5. Cancel and Timeout order point logic
content = re.sub(r'if \(timeOutOrder\.omsOrder\(\)\.getUsedPoints\(\) != null\) \{.*?(?=\})\}', '', content, flags=re.DOTALL)
content = re.sub(r'if \(cancelOrder\.getUsedPoints\(\) != null\) \{.*?(?=\})\}', '', content, flags=re.DOTALL)

with open(filepath, 'w') as f:
    f.write(content)

print("OmsPortalOrderServiceImpl points and dead columns cleaned!")
