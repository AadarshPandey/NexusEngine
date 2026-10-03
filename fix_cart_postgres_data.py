import re

filepath = './resource_setup/postgres_data.sql'
with open(filepath, 'r') as f:
    content = f.read()

# Update COPY statement
new_copy = 'COPY orders.oms_cart_item (id, product_id, product_sku_id, member_id, quantity, price, product_pic, product_name, product_sku_code, create_date, modify_date, delete_status, product_category_id, product_brand, product_sn, product_attr) FROM stdin;'
content = content.replace(
    'COPY orders.oms_cart_item (id, product_id, product_sku_id, member_id, quantity, price, product_pic, product_name, product_sub_title, product_sku_code, member_nickname, create_date, modify_date, delete_status, product_category_id, product_brand, product_sn, product_attr) FROM stdin;',
    new_copy
)

# Process TSV data block
pattern = r'(COPY orders\.oms_cart_item \([^)]+\) FROM stdin;\n)(.*?)(\\\.\n)'
def repl(m):
    header = m.group(1)
    data = m.group(2)
    footer = m.group(3)
    
    new_data = []
    for line in data.split('\n'):
        if line.strip():
            parts = line.split('\t')
            # Check if it has enough columns (it should be 18)
            if len(parts) >= 18:
                # Remove index 10 first to not shift index 8
                parts.pop(10)
                parts.pop(8)
            new_data.append('\t'.join(parts))
        else:
            new_data.append(line)
            
    return header + '\n'.join(new_data) + footer

content = re.sub(pattern, repl, content, flags=re.DOTALL)

with open(filepath, 'w') as f:
    f.write(content)

print("postgres_data.sql TSV blocks stripped!")
