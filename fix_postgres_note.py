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
            line = re.sub(r', note', '', line)
            new_lines.append(line)
        elif in_copy_block:
            if line.strip() == '\\.':
                in_copy_block = False
                new_lines.append(line)
            else:
                cols = line.split('\t')
                # Wait, earlier we removed 4 columns (indices 3,12,13,15) from 21 total.
                # So now there are 17 columns left.
                # In the original 21, note was index 11.
                # Since we removed index 3 (platform), note shifted to index 10.
                # Let's just find the index of note in the new header if needed, but it's easier to just remove index 10.
                if len(cols) == 17:
                    new_cols = [col for i, col in enumerate(cols) if i != 10]
                    new_lines.append('\t'.join(new_cols))
                else:
                    new_lines.append(line)
        else:
            if 'note character varying' not in line and 'note VARCHAR' not in line:
                new_lines.append(line)

    with open(filepath, 'w') as f:
        f.writelines(new_lines)
    print("Updated postgres_data.sql")
