import re

# 1. OmsOrderServiceImpl.java
filepath = './NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/OmsOrderServiceImpl.java'
with open(filepath, 'r') as f: content = f.read()
content = re.sub(r'\s*Long vendorId = admin != null \? admin\.getVendorId\(\) : null;\s*if \(vendorId != null\) \{\s*return orderRepository\.findByVendorIdOrderByCreateTimeDesc\(vendorId\);\s*\}', '', content)
with open(filepath, 'w') as f: f.write(content)

# 2. OmsOrderRepository.java
filepath = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/OmsOrderRepository.java'
with open(filepath, 'r') as f: content = f.read()
content = re.sub(r'\s*List<OmsOrder> findByVendorId[^;]+;', '', content)
with open(filepath, 'w') as f: f.write(content)

print("Vendor logic removed!")
