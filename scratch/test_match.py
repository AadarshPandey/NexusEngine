import json

sku_attrs_by_product = {1: {'color': {'Black', 'Silver'}, 'storage': {'128GB', '256GB'}}}
p_id = 1
a_name = "Color"
k_lower = a_name.lower().strip()
matched_key = None
if p_id in sku_attrs_by_product:
    for k in sku_attrs_by_product[p_id]:
        if k == k_lower or k_lower in k or k in k_lower:
            matched_key = k
            break
if matched_key:
    val = ",".join(sorted(list(sku_attrs_by_product[p_id][matched_key])))
else:
    val = "Standard"
print(f"Matched {a_name} -> {val}")
