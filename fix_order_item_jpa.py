import re

filepath = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsOrderItem.java'
with open(filepath, 'r') as f: content = f.read()

content = re.sub(r'(?:\s*@Column\([^)]+\))?(?:\s*@Schema\([^)]+\))?\s*private\s+BigDecimal\s+pointsDiscountAmount;', '', content)
content = re.sub(r'(?:\s*@Column\([^)]+\))?(?:\s*@Schema\([^)]+\))?\s*private\s+Integer\s+earnedPoints;', '', content)

with open(filepath, 'w') as f: f.write(content)
print("OmsOrderItem points removed!")
