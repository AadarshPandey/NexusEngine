import requests
login_res = requests.post("http://localhost:8080/admin/login", json={"username": "admin", "password": "macro123"})
data = login_res.json()["data"]
token = data["tokenHead"] + data["token"]
res = requests.get("http://localhost:8080/brand/list?pageNum=1&pageSize=10&keyword=Apple", headers={"Authorization": token})
print("Total:", res.json()["data"]["total"])
for b in res.json()["data"]["list"]:
    print(f"ID: {b['id']}, Name: {b['name']}")
