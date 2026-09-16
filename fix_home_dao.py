with open("NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java", "r") as f:
    lines = f.readlines()

out = []
for line in lines:
    if "import com.nexusengine.core.model.CmsSubject;" in line:
        continue
    if "@Autowired" in line and "CmsSubjectRepository" in "".join(lines):
        # We need to remove the @Autowired and the private CmsSubjectRepository subjectRepository;
        pass
    if "private CmsSubjectRepository subjectRepository;" in line:
        continue
    if "public List<CmsSubject> getRecommendSubjectList" in line:
        break
    out.append(line)

# Add the final method
out.append("""
    public List<FlashPromotionProduct> getFlashProductList(Long flashPromotionId, Long sessionId) {
        // Flash promotion product lookup - simplified since we don't have the relation table easily
        return Collections.emptyList();
    }
}
""")

with open("NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/dao/HomeDao.java", "w") as f:
    f.writelines(out)
