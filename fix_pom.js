const fs = require('fs');
let content = fs.readFileSync('./NexusCore/nexus-portal/pom.xml', 'utf8');

content = content.replace(/<dependency>\s*<groupId>org\.springframework\.boot<\/groupId>\s*<artifactId>spring-boot-starter-data-mongodb<\/artifactId>\s*<\/dependency>/g, '');
content = content.replace(/<dependency>\s*<groupId>org\.springframework\.boot<\/groupId>\s*<artifactId>spring-boot-starter-mail<\/artifactId>\s*<\/dependency>/g, '');

fs.writeFileSync('./NexusCore/nexus-portal/pom.xml', content);
