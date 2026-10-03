import re

with open('./NexusCore/nexus-admin/src/main/java/com/nexusengine/core/dto/PmsProductParam.java', 'r') as f:
    content = f.read()

# Fields to remove if it were a normal class, but it's a record! Wait, PmsProductParam uses PmsProduct.
# Let's check PmsProductParam.java
