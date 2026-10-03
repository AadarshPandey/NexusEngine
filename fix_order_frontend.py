import re
import os

files_to_check = [
    './NexusAdminPanel/src/types/returnApply.d.ts',
    './NexusAdminPanel/src/types/order.d.ts',
    './NexusAdminPanel/src/pages/oms/ReturnApplyDetail.tsx',
    './NexusAdminPanel/src/pages/oms/ReturnApplyList.tsx',
    './NexusAdminPanel/src/pages/oms/OrderList.tsx',
    './NexusFrontendWeb/src/views/Profile.tsx',
    './NexusFrontendWeb/src/api/order.ts'
]

# Patterns to completely remove from TS interfaces and TSX files
patterns = [
    r'\s*pointsDiscountAmount\??:\s*number;?',
    r'\s*earnedPoints\??:\s*number;?',
    r'\s*usedPoints\??:\s*number;?',
    r'\s*memberUsername\??:\s*string;?',
    r'\s*sourceType\??:\s*number;?',
    r'\s*autoConfirmDay\??:\s*number;?',
    r'\s*promotionInfo\??:\s*string;?',
    r'\s*vendorId\??:\s*number;?',
    r'\s*paymentId\??:\s*string;?',
    r'\s*billType\??:\s*number;?',
    r'\s*billHeader\??:\s*string;?',
    r'\s*billContent\??:\s*string;?',
    r'\s*billReceiverPhone\??:\s*string;?',
    r'\s*billReceiverEmail\??:\s*string;?',
    r'\s*receiverProvince\??:\s*string;?',
    r'\s*receiverCity\??:\s*string;?',
    r'\s*receiverRegion\??:\s*string;?'
]

for fp in files_to_check:
    if not os.path.exists(fp): continue
    with open(fp, 'r') as f: content = f.read()
    
    # Clean interfaces
    for p in patterns:
        content = re.sub(p, '', content)
        
    # Clean specific TSX usages
    if 'OrderList.tsx' in fp:
        content = re.sub(r'if \(searchMember && !order\.memberUsername.*?\n', '', content)
        content = re.sub(r'<TableCell>\{order\.memberUsername\}</TableCell>', '<TableCell>N/A</TableCell>', content)
    elif 'ReturnApplyList.tsx' in fp:
        content = re.sub(r'<TableCell>\{apply\.memberUsername\}</TableCell>', '<TableCell>N/A</TableCell>', content)
    elif 'ReturnApplyDetail.tsx' in fp:
        content = re.sub(r'<Typography component="span" color="text\.primary">\{detail\.memberUsername\}</Typography>', '<Typography component="span" color="text.primary">N/A</Typography>', content)
    elif 'Profile.tsx' in fp:
        content = re.sub(r'\s*memberUsername:\s*memberInfo\?.username,', '', content)
    
    with open(fp, 'w') as f: f.write(content)

print("Frontend React files cleaned!")
