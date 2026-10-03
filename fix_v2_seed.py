import re

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql', 'r') as f:
    content = f.read()

# Replace column lists
content = content.replace(
    ', sort, sale, unit, weight, keywords, detail_title',
    ', sale, keywords'
)
# Replace values lists
content = re.sub(r', 0, 0, \'piece\', 500, \'([^\']+)\', \'[^\']+\'\)', r', 0, \'\1\')', content)

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql', 'w') as f:
    f.write(content)
