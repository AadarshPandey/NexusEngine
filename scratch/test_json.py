import subprocess
import json

result = subprocess.run(
    ["docker", "exec", "-i", "postgres", "psql", "-U", "postgres", "-d", "nexuscore", "-t", "-c", "SELECT product_id, sku_attributes::text FROM pms_sku_stock;"],
    capture_output=True, text=True
)

lines = [line.strip() for line in result.stdout.strip().split('\n') if line.strip()]
success = 0
for line in lines:
    parts = line.split('|')
    if len(parts) >= 2:
        try:
            j = json.loads(parts[1].strip())
            success += 1
        except Exception as e:
            pass
print(f"Parsed {success} JSONs.")
