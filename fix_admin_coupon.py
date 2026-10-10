import os
import re

replacements = {
    # SmsCoupon replacements
    "discountValue": "amount",
    "perMemberLimit": "perLimit",
    "minSpendAmount": "minOrderAmount",
    "startsAt": "startTime",
    "endsAt": "endTime",
    "applicabilityScope": "useType",
    "claimedCount": "receiveCount",
    "minQuantity": "count",
    
    # SmsCouponHistory replacements
    "acquisitionChannel": "getType", 
    "createdAt": "createTime",
    "status": "useStatus",
    "redeemedAt": "useTime",
    "orderNumber": "orderSn"
}

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    for old, new in replacements.items():
        # Special case: don't replace 'status' in a way that breaks React state or Redux
        if old == 'status':
            # only replace 'status' if it's accessed via object property (e.g., item.status, history.status, row.status)
            content = re.sub(r'(\w+)\.status\b', r'\1.useStatus', content)
            # also in CouponHistoryQueryParam type definition: "status?: number" -> "useStatus?: number"
            content = re.sub(r'\bstatus\s*\?:', 'useStatus?:', content)
        elif old == 'createdAt':
            content = re.sub(r'(\w+)\.createdAt\b', r'\1.createTime', content)
            content = re.sub(r'\bcreatedAt\s*\?:', 'createTime?:', content)
        else:
            content = re.sub(r'\b' + old + r'\b', new, content)
            
    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('../NexusAdminPanel/src'):
    for file in files:
        if file.endswith('.tsx') or file.endswith('.ts'):
            process_file(os.path.join(root, file))

print("Done")
