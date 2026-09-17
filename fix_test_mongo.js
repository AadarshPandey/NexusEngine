const fs = require('fs');
const file = './NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/PmsProductSemanticSearchServiceIntegrationTest.java';
let content = fs.readFileSync(file, 'utf8');

content = content.replace(/import org\.springframework\.data\.mongodb\.core\.MongoTemplate;\n/g, '');
content = content.replace(/org\.springframework\.boot\.autoconfigure\.mongo\.MongoAutoConfiguration\.class,\n/g, '');
content = content.replace(/org\.springframework\.boot\.autoconfigure\.data\.mongo\.MongoDataAutoConfiguration\.class,\n/g, '');
content = content.replace(/@MockBean\n\s*private MongoTemplate mongoTemplate;\n/g, '');
content = content.replace(/@MockBean\n\s*private org\.springframework\.data\.mongodb\.core\.convert\.MongoConverter mongoConverter;\n/g, '');
content = content.replace(/@MockBean\n\s*private org\.springframework\.data\.mongodb\.MongoDatabaseFactory mongoDatabaseFactory;\n/g, '');
content = content.replace(/@MockBean\n\s*private com\.nexusengine\.core\.portal\.repository\.MemberBrandAttentionRepository memberBrandAttentionRepository;\n/g, '');

fs.writeFileSync(file, content);
