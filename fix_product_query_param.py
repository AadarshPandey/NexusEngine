import re

file_path = "NexusCore/nexus-admin/src/main/java/com/nexusengine/core/dto/PmsProductQueryParam.java"
with open(file_path, "r") as f:
    content = f.read()

replacements = {
    r'(Integer publishStatus)': r'@com.fasterxml.jackson.annotation.JsonProperty("isPublished") \1',
    r'(Integer verifyStatus)': r'@com.fasterxml.jackson.annotation.JsonProperty("approvalStatus") \1',
    r'(String productSn)': r'@com.fasterxml.jackson.annotation.JsonProperty("productCode") \1',
}

for pattern, replacement in replacements.items():
    content = re.sub(pattern, replacement, content)

with open(file_path, "w") as f:
    f.write(content)

print("Done")
