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

base_dir = '/home/aadarsh/Documents/NexusEngine'

replace_in_file(f"{base_dir}/NexusFrontendWeb/src/api/member.ts",
    {
        "integration: number;": "points: number;",
        "lifetimeRewardPoints: number;": "lifetimePoints: number;"
    },
    [
        r'\s*growth: number;',
        r'\s*memberLevelId: number;'
    ]
)

replace_in_file(f"{base_dir}/NexusFrontendWeb/src/views/Profile.tsx",
    {
        "memberInfo?.integration": "memberInfo?.points"
    },
    [
        r'\s*<Typography variant="body2" color="text\.secondary">Member Level: \{memberInfo\?\.memberLevelId \|\| 1\}</Typography>'
    ]
)

replace_in_file(f"{base_dir}/NexusFrontendWeb/src/api/order.ts",
    {
        "integrationAmount": "pointsDiscountAmount"
    },
    [
        r'\s*integration: number;',
        r'\s*growth: number;'
    ]
)

replace_in_file(f"{base_dir}/NexusFrontendWeb/src/api/product.ts",
    {},
    [
        r'\s*memberLevel: number;'
    ]
)

replace_in_file(f"{base_dir}/NexusAdminPanel/src/types/order.d.ts",
    {
        "integrationAmount": "pointsDiscountAmount"
    },
    [
        r'\s*integration\?: number',
        r'\s*growth\?: number'
    ]
)

replace_in_file(f"{base_dir}/NexusAdminPanel/src/types/product.d.ts",
    {},
    [
        r'\s*memberLevelId\?: number',
        r'\s*memberLevelName\?: string'
    ]
)

replace_in_file(f"{base_dir}/NexusAdminPanel/src/types/coupon.d.ts",
    {},
    [
        r'\s*memberLevel\?: number'
    ]
)

# delete admin panel memberLevel api
files_to_delete = [
    f"{base_dir}/NexusAdminPanel/src/apis/memberLevel.ts",
    f"{base_dir}/NexusAdminPanel/src/types/memberLevel.d.ts"
]
for f in files_to_delete:
    if os.path.exists(f):
        os.remove(f)
        print(f"Deleted {f}")

