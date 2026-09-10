import os
import re

path = "/home/aadarsh/Documents/NexusEngine/NexusCore/nexus-admin/src/main/java/com/nexusengine/core/controller/UmsAdminController.java"
with open(path, "r") as f:
    content = f.read()

# Add import
if "import org.springframework.security.access.prepost.PreAuthorize;" not in content:
    content = content.replace("import org.springframework.web.bind.annotation.*;", "import org.springframework.web.bind.annotation.*;\nimport org.springframework.security.access.prepost.PreAuthorize;")

# Add annotations
content = content.replace(
    "@RequestMapping(value = \"/register\", method = RequestMethod.POST)",
    "@RequestMapping(value = \"/register\", method = RequestMethod.POST)\n    @PreAuthorize(\"hasAuthority('ums:admin:create')\")"
)

content = content.replace(
    "@RequestMapping(value = \"/update/{id}\", method = RequestMethod.POST)",
    "@RequestMapping(value = \"/update/{id}\", method = RequestMethod.POST)\n    @PreAuthorize(\"hasAuthority('ums:admin:update')\")"
)

content = content.replace(
    "@RequestMapping(value = \"/updatePassword\", method = RequestMethod.POST)",
    "@RequestMapping(value = \"/updatePassword\", method = RequestMethod.POST)\n    @PreAuthorize(\"hasAuthority('ums:admin:update')\")"
)

content = content.replace(
    "@RequestMapping(value = \"/delete/{id}\", method = RequestMethod.POST)",
    "@RequestMapping(value = \"/delete/{id}\", method = RequestMethod.POST)\n    @PreAuthorize(\"hasAuthority('ums:admin:delete')\")"
)

content = content.replace(
    "@RequestMapping(value = \"/updateStatus/{id}\", method = RequestMethod.POST)",
    "@RequestMapping(value = \"/updateStatus/{id}\", method = RequestMethod.POST)\n    @PreAuthorize(\"hasAuthority('ums:admin:update')\")"
)

content = content.replace(
    "@RequestMapping(value = \"/role/update\", method = RequestMethod.POST)",
    "@RequestMapping(value = \"/role/update\", method = RequestMethod.POST)\n    @PreAuthorize(\"hasAuthority('ums:admin:update')\")"
)

with open(path, "w") as f:
    f.write(content)

print("Done admin controller")
