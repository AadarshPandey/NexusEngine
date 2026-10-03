import re

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql', 'r') as f:
    content = f.read()

fields = [
    "sort", "sub_title", "unit", "weight", "service_ids", "note",
    "promotion_start_time", "promotion_end_time", "promotion_per_limit", "promotion_type",
    "product_category_name", "vendor_id", "detail_title", "detail_desc", "detail_html"
]

# We need to find the CREATE TABLE pms_product block and only remove these fields from inside it.
table_start = content.find('CREATE TABLE pms_product (')
table_end = content.find(');', table_start)

if table_start != -1 and table_end != -1:
    table_content = content[table_start:table_end]
    
    for field in fields:
        # Match lines like "    sub_title VARCHAR(255)," or "    sort INT DEFAULT 0,"
        pattern = r'\s*' + field + r'\s+[^,]+,'
        table_content = re.sub(pattern, '', table_content)
        
    content = content[:table_start] + table_content + content[table_end:]
    
    with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql', 'w') as f:
        f.write(content)
