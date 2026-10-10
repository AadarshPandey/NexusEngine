import re
import os

files_to_update = {
    "PmsProduct.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String brandName;', 
         '    @org.hibernate.annotations.Formula("(SELECT b.name FROM catalog.brand b WHERE b.id = brand_id)")\n    private String brandName;')
    ],
    "PmsProductCategory.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private Integer productCount;',
         '    @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM catalog.product p WHERE p.category_id = id AND p.deleted_at IS NULL)")\n    private Integer productCount;')
    ],
    "PmsProductAttributeCategory.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private Integer attributeCount;',
         '    @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM catalog.attribute_definition a WHERE a.product_type_id = id AND a.kind = 0)")\n    private Integer attributeCount;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private Integer paramCount;',
         '    @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM catalog.attribute_definition a WHERE a.product_type_id = id AND a.kind = 1)")\n    private Integer paramCount;')
    ],
    "UmsRole.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private Integer adminCount;',
         '    @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM iam.admin_user_role r WHERE r.role_id = id)")\n    private Integer adminCount;')
    ],
    "OmsCartItem.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productPic;',
         '    @org.hibernate.annotations.Formula("(SELECT p.primary_image_url FROM catalog.product p WHERE p.id = product_id)")\n    private String productPic;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productName;',
         '    @org.hibernate.annotations.Formula("(SELECT p.name FROM catalog.product p WHERE p.id = product_id)")\n    private String productName;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productBrand;',
         '    @org.hibernate.annotations.Formula("(SELECT b.name FROM catalog.brand b JOIN catalog.product p ON p.brand_id = b.id WHERE p.id = product_id)")\n    private String productBrand;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productSn;',
         '    @org.hibernate.annotations.Formula("(SELECT p.product_code FROM catalog.product p WHERE p.id = product_id)")\n    private String productSn;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productSkuCode;',
         '    @org.hibernate.annotations.Formula("(SELECT s.sku_code FROM catalog.product_sku s WHERE s.id = sku_id)")\n    private String productSkuCode;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productAttr;',
         '    @org.hibernate.annotations.Formula("(SELECT s.sku_attributes FROM catalog.product_sku s WHERE s.id = sku_id)")\n    private String productAttr;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private Long productCategoryId;',
         '    @org.hibernate.annotations.Formula("(SELECT p.category_id FROM catalog.product p WHERE p.id = product_id)")\n    private Long productCategoryId;')
    ],
    "OmsOrderReturnApply.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String memberUsername;',
         '    @org.hibernate.annotations.Formula("(SELECT m.username FROM customer.member m WHERE m.id = member_id)")\n    private String memberUsername;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String orderSn;',
         '    @org.hibernate.annotations.Formula("(SELECT o.order_number FROM ordering.customer_order o WHERE o.id = order_id)")\n    private String orderSn;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productPic;',
         '    @org.hibernate.annotations.Formula("(SELECT l.product_image_url FROM ordering.order_line l WHERE l.id = order_line_id)")\n    private String productPic;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productName;',
         '    @org.hibernate.annotations.Formula("(SELECT l.product_name FROM ordering.order_line l WHERE l.id = order_line_id)")\n    private String productName;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productBrand;',
         '    @org.hibernate.annotations.Formula("(SELECT l.brand_name FROM ordering.order_line l WHERE l.id = order_line_id)")\n    private String productBrand;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String productAttr;',
         '    @org.hibernate.annotations.Formula("(SELECT l.sku_attributes FROM ordering.order_line l WHERE l.id = order_line_id)")\n    private String productAttr;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private java\.math\.BigDecimal productPrice;',
         '    @org.hibernate.annotations.Formula("(SELECT l.unit_price FROM ordering.order_line l WHERE l.id = order_line_id)")\n    private java.math.BigDecimal productPrice;')
    ],
    "SmsCouponHistory.java": [
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String couponCode;',
         '    @org.hibernate.annotations.Formula("(SELECT c.code FROM promotion.coupon c WHERE c.id = coupon_id)")\n    private String couponCode;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String memberNickname;',
         '    @org.hibernate.annotations.Formula("(SELECT m.display_name FROM customer.member m WHERE m.id = member_id)")\n    private String memberNickname;'),
        (r'(@Column\([^)]+\)\s*)?(@Schema\([^)]+\)\s*)?private String orderSn;',
         '    @org.hibernate.annotations.Formula("(SELECT o.order_number FROM ordering.customer_order o WHERE o.id = order_id)")\n    private String orderSn;')
    ]
}

model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"

for filename, replacements in files_to_update.items():
    filepath = os.path.join(model_dir, filename)
    if os.path.exists(filepath):
        with open(filepath, "r") as f:
            content = f.read()
        
        orig_content = content
        for pattern, replacement in replacements:
            content = re.sub(pattern, replacement, content)
            
        if content != orig_content:
            with open(filepath, "w") as f:
                f.write(content)
            print(f"Updated {filename}")
