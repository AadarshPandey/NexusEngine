sed -i 's/item.setDeleteStatus(1);/cartItemRepository.delete(item);/g' ./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java
sed -i 's/cartItemRepository.save(item);//g' ./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java

# For clear method:
sed -i 's/cartItemRepository.saveAll(items);/cartItemRepository.deleteAll(items);/g' ./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java

# For updateAttr:
sed -i 's/updateCart.setDeleteStatus(1);/cartItemRepository.delete(updateCart);/g' ./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java
sed -i 's/cartItemRepository.save(updateCart);//g' ./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsCartItemServiceImpl.java
