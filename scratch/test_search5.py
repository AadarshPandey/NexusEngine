import requests

login_res = requests.post("http://localhost:8080/admin/login", json={"username": "admin", "password": "macro123"})
data = login_res.json()["data"]
token = data["tokenHead"] + data["token"]

headers = {"Authorization": token}
res = requests.get("http://localhost:8080/product/list?pageNum=1&pageSize=10&keyword=&productCategoryId=&brandId=&publishStatus=&verifyStatus=", headers=headers)
print("Status Code:", res.status_code)
print(res.text[:200])
