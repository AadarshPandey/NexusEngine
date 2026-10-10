import re

file_path = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PromotionalBanner.java"
with open(file_path, "r") as f:
    content = f.read()

replacements = {
    r'(private OffsetDateTime startTime;)': r'@com.fasterxml.jackson.annotation.JsonProperty("startsAt")\n    \1',
    r'(private OffsetDateTime endTime;)': r'@com.fasterxml.jackson.annotation.JsonProperty("endsAt")\n    \1',
    r'(private Integer sort;)': r'@com.fasterxml.jackson.annotation.JsonProperty("displayOrder")\n    \1',
}

for pattern, replacement in replacements.items():
    if 'JsonProperty("startsAt")' not in content and 'startsAt' in replacement:
        content = re.sub(pattern, replacement, content)
    elif 'JsonProperty("endsAt")' not in content and 'endsAt' in replacement:
        content = re.sub(pattern, replacement, content)
    elif 'JsonProperty("displayOrder")' not in content and 'displayOrder' in replacement:
        content = re.sub(pattern, replacement, content)

with open(file_path, "w") as f:
    f.write(content)

print("Done")
