import re

with open('./NexusAdminPanel/src/types/product.d.ts', 'r') as f:
    content = f.read()

fields = [
    "subTitle", "unit", "weight", "serviceIds", "note",
    "promotionStartTime", "promotionEndTime", "promotionPerLimit", "promotionType",
    "productCategoryName", "vendorId", "detailTitle", "detailDesc", "detailHtml", "sort"
]

for field in fields:
    pattern = r'^\s*' + field + r'\?:\s*.*$\n'
    content = re.sub(pattern, '', content, flags=re.MULTILINE)

with open('./NexusAdminPanel/src/types/product.d.ts', 'w') as f:
    f.write(content)
