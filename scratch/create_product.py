import requests
import json
import boto3
from botocore.client import Config
import os

# 1. Login to get token
login_res = requests.post("http://localhost:8080/admin/login", json={"username": "admin", "password": "macro123"})
token = login_res.json()["data"]["tokenHead"] + login_res.json()["data"]["token"]
headers = {"Authorization": token, "Content-Type": "application/json"}

# 2. Create the product
product_data = {
    "name": "Lenovo LOQ 15IRX9 Gaming Laptop",
    "subTitle": "Intel Core i5 13th Gen with AI-Tuned Performance",
    "productCategoryId": 3,
    "brandId": 45,
    "price": 74990,
    "originalPrice": 91890,
    "stock": 18,
    "description": "Unleash next-level gameplay with the Lenovo LOQ 15.6-inch gaming laptop. Featuring an advanced Intel Core processor, NVIDIA GeForce RTX graphics, and the Lenovo AI Engine+ with the LA1 AI chip, it dynamically optimizes performance in real time. Designed with a high-refresh-rate FHD display, an ergonomic tactile keyboard with 100% anti-ghosting, and hyperchamber cooling technology to keep your system quiet and chilled under pressure.",
    "publishStatus": 1,
    "newStatus": 1,
    "recommendStatus": 1,
    "verifyStatus": 1,
    "pic": ""
}

create_res = requests.post("http://localhost:8080/product/create", json=product_data, headers=headers)
print("Create response:", create_res.json())

# Assuming the backend returns 200, we need to find the product ID.
# Let's search for the product we just created.
search_res = requests.get("http://localhost:8080/product/list?pageNum=1&pageSize=10&keyword=Lenovo LOQ", headers=headers)
product_id = search_res.json()["data"]["list"][0]["id"]
print("Created Product ID:", product_id)

# 3. Upload to MinIO
s3 = boto3.client('s3',
                  endpoint_url='http://localhost:9000',
                  aws_access_key_id='minioadmin',
                  aws_secret_access_key='minioadmin',
                  config=Config(signature_version='s3v4'),
                  region_name='us-east-1')

bucket_name = 'nexus-media'
object_name = f'public/catalog/products/{product_id}/main.jpg'
file_path = '/home/aadarsh/.gemini/antigravity-cli/brain/0e4b0c25-c1b2-4288-9166-c86a00791daf/lenovo_loq_laptop_1789687735676.jpg'

s3.upload_file(file_path, bucket_name, object_name, ExtraArgs={'ContentType': 'image/jpeg'})
pic_url = f"http://localhost:9000/{bucket_name}/{object_name}"
print("Uploaded image to:", pic_url)

# 4. Update the product with the pic URL
product_data["pic"] = pic_url
update_res = requests.post(f"http://localhost:8080/product/update/{product_id}", json=product_data, headers=headers)
print("Update response:", update_res.json())

