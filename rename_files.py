import os
import subprocess

file_renames = [
    # Feight -> Freight
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PmsFeightTemplate.java", "PmsFreightTemplate.java"),
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/PmsFeightTemplateRepository.java", "PmsFreightTemplateRepository.java"),
    
    # Vertify -> Verify
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PmsProductVertifyRecord.java", "PmsProductVerifyRecord.java"),
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/PmsProductVertifyRecordRepository.java", "PmsProductVerifyRecordRepository.java"),
    
    # Replay -> Reply
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/PmsCommentReplay.java", "PmsCommentReply.java"),
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/PmsCommentReplayRepository.java", "PmsCommentReplyRepository.java"),
    
    # Prefrence -> Preference
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/CmsPrefrenceArea.java", "CmsPreferenceArea.java"),
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/CmsPrefrenceAreaProductRelation.java", "CmsPreferenceAreaProductRelation.java"),
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/CmsPrefrenceAreaRepository.java", "CmsPreferenceAreaRepository.java"),
    ("NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/repository/CmsPrefrenceAreaProductRelationRepository.java", "CmsPreferenceAreaProductRelationRepository.java"),
    ("NexusCore/nexus-admin/src/main/java/com/nexusengine/core/controller/CmsPrefrenceAreaController.java", "CmsPreferenceAreaController.java"),
    ("NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/CmsPrefrenceAreaService.java", "CmsPreferenceAreaService.java"),
    ("NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/CmsPrefrenceAreaServiceImpl.java", "CmsPreferenceAreaServiceImpl.java")
]

for path, new_name in file_renames:
    if os.path.exists(path):
        dir_name = os.path.dirname(path)
        new_path = os.path.join(dir_name, new_name)
        subprocess.run(["git", "mv", path, new_path])
        print(f"Renamed {path} -> {new_path}")
    else:
        print(f"Skipping missing file: {path}")
