import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V7__flash_sale_tables.sql'
with open(filepath, 'r') as f:
    content = f.read()

content = re.sub(r'\s*sort\s*INTEGER\s*DEFAULT\s*0,', '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("V7 updated!")
