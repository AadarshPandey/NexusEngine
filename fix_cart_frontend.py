import re

filepath = './NexusFrontendWeb/src/api/cart.ts'
with open(filepath, 'r') as f:
    content = f.read()

content = re.sub(r'\s*productSubTitle:\s*string;', '', content)
content = re.sub(r'\s*memberNickname:\s*string;', '', content)

with open(filepath, 'w') as f:
    f.write(content)

print("Frontend React TS types cleaned!")
