import re

filepath = './NexusAdminPanel/src/types/companyAddress.d.ts'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace('province: string', 'state: string')
content = re.sub(r'\s*region:\s*string\n', '\n', content)

with open(filepath, 'w') as f:
    f.write(content)

print("Frontend React TS types updated!")
