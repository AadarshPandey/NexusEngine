import os
import re

# We will only apply these to SmsCoupon and SmsCouponHistory types, and specific UI files for coupons
replacements = {
    "discountValue": "amount",
    "perMemberLimit": "perLimit",
    "minSpendAmount": "minOrderAmount",
    "startsAt": "startTime",
    "endsAt": "endTime",
    "applicabilityScope": "useType",
    "claimedCount": "receiveCount",
    "minQuantity": "count",
    "acquisitionChannel": "getType", 
    "orderNumber": "orderSn"
}

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    for old, new in replacements.items():
        content = re.sub(r'\b' + old + r'\b', new, content)
    
    # Manually fix createdAt -> createTime and status -> useStatus ONLY in coupon types/pages
    if 'SmsCouponHistory' in content or 'CouponHistory' in content or 'coupon' in filepath.lower():
        # but wait, CouponHistory also has orderSn. 
        # let's be VERY specific.
        pass
        
    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {filepath}")

# Only modify coupon files!
target_files = [
    '../NexusAdminPanel/src/types/coupon.d.ts',
    '../NexusAdminPanel/src/apis/coupon.ts',
    '../NexusAdminPanel/src/pages/sms/CouponList.tsx',
    '../NexusAdminPanel/src/pages/sms/CouponUpdate.tsx',
    '../NexusAdminPanel/src/pages/sms/CouponAdd.tsx',
    '../NexusAdminPanel/src/pages/sms/CouponHistory.tsx'
]

for filepath in target_files:
    if os.path.exists(filepath):
        process_file(filepath)

print("Done")
