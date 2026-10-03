import re

# Fix frontend API endpoint
filepath = './NexusFrontendWeb/src/api/product.ts'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace("'/product/search'", "'/portal/product/search'")

with open(filepath, 'w') as f:
    f.write(content)


# Fix backend PmsPortalProductServiceImpl
filepath_backend = './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalProductServiceImpl.java'
with open(filepath_backend, 'r') as f:
    content_backend = f.read()

content_backend = re.sub(r'cb\.like\(cb\.lower\(root\.get\("subTitle"\)\), likeKeyword\),\s*', '', content_backend)

with open(filepath_backend, 'w') as f:
    f.write(content_backend)

print("Fixed frontend API path and backend subTitle bug")
