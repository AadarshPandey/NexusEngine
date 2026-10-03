import re

filepath = './resource_setup/postgres_data.sql'
with open(filepath, 'r') as f:
    content = f.read()

# 1. sms_coupon_product_category_relation
content = content.replace(
    'COPY marketing.sms_coupon_product_category_relation (id, coupon_id, product_category_id, product_category_name, parent_category_name) FROM stdin;',
    'COPY marketing.sms_coupon_product_category_relation (id, coupon_id, product_category_id) FROM stdin;'
)

# 2. sms_coupon_product_relation
content = content.replace(
    'COPY marketing.sms_coupon_product_relation (id, coupon_id, product_id, product_name, product_sn) FROM stdin;',
    'COPY marketing.sms_coupon_product_relation (id, coupon_id, product_id) FROM stdin;'
)

# 3. sms_flash_promotion_product_relation
content = content.replace(
    'COPY marketing.sms_flash_promotion_product_relation (id, flash_promotion_id, flash_promotion_session_id, product_id, flash_promotion_price, flash_promotion_count, flash_promotion_limit, sort) FROM stdin;',
    'COPY marketing.sms_flash_promotion_product_relation (id, flash_promotion_id, flash_promotion_session_id, product_id, flash_promotion_price, flash_promotion_count, flash_promotion_limit) FROM stdin;'
)

# We also need to strip the TSV data rows!
def process_data_block(table_name, num_cols_to_drop):
    global content
    pattern = r'(COPY marketing\.' + table_name + r' \([^)]+\) FROM stdin;\n)(.*?)(\\\.\n)'
    
    def repl(m):
        header = m.group(1)
        data = m.group(2)
        footer = m.group(3)
        
        new_data = []
        for line in data.split('\n'):
            if line.strip():
                parts = line.split('\t')
                parts = parts[:-num_cols_to_drop]
                new_data.append('\t'.join(parts))
            else:
                new_data.append(line)
                
        return header + '\n'.join(new_data) + footer

    content = re.sub(pattern, repl, content, flags=re.DOTALL)

process_data_block('sms_coupon_product_category_relation', 2)
process_data_block('sms_coupon_product_relation', 2)
process_data_block('sms_flash_promotion_product_relation', 1)

with open(filepath, 'w') as f:
    f.write(content)

print("postgres_data.sql TSV blocks stripped!")
