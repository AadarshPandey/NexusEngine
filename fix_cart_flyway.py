import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql'
with open(filepath, 'r') as f:
    content = f.read()

content = re.sub(r'\s*product_sub_title\s*VARCHAR\(255\),', '', content)
content = re.sub(r'\s*member_nickname\s*VARCHAR\(255\),', '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("V1__baseline.sql updated!")
