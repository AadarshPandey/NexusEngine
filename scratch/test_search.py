import requests
import json

# We need a JWT token first.
login_res = requests.post("http://localhost:8080/admin/login", json={"username": "admin", "password": "macro123"})
token = login_res.json()["data"]["token"]

headers = {"Authorization": token}
res = requests.get("http://localhost:8080/product/list?pageNum=1&pageSize=10&keyword=iPhone", headers=headers)
print("Total elements with keyword 'iPhone':", res.json()["data"]["total"])
