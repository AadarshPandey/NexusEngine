import os
from PIL import Image, ImageDraw

def create_image(path, text):
    img = Image.new('RGB', (400, 400), color=(73, 109, 137))
    d = ImageDraw.Draw(img)
    d.text((10,10), text, fill=(255, 255, 0))
    img.save(path)

# Ensure base dir exists
base_dir = "/home/aadarsh/Documents/NexusEngine/scratch/nexus-media"
os.makedirs(os.path.join(base_dir, 'products'), exist_ok=True)
os.makedirs(os.path.join(base_dir, 'skus'), exist_ok=True)
os.makedirs(os.path.join(base_dir, 'brands'), exist_ok=True)
os.makedirs(os.path.join(base_dir, 'categories'), exist_ok=True)

# Generate a single dummy image and just copy it to save time
dummy_img_path = "/home/aadarsh/Documents/NexusEngine/scratch/dummy.jpg"
create_image(dummy_img_path, "Dummy")

import shutil

def copy_dummy(path):
    shutil.copy(dummy_img_path, path)

import re

# Read names from python script
with open('/home/aadarsh/Documents/NexusEngine/allProducts.txt', 'r') as f:
    content = f.read()

products = []
for m in re.finditer(r'\| (P\d{3}) \| (.*?) \| (.*?) \|', content):
    p_id = m.group(1).lower()
    if p_id not in products:
        products.append(p_id)
        copy_dummy(f"{base_dir}/products/{p_id}.jpg")
        
skus = []
for m in re.finditer(r'\|\s*(S\d{3})\s*\|\s*(P\d{3})\s*\|', content):
    s_id = m.group(1).lower()
    if s_id not in skus:
        skus.append(s_id)
        copy_dummy(f"{base_dir}/skus/{s_id}.jpg")

# Brands and categories need to match what we wrote to DB
from parse_products import categories, brands

for b in brands:
    b_name = b.replace(" ", "_").lower()
    copy_dummy(f"{base_dir}/brands/{b_name}.jpg")

for c, subcats in categories.items():
    c_name = c.replace(" ", "_").lower()
    copy_dummy(f"{base_dir}/categories/{c_name}.jpg")

print(f"Created {len(products)} products, {len(skus)} skus, {len(brands)} brands, {len(categories)} categories")
