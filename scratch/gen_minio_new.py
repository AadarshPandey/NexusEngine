import subprocess
import os
import shutil

def run_query(query):
    result = subprocess.run(
        ["docker", "exec", "-i", "postgres", "psql", "-U", "postgres", "-d", "nexuscore", "-t", "-c", query],
        capture_output=True, text=True
    )
    lines = [line.strip() for line in result.stdout.strip().split('\n') if line.strip()]
    data = []
    for line in lines:
        parts = line.split('|')
        data.append([p.strip() for p in parts])
    return data

def create_svg(path, text):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    svg = f"""<svg xmlns="http://www.w3.org/2000/svg" width="400" height="400">
  <rect width="100%" height="100%" fill="#496d89"/>
  <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="yellow" font-size="20" font-family="sans-serif">{text}</text>
</svg>"""
    with open(path, 'w') as f:
        f.write(svg)

base_dir = "/home/aadarsh/Documents/NexusEngine/scratch/nexus-media-new"
os.makedirs(base_dir, exist_ok=True)

sql_updates = []
url_prefix = "http://localhost:9000/nexus-media/public/catalog"

print("Fetching brands...")
brands = run_query("SELECT id, name FROM pms_brand;")
for b in brands:
    b_id, b_name = b[0], b[1]
    b_dir = f"{base_dir}/public/catalog/brands/{b_id}"
    create_svg(f"{b_dir}/logo.svg", f"Brand {b_name} Logo")
    create_svg(f"{b_dir}/logo-dark.svg", f"Brand {b_name} Logo Dark")
    create_svg(f"{b_dir}/banner.svg", f"Brand {b_name} Banner")
    create_svg(f"{b_dir}/banner-mobile.svg", f"Brand {b_name} Banner Mobile")
    sql_updates.append(f"UPDATE pms_brand SET logo = '{url_prefix}/brands/{b_id}/logo.svg', big_pic = '{url_prefix}/brands/{b_id}/banner.svg' WHERE id = {b_id};")

print("Fetching categories...")
categories = run_query("SELECT id, name, parent_id FROM pms_product_category;")
for c in categories:
    c_id, c_name, parent_id = c[0], c[1], c[2]
    
    if parent_id == '0':
        c_dir = f"{base_dir}/public/catalog/categories/{c_id}"
        create_svg(f"{c_dir}/icon.svg", f"Cat {c_name} Icon")
        create_svg(f"{c_dir}/icon-dark.svg", f"Cat {c_name} Icon Dark")
        create_svg(f"{c_dir}/banner.svg", f"Cat {c_name} Banner")
        sql_updates.append(f"UPDATE pms_product_category SET icon = '{url_prefix}/categories/{c_id}/icon.svg' WHERE id = {c_id};")
    else:
        # Subcategory
        c_dir = f"{base_dir}/public/catalog/categories/{parent_id}/subcategories/{c_id}"
        create_svg(f"{c_dir}/icon.svg", f"Subcat {c_name} Icon")
        sql_updates.append(f"UPDATE pms_product_category SET icon = '{url_prefix}/categories/{parent_id}/subcategories/{c_id}/icon.svg' WHERE id = {c_id};")


print("Fetching products...")
products = run_query("SELECT id, name FROM pms_product;")
for p in products:
    p_id, p_name = p[0], p[1]
    p_dir = f"{base_dir}/public/catalog/products/{p_id}"
    create_svg(f"{p_dir}/main.svg", f"Prod {p_name} Main")
    create_svg(f"{p_dir}/gallery/01.svg", f"Prod {p_name} Gal1")
    create_svg(f"{p_dir}/gallery/02.svg", f"Prod {p_name} Gal2")
    create_svg(f"{p_dir}/gallery/03.svg", f"Prod {p_name} Gal3")
    
    # Wait, pms_product pic is single URL, album_pics can hold gallery
    sql_updates.append(f"UPDATE pms_product SET pic = '{url_prefix}/products/{p_id}/main.svg', album_pics = '{url_prefix}/products/{p_id}/gallery/01.svg,{url_prefix}/products/{p_id}/gallery/02.svg,{url_prefix}/products/{p_id}/gallery/03.svg' WHERE id = {p_id};")


print("Fetching skus...")
skus = run_query("SELECT id, sku_code FROM pms_sku_stock;")
for s in skus:
    s_id, s_code = s[0], s[1]
    s_dir = f"{base_dir}/public/catalog/skus/{s_code}"
    create_svg(f"{s_dir}/main.svg", f"SKU {s_code} Main")
    create_svg(f"{s_dir}/swatch.svg", f"SKU {s_code} Swatch")
    create_svg(f"{s_dir}/gallery/01.svg", f"SKU {s_code} Gal1")
    create_svg(f"{s_dir}/gallery/02.svg", f"SKU {s_code} Gal2")
    sql_updates.append(f"UPDATE pms_sku_stock SET pic = '{url_prefix}/skus/{s_code}/main.svg' WHERE id = {s_id};")

# Also create marketing and users
m_dir = f"{base_dir}/public/marketing"
os.makedirs(f"{m_dir}/banners", exist_ok=True)
os.makedirs(f"{m_dir}/campaigns", exist_ok=True)
os.makedirs(f"{m_dir}/homepage", exist_ok=True)
create_svg(f"{m_dir}/homepage/hero.svg", "Homepage Hero Banner")

u_dir = f"{base_dir}/public/users/avatars/1"
create_svg(f"{u_dir}/avatar.svg", "User 1 Avatar")


with open('/home/aadarsh/Documents/NexusEngine/scratch/update_urls.sql', 'w') as f:
    f.write('\n'.join(sql_updates))
print("SQL generated successfully.")
