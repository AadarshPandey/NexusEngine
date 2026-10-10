import re

files_to_update = [
    "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/UmsMember.java",
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/UmsMemberController.java",
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberCouponServiceImpl.java",
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java",
    "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberServiceImpl.java",
    "NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/TestFixtures.java"
]

renames = {
    "private String nickname;": "private String displayName;",
    "private String phone;": "private String contactPhone;",
    "private OffsetDateTime createTime;": "private OffsetDateTime createdAt;",
    "private String icon;": "private String iconUrl;",
    "private Integer sourceType;": "private Integer signupChannel;",
    "private Integer points;": "private Integer pointsBalance;",
    "private Integer lifetimePoints;": "private Integer lifetimePointsEarned;",
    ".getNickname()": ".getDisplayName()",
    ".setNickname(": ".setDisplayName(",
    ".getPhone()": ".getContactPhone()",
    ".setPhone(": ".setContactPhone(",
    "member.setCreateTime(": "member.setCreatedAt(",
    "umsMember.setCreateTime(": "umsMember.setCreatedAt(",
    ".getIcon()": ".getIconUrl()",
    ".setIcon(": ".setIconUrl(",
    ".getPoints()": ".getPointsBalance()",
    ".setPoints(": ".setPointsBalance("
}

for file_path in files_to_update:
    with open(file_path, 'r') as f:
        content = f.read()
    
    for old, new in renames.items():
        content = content.replace(old, new)
        
    with open(file_path, 'w') as f:
        f.write(content)

print("Done")
