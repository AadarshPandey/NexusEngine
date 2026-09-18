import requests

login_res = requests.post("http://localhost:8080/admin/login", json={"username": "admin", "password": "macro123"})
token = login_res.json()["data"]["tokenHead"] + login_res.json()["data"]["token"]
headers = {"Authorization": token}

res = requests.get("http://localhost:8080/resource/list?pageNum=1&pageSize=10", headers=headers)
print(res.json())
