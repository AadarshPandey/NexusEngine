import re

filepath = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V2__seed_full_catalog.sql'
with open(filepath, 'r') as f:
    content = f.read()

# We need to parse INSERT INTO sms_coupon (...) VALUES (...) and remove the 'note' column and its corresponding value.
pattern = r"INSERT INTO sms_coupon \(([^)]+)\) VALUES \(([^)]+)\)"
def replacer(match):
    cols_str = match.group(1)
    vals_str = match.group(2)
    
    cols = [c.strip() for c in cols_str.split(',')]
    
    import csv
    from io import StringIO
    # Use csv reader with quotechar="'" to correctly handle strings with commas
    reader = csv.reader([vals_str], skipinitialspace=True, quotechar="'")
    vals = list(reader)[0]
    
    new_cols = []
    new_vals = []
    for c, v in zip(cols, vals):
        if c != 'note':
            new_cols.append(c)
            # Re-quote the string if it was quoted. But actually csv reader strips quotes.
            # So if it was a string, we need to wrap it back.
            # A safer way if values don't have commas is to just split by comma:
            pass

    # Actually, simpler way: split by comma because V2 seeds for sms_coupon don't have commas in values!
    # Let's just use split(',')
    vals = [v.strip() for v in vals_str.split(',')]
    new_cols = []
    new_vals = []
    for c, v in zip(cols, vals):
        if c != 'note':
            new_cols.append(c)
            new_vals.append(v)
            
    return f"INSERT INTO sms_coupon ({', '.join(new_cols)}) VALUES ({', '.join(new_vals)})"

new_content = re.sub(pattern, replacer, content)

with open(filepath, 'w') as f:
    f.write(new_content)
print("Updated V2 seed")
