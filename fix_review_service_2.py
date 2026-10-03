import re

with open('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java', 'r') as f:
    content = f.read()

# Fix like() and dislike()
content = re.sub(r'\s*if \(comment != null\) \{\s*comment\.setLikesCount[^}]+\}\n', '\n', content)
content = re.sub(r'\s*if \(comment != null\) \{\s*comment\.setDislikeCount[^}]+\}\n', '\n', content)

# Fix the big block under // Sync to PmsComment
start_marker = "        // Sync to PmsComment"
end_marker = "        return saved;"

start_idx = content.find(start_marker)
end_idx = content.find(end_marker)

if start_idx != -1 and end_idx != -1:
    content = content[:start_idx] + "\n" + content[end_idx:]

# Also remove the leftover bottom:
#        return null;
#    }
#}
content = re.sub(r'        return null;\n    }\n}', '}', content)

with open('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java', 'w') as f:
    f.write(content)
