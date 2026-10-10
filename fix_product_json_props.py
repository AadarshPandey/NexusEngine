import re

file_path = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PmsProduct.java"
with open(file_path, "r") as f:
    content = f.read()

replacements = {
    r'(private String productSn;)': r'@com.fasterxml.jackson.annotation.JsonProperty("productCode")\n    \1',
    r'(private Integer publishStatus;)': r'@com.fasterxml.jackson.annotation.JsonProperty("isPublished")\n    \1',
    r'(private Integer newStatus;)': r'@com.fasterxml.jackson.annotation.JsonProperty("isNewArrival")\n    \1',
    r'(private Integer recommendStatus;)': r'@com.fasterxml.jackson.annotation.JsonProperty("isFeatured")\n    \1',
    r'(private Integer verifyStatus;)': r'@com.fasterxml.jackson.annotation.JsonProperty("approvalStatus")\n    \1',
    r'(private Integer sale;)': r'@com.fasterxml.jackson.annotation.JsonProperty("unitsSold")\n    \1',
    r'(private BigDecimal price;)': r'@com.fasterxml.jackson.annotation.JsonProperty("listPrice")\n    \1',
    r'(private Integer stock;)': r'@com.fasterxml.jackson.annotation.JsonProperty("onHandQty")\n    \1',
}

for pattern, replacement in replacements.items():
    if '@com.fasterxml.jackson.annotation.JsonProperty' not in re.search(pattern, content).group(0) if re.search(pattern, content) else True:
        content = re.sub(pattern, replacement, content)

with open(file_path, "w") as f:
    f.write(content)

print("Done")
