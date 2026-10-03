import re

path = './NexusFrontendWeb/src/api/order.ts'
with open(path, 'r') as f:
    c = f.read()

c = c.replace('  productPic: string;\n  productBrand: string;', '  productPic: string;\n  productName: string;\n  productBrand: string;\n  productSn: string;')
c = c.replace('  handleTime: string;\n  productPic: string;\n  productBrand: string;', '  handleTime: string;\n  productPic: string;\n  productName: string;\n  productBrand: string;')

with open(path, 'w') as f:
    f.write(c)

print("Restored productName to OmsOrderItem and OmsOrderReturnApply")
