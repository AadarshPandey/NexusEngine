const fs = require('fs');
const file = '/home/aadarsh/Documents/NexusEngine/NexusFrontendWeb/src/views/Profile.tsx';
let content = fs.readFileSync(file, 'utf8');
content = content.replace(
  "contact: memberInfo?.phone || '+919000090000'",
  "email: 'test@example.com',\n                                      contact: '9000090000'"
);
fs.writeFileSync(file, content);
