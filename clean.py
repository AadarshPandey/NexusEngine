import re

with open("NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java", "r") as f:
    text = f.read()

# Remove specific imports or fields
text = re.sub(r'@Autowired\s+private CmsSubjectProductRelationRepository subjectProductRelationRepository;', '', text)
text = re.sub(r'@Autowired\s+private CmsPreferenceAreaProductRelationRepository preferenceAreaProductRelationRepository;', '', text)

text = re.sub(r'saveSubjectProductRelationList\(.*?\);', '', text)
text = re.sub(r'savePreferenceAreaProductRelationList\(.*?\);', '', text)

text = re.sub(r'subjectProductRelationRepository\.deleteByProductId.*?;\n', '', text)
text = re.sub(r'preferenceAreaProductRelationRepository\.deleteByProductId.*?;\n', '', text)

# Remove the whole methods
text = re.sub(r'private void saveSubjectProductRelationList[\s\S]*?\}\s*\}\s*\}', '', text)
text = re.sub(r'private void savePreferenceAreaProductRelationList[\s\S]*?\}\s*\}\s*\}', '', text)
text = re.sub(r'private void relateAndInsertList[\s\S]*?\}\s*\}', '', text)

# Just to be safe for any leftovers
text = re.sub(r'private void saveSubjectProductRelationList[\s\S]*?(?=private void |\}$)', '', text)
text = re.sub(r'private void savePreferenceAreaProductRelationList[\s\S]*?(?=private void |\}$)', '', text)
text = re.sub(r'private void relateAndInsertList[\s\S]*?(?=private void |\}$)', '', text)

with open("NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/PmsProductServiceImpl.java", "w") as f:
    f.write(text)
