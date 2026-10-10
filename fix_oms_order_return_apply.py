import re

path = 'NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OmsOrderReturnApply.java'
with open(path, 'r') as f:
    content = f.read()

# Replace Formula for order_sn
content = re.sub(
    r'@org\.hibernate\.annotations\.Formula\("\(SELECT o\.order_number FROM ordering\.customer_order o WHERE o\.id = order_id\)"\)',
    r'@Column(name = "order_sn")',
    content
)

# Replace Formula for product_pic
content = re.sub(
    r'@org\.hibernate\.annotations\.Formula\("\(SELECT l\.product_image_url FROM ordering\.order_line l WHERE l\.id = order_line_id\)"\)',
    r'@Column(name = "product_pic")',
    content
)

# Replace Formula for product_name
content = re.sub(
    r'@org\.hibernate\.annotations\.Formula\("\(SELECT l\.product_name FROM ordering\.order_line l WHERE l\.id = order_line_id\)"\)',
    r'@Column(name = "product_name")',
    content
)

# Replace Formula for product_brand
content = re.sub(
    r'@org\.hibernate\.annotations\.Formula\("\(SELECT l\.brand_name FROM ordering\.order_line l WHERE l\.id = order_line_id\)"\)',
    r'@Column(name = "product_brand")',
    content
)

# Replace Formula for product_attr
content = re.sub(
    r'@org\.hibernate\.annotations\.Formula\("\(SELECT l\.sku_attributes FROM ordering\.order_line l WHERE l\.id = order_line_id\)"\)',
    r'@Column(name = "product_attr")',
    content
)

with open(path, 'w') as f:
    f.write(content)

