const fs = require('fs');
const file = './NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/integration/PmsProductSemanticSearchServiceIntegrationTest.java';
let content = fs.readFileSync(file, 'utf8');
content = content.replace(/@MockBean\n\s*private org\.springframework\.data\.mongodb\.gridfs\.GridFsTemplate gridFsTemplate;\n/g, '');
content = content.replace(/import org\.springframework\.data\.mongodb\.gridfs\.GridFsTemplate;\n/g, '');
// Since I removed the fields, the imports for the repositories might still be there if it was a fully qualified name it wouldn't complain about package, wait the error is:
// `package org.springframework.data.mongodb.gridfs does not exist`
// `package com.nexusengine.core.portal.repository does not exist` - wait, the repository package DOES exist, but maybe the specific classes?
// Let's just remove anything that matches MemberBrandAttentionRepository etc.
content = content.replace(/.*MemberBrandAttentionRepository.*/g, '');
content = content.replace(/.*MemberProductCollectionRepository.*/g, '');
content = content.replace(/.*MemberReadHistoryRepository.*/g, '');
content = content.replace(/.*GridFsTemplate.*/g, '');

fs.writeFileSync(file, content);
