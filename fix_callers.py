import os

replacements = {
    "NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsCouponHistoryServiceImpl.java": {
        "findByCouponIdAndUseStatus": "findByCouponIdAndStatus"
    }
}

for filepath, repls in replacements.items():
    if os.path.exists(filepath):
        with open(filepath, 'r') as f:
            content = f.read()
        for old, new in repls.items():
            content = content.replace(old, new)
        with open(filepath, 'w') as f:
            f.write(content)
print("Callers updated.")
