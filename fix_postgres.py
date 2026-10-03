import re

with open('./resource_setup/postgres_data.sql', 'r') as f:
    content = f.read()

# Update CREATE TABLE public.pms_product
table_start = content.find('CREATE TABLE public.pms_product (')
table_end = content.find(');', table_start)

fields = [
    "sort", "sub_title", "unit", "weight", "service_ids", "note",
    "promotion_start_time", "promotion_end_time", "promotion_per_limit", "promotion_type",
    "product_category_name", "vendor_id", "detail_title", "detail_desc", "detail_html"
]

if table_start != -1 and table_end != -1:
    table_content = content[table_start:table_end]
    for field in fields:
        pattern = r'\s*' + field + r'\s+[^,]+,'
        table_content = re.sub(pattern, '', table_content)
    content = content[:table_start] + table_content + content[table_end:]

# Fix the COPY commands (there's one for catalog.pms_product and one for public.pms_product)
pattern = r'COPY (catalog|public)\.pms_product \(id, brand_id, product_category_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, unit, weight, keywords, detail_title\) FROM stdin;'
replacement = r'COPY \1.pms_product (id, brand_id, product_category_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sale, keywords) FROM stdin;'
content = re.sub(pattern, replacement, content)

# Fix the data rows for pms_product.
# Lines look like:
# 1	1	2	1	iPhone 17	http://localhost:9000/nexus-media/public/catalog/products/1-iphone-17/main.webp	APP-IPH-1	0	1	1	1	1	0	0	piece	500.00	iphone 17	iPhone 17 Details
def filter_row(match):
    parts = match.group(0).split('\t')
    if len(parts) >= 18:
        # Keep indices: 0..11, 13, 16
        new_parts = parts[:12] + [parts[13], parts[16]]
        return '\t'.join(new_parts)
    return match.group(0)

# Replace the data block for catalog.pms_product
content = re.sub(r'^\d+\t\d+\t.*\n', lambda m: filter_row(m) + '\n', content, flags=re.MULTILINE)

with open('./resource_setup/postgres_data.sql', 'w') as f:
    f.write(content)
