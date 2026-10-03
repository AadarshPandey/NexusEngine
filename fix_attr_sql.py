import re

# V1
with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql', 'r') as f:
    content = f.read()

# We need to find the CREATE TABLE pms_product_attribute block
table_start = content.find('CREATE TABLE pms_product_attribute (')
table_end = content.find(');', table_start)

fields = ["filter_type", "search_type", "related_status", "hand_add_status"]

if table_start != -1 and table_end != -1:
    table_content = content[table_start:table_end]
    for field in fields:
        pattern = r'\s*' + field + r'\s+[^,]+,'
        table_content = re.sub(pattern, '', table_content)
    content = content[:table_start] + table_content + content[table_end:]

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql', 'w') as f:
    f.write(content)

# V2
with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql', 'r') as f:
    content = f.read()

# Replace column list
content = content.replace(
    ', sort, filter_type, search_type, related_status, hand_add_status, type',
    ', sort, type'
)

# Values replacements
# The values typically look like: , 0, 0, 0, 0, 0, 0)
# Let's just do a smart regex: we want to replace the last 5 fields (4 waste + 1 type) with just type.
# We know type is 0 or 1.
# Usually: , sort, filter, search, related, hand, type)
# Wait, actually let's just do it directly.
content = re.sub(r', (\d+), (\d+), (\d+), (\d+), (\d+), (\d+)\)', r', \1, \6)', content)

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql', 'w') as f:
    f.write(content)

# postgres_data.sql
with open('./resource_setup/postgres_data.sql', 'r') as f:
    content = f.read()

# Update CREATE TABLE public.pms_product_attribute
table_start = content.find('CREATE TABLE public.pms_product_attribute (')
table_end = content.find(');', table_start)
if table_start != -1 and table_end != -1:
    table_content = content[table_start:table_end]
    for field in fields:
        pattern = r'\s*' + field + r'\s+[^,]+,'
        table_content = re.sub(pattern, '', table_content)
    content = content[:table_start] + table_content + content[table_end:]

# Update COPY statement
content = content.replace(
    'COPY catalog.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, filter_type, search_type, related_status, hand_add_status, type)',
    'COPY catalog.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, type)'
)
content = content.replace(
    'COPY public.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, filter_type, search_type, related_status, hand_add_status, type)',
    'COPY public.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, type)'
)

# Data for catalog.pms_product_attribute is right after the COPY statement.
# We need to remove the 4 columns before the last column.
# Format: id \t cat_id \t name \t select \t input \t list \t sort \t filter \t search \t related \t hand \t type
def filter_attr_row(match):
    parts = match.group(0).split('\t')
    if len(parts) == 12:
        new_parts = parts[:7] + [parts[11]]
        return '\t'.join(new_parts)
    return match.group(0)

# We know the block starts with 1\t1\t
# Wait, let's just find the COPY block and replace inside it
copy_str = 'COPY catalog.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, type) FROM stdin;\n'
start_idx = content.find(copy_str)
if start_idx != -1:
    start_idx += len(copy_str)
    end_idx = content.find('\.\n', start_idx)
    data_block = content[start_idx:end_idx]
    
    new_data_block = []
    for line in data_block.split('\n'):
        if line:
            parts = line.split('\t')
            if len(parts) == 12:
                new_parts = parts[:7] + [parts[11]]
                new_data_block.append('\t'.join(new_parts))
            else:
                new_data_block.append(line)
    
    content = content[:start_idx] + '\n'.join(new_data_block) + '\n' + content[end_idx:]

with open('./resource_setup/postgres_data.sql', 'w') as f:
    f.write(content)
