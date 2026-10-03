import re

filepath = './NexusFrontendWeb/src/views/Checkout.tsx'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace('.minPoint', '.minOrderAmount')
content = content.replace('minPoint:', 'minOrderAmount:')

with open(filepath, 'w') as f:
    f.write(content)

print("Fixed Checkout.tsx minPoint")
