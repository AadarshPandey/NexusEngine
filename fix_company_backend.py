import re

filepath = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsCompanyAddress.java'
with open(filepath, 'r') as f:
    content = f.read()

# Replace province with state
content = content.replace('@Schema(title = "Province")', '@Schema(title = "State")')
content = content.replace('private String province;', 'private String state;')

# Remove region completely
content = re.sub(r'\s*@Schema\(title\s*=\s*"Region"\)\s*private\s*String\s*region;', '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("Backend JPA entities updated!")
