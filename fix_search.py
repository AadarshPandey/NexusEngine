import re

filepath = './NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/repository/EsProductRepository.java'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace('findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords,Pageable page)', 'findByNameOrKeywords(String name, String keywords, Pageable page)')

with open(filepath, 'w') as f:
    f.write(content)

filepath_service = './NexusCore/nexus-search/src/main/java/com/nexusengine/core/search/service/impl/EsProductServiceImpl.java'
with open(filepath_service, 'r') as f:
    content_service = f.read()

content_service = content_service.replace('productRepository.findByNameOrSubTitleOrKeywords(keyword, keyword, keyword, pageable)', 'productRepository.findByNameOrKeywords(keyword, keyword, pageable)')

with open(filepath_service, 'w') as f:
    f.write(content_service)

print("Fixed search repo and service")
