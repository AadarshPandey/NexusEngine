import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace('province VARCHAR(255),', 'state VARCHAR(255),')
content = re.sub(r'\s*region\s*VARCHAR\(255\),', '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("V1__baseline.sql updated!")
