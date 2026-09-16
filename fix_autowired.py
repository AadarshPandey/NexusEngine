import re

with open("NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java", "r") as f:
    text = f.read()

# Replace any @Autowired followed by an empty line or a method definition
text = re.sub(r'@Autowired\s*\n\s*public', 'public', text)
text = re.sub(r'@Autowired\s*\n\s*\n', '\n', text)

with open("NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java", "w") as f:
    f.write(text)

