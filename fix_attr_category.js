const fs = require('fs');
const file = './NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductAttributeCategoryServiceImpl.java';
let content = fs.readFileSync(file, 'utf8');

// we need to inject the PmsProductAttributeRepository
content = content.replace(
    /private final PmsProductAttributeCategoryRepository productAttributeCategoryRepository;/g, 
    `private final PmsProductAttributeCategoryRepository productAttributeCategoryRepository;
    private final com.nexusengine.core.repository.PmsProductAttributeRepository productAttributeRepository;`
);

content = content.replace(/return new ArrayList\<\>\(\); \/\/ Legacy DTO mapping bypassed for compilation/g, `
        List<PmsProductAttributeCategory> allCategories = productAttributeCategoryRepository.findAll();
        List<PmsProductAttributeCategoryItem> result = new ArrayList<>();
        
        for (PmsProductAttributeCategory category : allCategories) {
            PmsProductAttributeCategoryItem item = new PmsProductAttributeCategoryItem();
            org.springframework.beans.BeanUtils.copyProperties(category, item);
            
            // fetch attributes
            List<com.nexusengine.core.model.PmsProductAttribute> attrs = productAttributeRepository.findByProductAttributeCategoryId(category.getId());
            item.setProductAttributeList(attrs);
            
            result.add(item);
        }
        return result;`);

fs.writeFileSync(file, content);
