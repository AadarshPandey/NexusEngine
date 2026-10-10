import os

os.chdir("NexusCore")
files_to_fix = [
    "nexus-admin-api/src/main/java/com/nexusengine/core/admin/analytics/controller/DashboardController.java",
    "nexus-admin-api/src/main/java/com/nexusengine/core/admin/analytics/service/DashboardService.java",
    "nexus-admin-api/src/main/java/com/nexusengine/core/admin/analytics/service/DashboardServiceImpl.java"
]

for filepath in files_to_fix:
    with open(filepath, "r") as f:
        content = f.read()
    
    if "import com.nexusengine.core.dto.DashboardInfo;" not in content:
        imports_end = content.find("import ")
        if imports_end != -1:
            end_of_line = content.find("\n", imports_end)
            content = content[:end_of_line+1] + "import com.nexusengine.core.dto.DashboardInfo;\n" + content[end_of_line+1:]
            
            with open(filepath, "w") as f:
                f.write(content)
