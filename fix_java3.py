import re

filepath = './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java'
with open(filepath, 'r') as f: content = f.read()

single_line_setters = [
    'orderItem.setEarnedPoints', 'orderItem.setPointsDiscountAmount',
    'order.setPointsDiscountAmount', 'order.setEarnedPoints'
]
for s in single_line_setters:
    content = re.sub(r'^\s*'+s+r'\(.*?\);\s*$', '', content, flags=re.MULTILINE)

with open(filepath, 'w') as f: f.write(content)

print("Processed fix 3!")
