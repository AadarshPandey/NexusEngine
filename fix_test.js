const fs = require('fs');
const file = './NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/unit/UmsMemberServiceImplTest.java';
let content = fs.readFileSync(file, 'utf8');
content = content.replace(/@Mock\n\s*private org\.springframework\.mail\.javamail\.JavaMailSender mailSender;/g, '');
fs.writeFileSync(file, content);
