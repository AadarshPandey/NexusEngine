const fs = require('fs');
const file = '/home/aadarsh/Documents/NexusEngine/NexusFrontendWeb/src/views/Checkout.tsx';
let content = fs.readFileSync(file, 'utf8');
content = content.replace(
  "contact: '+919000090000'",
  "email: 'test@example.com',\n          contact: '9000090000'"
);
fs.writeFileSync(file, content);
