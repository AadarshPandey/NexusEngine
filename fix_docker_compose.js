const fs = require('fs');
const file = './docker-compose.yml';
let content = fs.readFileSync(file, 'utf8');

// Remove mongodb service
content = content.replace(/  mongodb:[\s\S]*?(?=  rabbitmq:)/, '');
// Remove mongodb dependency from nexus-application
content = content.replace(/      mongodb:\n        condition: service_started\n/g, '');
// Remove mongodb volume
content = content.replace(/  mongodb-data:\n/, '');

fs.writeFileSync(file, content);
