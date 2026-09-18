import subprocess
import json

result = subprocess.run(
    ["docker", "exec", "-i", "postgres", "psql", "-U", "postgres", "-d", "nexuscore", "-t", "-c", "SELECT product_id, sku_attributes::text FROM pms_sku_stock WHERE product_id=1;"],
    capture_output=True, text=True
)

skus_raw = [line.strip().split('|') for line in result.stdout.strip().split('\n') if line.strip()]
sku_attrs = {}
for s in skus_raw:
    p_id = int(s[0].strip())
    j = json.loads(s[1].strip())
    if p_id not in sku_attrs: sku_attrs[p_id] = {}
    for k,v in j.items():
        k_lower = k.lower().strip()
        if k_lower not in sku_attrs[p_id]: sku_attrs[p_id][k_lower] = set()
        sku_attrs[p_id][k_lower].add(str(v))
print("sku_attrs:", sku_attrs)
