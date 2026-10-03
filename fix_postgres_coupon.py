import os
import re

filepath = './resource_setup/postgres_data.sql'
if os.path.exists(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()

    new_lines = []
    in_copy_block = False
    
    for line in lines:
        if line.startswith('COPY marketing.sms_coupon ') or line.startswith('COPY public.sms_coupon '):
            in_copy_block = True
            # Update the COPY header
            line = line.replace('min_point', 'min_order_amount')
            line = re.sub(r', platform', '', line)
            line = re.sub(r', publish_count', '', line)
            line = re.sub(r', use_count', '', line)
            line = re.sub(r', enable_time', '', line)
            new_lines.append(line)
        elif in_copy_block:
            if line.strip() == '\\.':
                in_copy_block = False
                new_lines.append(line)
            else:
                cols = line.split('\t')
                if len(cols) == 21: # Original number of columns
                    # Remove indices: platform=3, publish_count=12, use_count=13, enable_time=15
                    new_cols = [col for i, col in enumerate(cols) if i not in [3, 12, 13, 15]]
                    new_lines.append('\t'.join(new_cols))
                else:
                    new_lines.append(line)
        else:
            # Update schema definitions outside of COPY
            if 'min_point NUMERIC' in line or 'min_point numeric' in line:
                line = line.replace('min_point', 'min_order_amount')
            if 'platform INTEGER' not in line and 'platform integer' not in line and \
               'publish_count INTEGER' not in line and 'publish_count integer' not in line and \
               'use_count INTEGER' not in line and 'use_count integer' not in line and \
               'enable_time TIMESTAMP' not in line and 'enable_time timestamp' not in line:
                new_lines.append(line)

    with open(filepath, 'w') as f:
        f.writelines(new_lines)
    print("Updated postgres_data.sql")
