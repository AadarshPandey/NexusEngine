import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql'
with open(filepath, 'r') as f: content = f.read()

# oms_order columns
cols_to_remove = [
    'points_discount_amount', 'earned_points', 'used_points', 'member_username',
    'source_type', 'auto_confirm_day', 'promotion_info', 'vendor_id', 'payment_id',
    'bill_type', 'bill_header', 'bill_content', 'bill_receiver_phone', 'bill_receiver_email',
    'receiver_province', 'receiver_city', 'receiver_region'
]
for col in cols_to_remove:
    content = re.sub(r'\s*' + col + r'\s+[^,]+,', '', content)

with open(filepath, 'w') as f: f.write(content)
print("Flyway V1 baseline updated!")
