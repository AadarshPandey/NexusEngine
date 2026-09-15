const bcrypt = require('bcryptjs');
console.log(bcrypt.compareSync('macro123', '$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO'));
