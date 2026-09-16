import re

file_path = "NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/controller/UmsMemberController.java"
with open(file_path, "r") as f:
    content = f.read()

# I will cleanly fix the controller by replacing the messed up block.
messed_up_block = """    @Operation(summary = "Info Operation")
    @RequestMapping(value = "/info", method = RequestMethod.GET)

    
    @Operation(summary = "Update Profile Operation")
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public CommonResult updateProfile(@org.springframework.web.bind.annotation.RequestBody UmsMember member) {
        UmsMember current = memberService.getCurrentMember();
        if (member.getNickname() != null) current.setNickname(member.getNickname());
        if (member.getPhone() != null) current.setPhone(member.getPhone());
        if (member.getIcon() != null) current.setIcon(member.getIcon());
        memberService.updateMember(current);
        return CommonResult.success(current);
    }

    public CommonResult info(Principal principal) {"""

clean_block = """    @Operation(summary = "Update Profile Operation")
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public CommonResult updateProfile(@org.springframework.web.bind.annotation.RequestBody UmsMember member) {
        UmsMember current = memberService.getCurrentMember();
        if (member.getNickname() != null) current.setNickname(member.getNickname());
        if (member.getPhone() != null) current.setPhone(member.getPhone());
        if (member.getIcon() != null) current.setIcon(member.getIcon());
        memberService.updateMember(current);
        return CommonResult.success(current);
    }

    @Operation(summary = "Info Operation")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult info(Principal principal) {"""

content = content.replace(messed_up_block, clean_block)
with open(file_path, "w") as f:
    f.write(content)
