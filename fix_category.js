const fs = require('fs');
const file = './NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductCategoryServiceImpl.java';
let content = fs.readFileSync(file, 'utf8');

content = content.replace(/return new ArrayList\<\>\(\); \/\/ Bypass DAO compilation error/g, `
        List<PmsProductCategory> allCategories = productCategoryRepository.findAll();
        List<PmsProductCategoryWithChildrenItem> result = new ArrayList<>();
        
        for (PmsProductCategory category : allCategories) {
            if (category.getParentId() == 0) {
                PmsProductCategoryWithChildrenItem item = new PmsProductCategoryWithChildrenItem();
                org.springframework.beans.BeanUtils.copyProperties(category, item);
                
                List<PmsProductCategory> children = new ArrayList<>();
                for (PmsProductCategory child : allCategories) {
                    if (child.getParentId() != null && child.getParentId().equals(category.getId())) {
                        children.add(child);
                    }
                }
                item.setChildren(children);
                result.add(item);
            }
        }
        return result;`);

fs.writeFileSync(file, content);
