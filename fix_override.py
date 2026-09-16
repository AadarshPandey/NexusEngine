import re

file_path = "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberServiceImpl.java"
with open(file_path, "r") as f:
    content = f.read()

content = content.replace("@Override\n    \n    @Override\n    public void updateMember", "@Override\n    public void updateMember")

with open(file_path, "w") as f:
    f.write(content)
