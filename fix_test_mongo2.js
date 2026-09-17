const fs = require('fs');
const file = './NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/PmsProductSemanticSearchServiceIntegrationTest.java';
let content = fs.readFileSync(file, 'utf8');
content = content.replace(/@MockBean\n\s*private com\.nexusengine\.core\.portal\.repository\.MemberProductCollectionRepository memberProductCollectionRepository;\n/g, '');
content = content.replace(/@MockBean\n\s*private com\.nexusengine\.core\.portal\.repository\.MemberReadHistoryRepository memberReadHistoryRepository;\n/g, '');
fs.writeFileSync(file, content);
