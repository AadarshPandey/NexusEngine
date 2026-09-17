const fs = require('fs');
let content = fs.readFileSync('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java', 'utf8');

content = content.replace(
  'if (CollUtil.isEmpty(orderList)) return resultPage;',
  'if (CollUtil.isEmpty(orderList)) {\n            resultPage.setList(new ArrayList<>());\n            return resultPage;\n        }'
);

fs.writeFileSync('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java', content);
