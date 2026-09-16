import re

with open('NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/HomeController.java', 'r') as f:
    text = f.read()

text = re.sub(r'@Operation\(summary = "Get subject list Operation"\)[\s\S]*?return CommonResult\.success\(subjectList\);\n\s*\}\n', '', text)
text = re.sub(r'import com.nexusengine.core.model.CmsSubject;\n', '', text)

with open('NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/HomeController.java', 'w') as f:
    f.write(text)

with open('NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java', 'r') as f:
    text = f.read()

# Fix the dangling block in HomeDao.java
text = re.sub(r'import com.nexusengine.core.model.CmsSubject;\n', '', text)
text = re.sub(r'@Autowired\s+private CmsSubjectRepository subjectRepository;', '', text)
text = re.sub(r'public List<CmsSubject> getRecommendSubjectList[\s\S]*?\}\n', '', text)
# In case it was already broken, we'll just fix it by looking for the broken text
text = re.sub(r'public List<CmsSubject> getRecommendSubjectList.*?\{', '', text)

# if the method body is dangling:
text = re.sub(r'return subjectRepository\.findAll.*?;\n\s*\}', '', text)


with open('NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java', 'w') as f:
    f.write(text)
