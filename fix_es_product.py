import re

with open('./NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/domain/EsProduct.java', 'r') as f:
    content = f.read()

fields = ["subTitle", "sort", "productCategoryName"]

for field in fields:
    pattern = r'(\s*(?:@[^\n]+\n)*\s*private\s+[A-Za-z0-9_<>\[\]]+\s+' + field + r'\s*;)'
    content = re.sub(pattern, '', content, flags=re.MULTILINE)

with open('./NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/domain/EsProduct.java', 'w') as f:
    f.write(content)
