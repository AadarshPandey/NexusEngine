import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql'
with open(filepath, 'r') as f:
    content = f.read()

# sms_coupon_product_category_relation
content = re.sub(r'\s*product_category_name\s*VARCHAR\(255\),', '', content)
content = re.sub(r'\s*parent_category_name\s*VARCHAR\(255\)', '', content)

# sms_coupon_product_relation
content = re.sub(r'\s*product_name\s*VARCHAR\(255\),', '', content)
content = re.sub(r'\s*product_sn\s*VARCHAR\(255\)', '', content)

# sms_flash_promotion_product_relation
content = re.sub(r'\s*sort\s*INTEGER\s*DEFAULT\s*0\s*,', '', content)
content = re.sub(r'\s*sort\s*INTEGER\s*,', '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("V1__baseline.sql updated!")
