import re

with open("README.md", "r", encoding="utf-8") as f:
    content = f.read()

schema_section = """
## 🗄️ Database Schema & Relationships

NexusEngine organizes its database into strict logical schemas representing bounded contexts. Below are the primary modules, their core tables, and their entity relationships.

### Core Modules

1. **Catalog (`catalog` schema)**
   - Manages the core product information, categorizations, and variations (SKUs).
   - **Key Tables**: `product`, `category`, `product_sku`, `brand`, `attribute_definition`, `product_attribute_value`.

2. **Ordering (`ordering` schema)**
   - Handles the entire lifecycle of an order from cart to fulfillment and returns.
   - **Key Tables**: `customer_order`, `order_line`, `cart_item`, `order_status_history`, `return_request`.

3. **Customer (`customer` schema)**
   - Manages user accounts, profiles, addresses, and login telemetry.
   - **Key Tables**: `member`, `member_address`, `member_login_event`.

4. **Promotion (`promotion` schema)**
   - Handles discounting engines, coupon claims, and time-limited flash sales.
   - **Key Tables**: `coupon`, `member_coupon`, `flash_sale`, `flash_sale_item`.

5. **Security & System (`public` / `system` schema)**
   - Admin RBAC, dynamic routing permissions, and background job event tracking.
   - **Key Tables**: `ums_admin`, `ums_role`, `ums_resource`, `staged_event`.

### Entity-Relationship Diagram (Core Flow)

Below is a simplified ER diagram mapping how a Customer, their Orders, and the Products interconnect.

```mermaid
erDiagram
    CUSTOMER-MEMBER ||--o{ CUSTOMER-ORDER : places
    CUSTOMER-MEMBER ||--o{ MEMBER-ADDRESS : owns
    CUSTOMER-MEMBER ||--o{ CART-ITEM : manages
    
    CUSTOMER-ORDER ||--|{ ORDER-LINE : contains
    CUSTOMER-ORDER }|--|| MEMBER-ADDRESS : ships_to
    
    ORDER-LINE }|--|| PRODUCT-SKU : references
    PRODUCT-SKU }|--|| PRODUCT : belongs_to
    
    PRODUCT }|--|| CATEGORY : categorized_under
    PRODUCT }|--|| BRAND : manufactured_by
    
    PRODUCT ||--o{ PRODUCT-ATTRIBUTE-VALUE : has
    PRODUCT-ATTRIBUTE-VALUE }|--|| ATTRIBUTE-DEFINITION : maps_to

    CUSTOMER-MEMBER {
        bigint id PK
        varchar username
        varchar password
        varchar phone
        int status
    }
    
    CUSTOMER-ORDER {
        bigint id PK
        bigint member_id FK
        varchar order_sn
        decimal total_amount
        int status
        int pay_type
    }
    
    PRODUCT {
        bigint id PK
        bigint category_id FK
        bigint brand_id FK
        varchar name
        varchar product_sn
        decimal price
        int stock
    }
    
    PRODUCT-SKU {
        bigint id PK
        bigint product_id FK
        varchar sku_code
        decimal price
        int stock
        jsonb attributes
    }
```

"""

if "Database Schema & Relationships" not in content:
    # Insert it before "## 🚀 Getting Started"
    content = content.replace("## 🚀 Getting Started", schema_section + "\n## 🚀 Getting Started")
    
    with open("README.md", "w", encoding="utf-8") as f:
        f.write(content)
    print("README.md updated.")
else:
    print("Section already exists.")
