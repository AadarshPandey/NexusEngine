import os
import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql'
with open(filepath, 'r') as f:
    content = f.read()

# Replace min_point -> min_order_amount
content = content.replace('min_point', 'min_order_amount')

# Remove platform, publish_count, use_count, enable_time
# e.g., INSERT INTO sms_coupon (id, type, name, platform, count, amount, per_limit, min_order_amount, start_time, end_time, use_type, note, publish_count, use_count, receive_count, enable_time, code, max_discount_amount, version, stackable, is_public) 
# VALUES (1, 0, 'Welcome Coupon', 0, 1000, 10.00, 1, 50.00, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 0, 'New user discount', 1000, 0, 0, '2024-01-01 00:00:00', 'WELCOME10', NULL, 0, false, true);

# Regex to find INSERT INTO sms_coupon (...) VALUES (...)
pattern = r"INSERT INTO sms_coupon \(([^)]+)\) VALUES \(([^)]+)\)"
def replacer(match):
    cols_str = match.group(1)
    vals_str = match.group(2)
    
    cols = [c.strip() for c in cols_str.split(',')]
    # Values might contain commas in quotes, but here they are simple numbers/dates/strings
    # Let's use split(',') carefully
    import csv
    from io import StringIO
    reader = csv.reader([vals_str], skipinitialspace=True)
    vals = list(reader)[0]
    # some strings might have single quotes instead of double quotes, csv module handles double quotes by default.
    # Actually, SQL uses single quotes. Let's just split by comma and handle it if there's no commas inside strings.
    # Fortunately these seeds don't have commas in the names/notes.
    
    if len(cols) != len(vals):
        # Fallback to simple split if csv fails
        vals = [v.strip() for v in vals_str.split(',')]
        
    new_cols = []
    new_vals = []
    for c, v in zip(cols, vals):
        if c not in ['platform', 'publish_count', 'use_count', 'enable_time']:
            new_cols.append(c)
            new_vals.append(v)
            
    return f"INSERT INTO sms_coupon ({', '.join(new_cols)}) VALUES ({', '.join(new_vals)})"

new_content = re.sub(pattern, replacer, content)

with open(filepath, 'w') as f:
    f.write(new_content)
print("Updated V2 seed")
