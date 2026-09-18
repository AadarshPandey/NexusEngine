import requests

login_res = requests.post("http://localhost:8080/admin/login", json={"username": "admin", "password": "macro123"})
token = login_res.json()["data"]["tokenHead"] + login_res.json()["data"]["token"]
headers = {"Authorization": token}

# Get Lenovo Brand ID
brands = requests.get("http://localhost:8080/brand/list?pageNum=1&pageSize=100&keyword=Lenovo", headers=headers).json()
lenovo_id = None
for b in brands["data"]["list"]:
    if b["name"] == "Lenovo":
        lenovo_id = b["id"]
        break

# Get Laptops Category ID
cates = requests.get("http://localhost:8080/productCategory/list/withChildren", headers=headers).json()
laptops_id = None
for parent in cates["data"]:
    for child in parent.get("children", []):
        if child["name"] == "Laptops":
            laptops_id = child["id"]
            break
            
print(f"Lenovo ID: {lenovo_id}")
print(f"Laptops ID: {laptops_id}")
