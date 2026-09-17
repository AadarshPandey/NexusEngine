const fs = require('fs');
const file = './NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductAttributeServiceImpl.java';
let content = fs.readFileSync(file, 'utf8');

// Inject the relation repository and attribute repository
content = content.replace(
    /private final PmsProductAttributeRepository productAttributeRepository;/g, 
    `private final PmsProductAttributeRepository productAttributeRepository;
    private final com.nexusengine.core.repository.PmsProductCategoryAttributeRelationRepository relationRepository;`
);

content = content.replace(/return new ArrayList\<\>\(\); \/\/ Bypass DTO query compilation error/g, `
        List<com.nexusengine.core.model.PmsProductCategoryAttributeRelation> relations = relationRepository.findAll();
        List<ProductAttrInfo> result = new ArrayList<>();
        
        for (com.nexusengine.core.model.PmsProductCategoryAttributeRelation rel : relations) {
            if (rel.getProductCategoryId() != null && rel.getProductCategoryId().equals(productCategoryId)) {
                com.nexusengine.core.model.PmsProductAttribute attr = productAttributeRepository.findById(rel.getProductAttributeId()).orElse(null);
                if (attr != null) {
                    ProductAttrInfo info = new ProductAttrInfo();
                    info.setAttributeId(attr.getId());
                    info.setAttributeCategoryId(attr.getProductAttributeCategoryId());
                    result.add(info);
                }
            }
        }
        return result;`);

fs.writeFileSync(file, content);
