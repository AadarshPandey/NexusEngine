import re

# 1. OmsCartItem.java
filepath = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsCartItem.java'
with open(filepath, 'r') as f:
    content = f.read()

content = re.sub(r'\s*@Column\(name\s*=\s*"product_sub_title"\)\s*@Schema\(title\s*=\s*"Product sub title"\)\s*private\s*String\s*productSubTitle;', '', content)
content = re.sub(r'\s*@Column\(name\s*=\s*"member_nickname"\)\s*@Schema\(title\s*=\s*"Member nickname"\)\s*private\s*String\s*memberNickname;', '', content)

with open(filepath, 'w') as f:
    f.write(content)


# 2. OmsCartItemServiceImpl.java
filepath_impl = './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java'
with open(filepath_impl, 'r') as f:
    content_impl = f.read()

content_impl = re.sub(r'\s*cartItem\.setMemberNickname\(currentMember\.getNickname\(\)\);', '', content_impl)

with open(filepath_impl, 'w') as f:
    f.write(content_impl)

print("Backend JPA entities and services cleaned!")
