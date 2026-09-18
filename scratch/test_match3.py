import subprocess
result = subprocess.run(
    ["docker", "exec", "-i", "postgres", "psql", "-U", "postgres", "-d", "nexuscore", "-t", "-c", "SELECT id, product_attribute_category_id, name FROM pms_product WHERE id=1;"],
    capture_output=True, text=True
)
print("prods_raw:", [line.strip().split('|') for line in result.stdout.strip().split('\n') if line.strip()])
