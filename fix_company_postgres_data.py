import re

filepath = './resource_setup/postgres_data.sql'
with open(filepath, 'r') as f:
    content = f.read()

# Update COPY statement
old_copy = 'COPY orders.oms_company_address (id, address_name, send_status, receive_status, name, phone, province, city, region, detail_address) FROM stdin;'
new_copy = 'COPY orders.oms_company_address (id, address_name, send_status, receive_status, name, phone, state, city, detail_address) FROM stdin;'
content = content.replace(old_copy, new_copy)

# Process TSV data block
pattern = r'(COPY orders\.oms_company_address \([^)]+\) FROM stdin;\n)(.*?)(\\\.\n)'
def repl(m):
    header = m.group(1)
    data = m.group(2)
    footer = m.group(3)
    
    new_data = []
    for line in data.split('\n'):
        if line.strip():
            parts = line.split('\t')
            # Check if it has enough columns (it should be 10)
            if len(parts) >= 10:
                # Remove index 8 (region)
                parts.pop(8)
            new_data.append('\t'.join(parts))
        else:
            new_data.append(line)
            
    return header + '\n'.join(new_data) + footer

content = re.sub(pattern, repl, content, flags=re.DOTALL)

with open(filepath, 'w') as f:
    f.write(content)

print("postgres_data.sql TSV blocks stripped!")
