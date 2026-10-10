import os
import re

replacements = {
    "orderNumber": "orderSn",
    "subtotalAmount": "totalAmount",
    "grandTotalAmount": "payAmount",
    "shippingAmount": "freightAmount",
    "promotionDiscountAmount": "promotionAmount",
    "paymentMethod": "payType",
    "carrierName": "deliveryCompany",
    "trackingNumber": "deliverySn",
    "recipientName": "receiverName",
    "recipientPhone": "receiverPhone",
    "shippingPostalCode": "receiverPostCode",
    "shippingAddressLine": "receiverDetailAddress",
    "isReceiptConfirmed": "confirmStatus",
    "deletedAt": "deleteStatus",
    "paidAt": "paymentTime",
    "shippedAt": "deliveryTime",
    "deliveredAt": "receiveTime",
    "reviewedAt": "commentTime",
    "updatedAt": "modifyTime",
    "isDefault": "defaultStatus",
    "postalCode": "postCode",
    "district": "region",
    "addressLine": "detailAddress"
}

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    for old, new in replacements.items():
        # Match only whole words
        content = re.sub(r'\b' + old + r'\b', new, content)
        
    # We must also fix createdAt for OmsOrder, but NOT for UmsMember!
    # For order.createdAt -> order.createTime
    content = content.replace("order.createdAt", "order.createTime")
    
    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('./src'):
    for file in files:
        if file.endswith('.tsx') or file.endswith('.ts'):
            process_file(os.path.join(root, file))

print("Done")
