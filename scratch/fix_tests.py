import os
import re

def replace_in_file(filepath, replacements, drops=None):
    if not os.path.exists(filepath):
        print(f"Skipping {filepath} (does not exist)")
        return
    with open(filepath, 'r') as f:
        content = f.read()
    
    if drops:
        for drop in drops:
            content = re.sub(drop, '', content, flags=re.DOTALL)
            
    for old, new in replacements.items():
        content = content.replace(old, new)
        
    with open(filepath, 'w') as f:
        f.write(content)
    print(f"Updated {filepath}")

base_dir = '/home/aadarsh/Documents/NexusEngine/NexusCore'

# UmsMemberServiceImplTest.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/test/java/com/nexusengine/core/portal/unit/UmsMemberServiceImplTest.java",
    {
        "setRewardPoints": "setPoints",
        "getRewardPoints": "getPoints",
        "updateIntegration": "updatePoints"
    },
    [
        r'import com\.nexusengine\.core\.model\.UmsMemberLevel;\n',
        r'import com\.nexusengine\.core\.repository\.UmsMemberLevelRepository;\n',
        r'\s*@Mock\s*private UmsMemberLevelRepository memberLevelRepository;',
        r'\s*UmsMemberLevel mockLevel = new UmsMemberLevel\(\);\n\s*mockLevel\.setId\(1L\);\n\s*mockLevel\.setDefaultStatus\(1\);\n\s*when\(memberLevelRepository\.findByDefaultStatus\(1\)\)\.thenReturn\(Collections\.singletonList\(mockLevel\)\);',
        r'\s*assertEquals\(1L, savedMember\.getMemberLevelId\(\)\);'
    ]
)

# OmsPortalOrderServiceImplTest.java (just in case there are errors)
replace_in_file(
    f"{base_dir}/nexus-portal/src/test/java/com/nexusengine/core/portal/unit/OmsPortalOrderServiceImplTest.java",
    {
        "setRewardPoints": "setPoints", # for UmsMember
        "cartPromotionItem.setRewardPoints": "cartPromotionItem.setEarnedPoints",
        "cartPromotionItem.getRewardPoints": "cartPromotionItem.getEarnedPoints",
        "updateIntegration": "updatePoints"
    },
    [
        r'\s*cartPromotionItem\.setExperiencePoints\(.*?\);'
    ]
)
