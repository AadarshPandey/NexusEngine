import re

with open('NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java', 'r') as f:
    lines = f.readlines()

new_lines = []
skip_next = False
for i, line in enumerate(lines):
    if skip_next:
        skip_next = False
        continue
    
    if '@Autowired' in line:
        if i + 1 < len(lines):
            next_line = lines[i+1]
            if 'subjectProductRelationRepository' in next_line or 'preferenceAreaProductRelationRepository' in next_line:
                skip_next = True
                continue
                
    if 'saveSubjectProductRelationList' in line or 'savePreferenceAreaProductRelationList' in line:
        continue
        
    if 'subjectProductRelationRepository' in line or 'preferenceAreaProductRelationRepository' in line:
        continue
        
    if 'relateAndInsertList(subjectProductRelationDao' in line or 'relateAndInsertList(prefrenceAreaProductRelationDao' in line:
        continue
        
    new_lines.append(line)

# Handle generic relateAndInsertList method removal (it's at the end)
content = "".join(new_lines)
# Remove the private void relateAndInsertList completely
content = re.sub(r'private void relateAndInsertList.*?\n\s*\}\n', '', content, flags=re.DOTALL)

with open('NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java', 'w') as f:
    f.write(content)
