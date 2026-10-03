import re

# 1. NexusFrontendWeb/src/api/order.ts
path1 = './NexusFrontendWeb/src/api/order.ts'
with open(path1, 'r') as f:
    c1 = f.read()
c1 = re.sub(r'\s*productCategoryName:\s*string;', '', c1)
c1 = re.sub(r'\s*parentCategoryName:\s*string;', '', c1)
c1 = re.sub(r'\s*productName:\s*string;', '', c1)
c1 = re.sub(r'\s*productSn:\s*string;', '', c1)
with open(path1, 'w') as f:
    f.write(c1)

# 2. NexusAdminPanel/src/types/flash.d.ts
path2 = './NexusAdminPanel/src/types/flash.d.ts'
with open(path2, 'r') as f:
    c2 = f.read()
c2 = re.sub(r'\s*sort\?:\s*number', '', c2)
with open(path2, 'w') as f:
    f.write(c2)

# 3. NexusAdminPanel/src/pages/sms/FlashProductRelation.tsx
path3 = './NexusAdminPanel/src/pages/sms/FlashProductRelation.tsx'
with open(path3, 'r') as f:
    c3 = f.read()
c3 = re.sub(r'\s*<TableCell>Sort</TableCell>', '', c3)
c3 = re.sub(r'\s*<TableCell>\{row\.sort\}</TableCell>', '', c3)
c3 = c3.replace('colSpan={8}', 'colSpan={7}')
with open(path3, 'w') as f:
    f.write(c3)

print("Frontend React TS types and components cleaned!")
