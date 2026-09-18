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
        r'\s*defaultLevel = new UmsMemberLevel\(\);\n\s*defaultLevel\.setId\(1L\);\n\s*defaultLevel\.setDefaultStatus\(1\);',
        r'\s*when\(memberLevelRepository\.findByDefaultStatus\(1\)\)\.thenReturn\(Collections\.singletonList\(defaultLevel\)\);'
    ]
)

# OmsPortalOrderServiceImplTest.java
replace_in_file(
    f"{base_dir}/nexus-portal/src/test/java/com/nexusengine/core/portal/unit/OmsPortalOrderServiceImplTest.java",
    {
        "cartPromotionItem.setPoints(100);": "cartPromotionItem.setEarnedPoints(100);"
    }
)
