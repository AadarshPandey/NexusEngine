const fs = require('fs');
const file = './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberServiceImpl.java';
let content = fs.readFileSync(file, 'utf8');

content = content.replace(/private final org\.springframework\.mail\.javamail\.JavaMailSender mailSender;\n/g, '');

content = content.replace(/try {\n\s*org\.springframework\.mail\.SimpleMailMessage message = new org\.springframework\.mail\.SimpleMailMessage\(\);\n\s*message\.setFrom\(fromEmail\);\n\s*message\.setTo\(email\);\n\s*message\.setSubject\("Your Registration OTP"\);\n\s*message\.setText\("Your OTP code is: " \+ sb\.toString\(\) \+ "\\nIt is valid for " \+ \(AUTH_CODE_EXPIRE_SECONDS \/ 60\) \+ " minutes\."\);\n\s*mailSender\.send\(message\);\n\s*} catch \(Exception e\) {\n\s*LOGGER\.error\("Failed to send OTP email", e\);\n\s*org\.springframework\.security\.authentication\.BadCredentialsException ex = new org\.springframework\.security\.authentication\.BadCredentialsException\("Failed to send email\. Please check your SMTP configuration\."\);\n\s*ex\.initCause\(e\);\n\s*throw ex;\n\s*}/g, 'LOGGER.info("OTP code generated: " + sb.toString());');

fs.writeFileSync(file, content);
