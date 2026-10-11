package com.nexusengine.core.admin.iam.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.admin.iam.dto.AdminLoginParam;
import com.nexusengine.core.admin.iam.dto.AdminUserParam;
import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.admin.iam.service.RoleService;
import com.nexusengine.core.admin.iam.dto.UpdatePasswordParam;
import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Represents the AdminUserController component.
 * Provides core functionality and operations for AdminUserController.
 */
@RestController
@Tag(name = "AdminUserController", description = "Ums admin controller APIs")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final AdminUserService adminService;
    private final RoleService roleService;

    @Operation(summary = "Register Operation")
    @PostMapping("/register")

    public CommonResult<AdminUser> register(@Validated @RequestBody AdminUserParam adminUserParam) {
        AdminUser adminUser = adminService.register(adminUserParam);
        if (adminUser == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(adminUser);
    }

    @Operation(summary = "Login Operation")
    @PostMapping("/login")

    public CommonResult login(@Validated @RequestBody AdminLoginParam adminLoginParam) {
        String token = adminService.login(adminLoginParam.username(), adminLoginParam.password());
        if (token == null) {
            return CommonResult.validateFailed("Invalid username or password");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @Operation(summary = "Refresh token Operation")
    @GetMapping("/refreshToken")

    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("Token refresh failed");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @Operation(summary = "Get admin info Operation")
    @GetMapping("/info")

    public CommonResult getAdminInfo(Principal principal) {
        if(principal==null){
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        AdminUser adminUser = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", adminUser.getUsername());
        data.put("menus", roleService.getMenuList(adminUser.getId()));
        data.put("icon", adminUser.getIcon());
        List<Role> roleList = adminService.getRoleList(adminUser.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(Role::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }
        return CommonResult.success(data);
    }

    @Operation(summary = "Logout Operation")
    @PostMapping("/logout")

    public CommonResult logout(Principal principal) {
        adminService.logout(principal.getName());
        return CommonResult.success(null);
    }

    @Operation(summary = "List Operation")
    @GetMapping("/list")

    public CommonResult<CommonPage<AdminUser>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        org.springframework.data.domain.Page<AdminUser> adminList = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @Operation(summary = "Get item Operation")
    @GetMapping("/{id}")

    public CommonResult<AdminUser> getItem(@PathVariable("id") Long id) {
        AdminUser admin = adminService.getItem(id);
        return CommonResult.success(admin);
    }

    @Operation(summary = "Update Operation")
    @PostMapping("/update/{id}")

    public CommonResult update(@PathVariable("id") Long id, @RequestBody AdminUser admin) {
        int count = adminService.update(id, admin);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update password Operation")
    @PostMapping("/updatePassword")

    public CommonResult updatePassword(@Validated @RequestBody UpdatePasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("Admin not found");
        } else if (status == -2) {
            return CommonResult.failed("Incorrect old password");
        } else if (status == -3) {
            return CommonResult.failed("Invalid password format");
        } else {
            return CommonResult.failed();
        }
    }

    @Operation(summary = "Delete Operation")
    @PostMapping("/delete/{id}")

    public CommonResult delete(@PathVariable("id") Long id) {
        int count = adminService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update status Operation")
    @PostMapping("/updateStatus/{id}")

    public CommonResult updateStatus(@PathVariable("id") Long id,@RequestParam(value = "status") Integer status) {
        AdminUser adminUser = new AdminUser();
        adminUser.setStatus(status);
        int count = adminService.update(id,adminUser);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Update role Operation")
    @PostMapping("/role/update")

    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "Get role list Operation")
    @GetMapping("/role/{adminId}")

    public CommonResult<List<Role>> getRoleList(@PathVariable("adminId") Long adminId) {
        List<Role> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

}
