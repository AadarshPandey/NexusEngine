import re

filepath = "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java"
with open(filepath, "r") as f:
    content = f.read()

# 1. Remove setDeleteStatus(0)
content = re.sub(r'\s*cartItem\.setDeleteStatus\(0\);', '', content)

# 2. Rename repository methods
content = content.replace("AndDeleteStatus", "")

# 3. Remove the ', 0' parameter in findBy calls
# We know the calls are like: findBy...(..., 0);
content = re.sub(r'(cartItemRepository\.findBy[^)]+),\s*0\s*\)', r'\1)', content)

# 4. Replace soft delete with hard delete
content = re.sub(r'item\.setDeleteStatus\(1\);\s*cartItemRepository\.save\(item\);', r'cartItemRepository.delete(item);', content)
content = re.sub(r'updateCart\.setDeleteStatus\(1\);\s*cartItemRepository\.save\(updateCart\);', r'cartItemRepository.delete(updateCart);', content)

# For the loop case:
# for (OmsCartItem item : items) {
#     item.setDeleteStatus(1);
# }
# cartItemRepository.saveAll(items); -> cartItemRepository.deleteAll(items);
content = re.sub(r'item\.setDeleteStatus\(1\);', '', content)
content = content.replace("cartItemRepository.saveAll(items);", "cartItemRepository.deleteAll(items);")


with open(filepath, "w") as f:
    f.write(content)

print("Done")
