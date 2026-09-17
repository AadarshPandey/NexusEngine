const fs = require('fs');
const file = './NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductCategoryServiceImpl.java';
let content = fs.readFileSync(file, 'utf8');

// The DB has 0 for parent_id, so if parentId == 0L, it should just query for 0, not IsNull.
content = content.replace(/if \(parentId == 0L\) \{\n\s*return productCategoryRepository\.findByParentIdIsNullOrderBySortDesc\(pageable\);\n\s*\}/g, '');

fs.writeFileSync(file, content);
