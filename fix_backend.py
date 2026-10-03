import re

# 1. SmsCouponProductCategoryRelation
path1 = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/SmsCouponProductCategoryRelation.java'
with open(path1, 'r') as f:
    c1 = f.read()

c1 = re.sub(r'\s*@Column\(name\s*=\s*"product_category_name"\)\s*@Schema\(title\s*=\s*"Product category name"\)\s*private\s*String\s*productCategoryName;', '', c1)
c1 = re.sub(r'\s*@Column\(name\s*=\s*"parent_category_name"\)\s*@Schema\(title\s*=\s*"Parent category name"\)\s*private\s*String\s*parentCategoryName;', '', c1)

with open(path1, 'w') as f:
    f.write(c1)

# 2. SmsCouponProductRelation
path2 = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/SmsCouponProductRelation.java'
with open(path2, 'r') as f:
    c2 = f.read()

c2 = re.sub(r'\s*@Column\(name\s*=\s*"product_name"\)\s*@Schema\(title\s*=\s*"Product name"\)\s*private\s*String\s*productName;', '', c2)
c2 = re.sub(r'\s*@Column\(name\s*=\s*"product_sn"\)\s*@Schema\(title\s*=\s*"Product sn"\)\s*private\s*String\s*productSn;', '', c2)

with open(path2, 'w') as f:
    f.write(c2)

# 3. SmsFlashPromotionProductRelation
path3 = './NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/SmsFlashPromotionProductRelation.java'
with open(path3, 'r') as f:
    c3 = f.read()

c3 = re.sub(r'\s*@Column\(name\s*=\s*"sort"\)\s*private\s*Integer\s*sort;', '', c3)

with open(path3, 'w') as f:
    f.write(c3)

print("Backend JPA entities cleaned!")
