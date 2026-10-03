import re

filepath = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsOrder.java'
with open(filepath, 'r') as f:
    content = f.read()

fields_to_remove = [
    'pointsDiscountAmount', 'earnedPoints', 'usedPoints', 'memberUsername',
    'sourceType', 'autoConfirmDay', 'promotionInfo', 'vendorId', 'paymentId',
    'billType', 'billHeader', 'billContent', 'billReceiverPhone', 'billReceiverEmail',
    'receiverProvince', 'receiverCity', 'receiverRegion'
]

for field in fields_to_remove:
    # Pattern to match the annotations above the field and the field itself
    # E.g. @Column(name = "member_username")\n    @Schema(title = "Member username")\n    private String memberUsername;
    pattern = r'(?:\s*@Column\([^)]+\))?(?:\s*@Schema\([^)]+\))?\s*private\s+[A-Za-z]+\s+' + field + r';'
    content = re.sub(pattern, '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("OmsOrder.java fields removed!")
