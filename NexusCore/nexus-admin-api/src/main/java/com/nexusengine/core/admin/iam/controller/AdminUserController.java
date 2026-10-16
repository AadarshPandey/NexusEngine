package com.nexusengine.core.admin.iam.controller;

import com.nexusengine.core.admin.iam.dto.AdminInfoResponse;
import com.nexusengine.core.admin.iam.dto.AdminLoginParam;
import com.nexusengine.core.admin.iam.dto.AdminUserParam;
import com.nexusengine.core.admin.iam.dto.TokenResponse;
import com.nexusengine.core.admin.iam.dto.UpdatePasswordParam;
import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.admin.iam.service.RoleService;
import com.nexusengine.core.common.api.ResultCode;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "IAM - Admin Users", description = "Admin user authentication and management APIs")
@RequestMapping("/api/admin/iam/users")
@RequiredArgsConstructor
public class AdminUserController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final AdminUserService adminService;
    private final RoleService roleService;

    @Operation(summary = "Register admin user")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUser register(@Valid @RequestBody AdminUserParam adminUserParam) {
        AdminUser adminUser = adminService.register(adminUserParam);
        if (adminUser == null) {
            Asserts.fail("Admin registration failed");
        }
        return adminUser;
    }

    @Operation(summary = "Login admin user")
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody AdminLoginParam adminLoginParam) {
        String token = adminService.login(adminLoginParam.username(), adminLoginParam.password());
        if (token == null) {
            Asserts.fail("Invalid username or password");
        }
        return new TokenResponse(token, tokenHead);
    }

    @Operation(summary = "Refresh JWT token")
    @GetMapping("/refresh-token")
    public TokenResponse refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (refreshToken == null) {
            Asserts.fail("Token refresh failed");
        }
        return new TokenResponse(refreshToken, tokenHead);
    }

    @Operation(summary = "Get current admin info")
    @GetMapping("/me")
    public AdminInfoResponse getAdminInfo(Principal principal) {
        if (principal == null) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        String username = principal.getName();
        AdminUser adminUser = adminService.getAdminByUsername(username);
        List<Role> roleList = adminService.getRoleList(adminUser.getId());
        List<String> roles =
                (roleList != null && !roleList.isEmpty())
                        ? roleList.stream().map(Role::getName).collect(Collectors.toList())
                        : Collections.emptyList();
        return new AdminInfoResponse(
                adminUser.getUsername(),
                roleService.getMenuList(adminUser.getId()),
                adminUser.getIcon(),
                roles);
    }

    @Operation(summary = "Logout admin user")
    @PostMapping("/logout")
    public void logout(Principal principal) {
        if (principal != null) {
            adminService.logout(principal.getName());
        }
    }

    @Operation(summary = "List admin users")
    @GetMapping
    public Page<AdminUser> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 5) Pageable pageable) {
        return adminService.list(
                keyword, pageable.getPageSize(), pageable.getPageNumber() + 1);
    }

    @Operation(summary = "Get admin user by ID")
    @GetMapping("/{id}")
    public AdminUser getItem(@PathVariable("id") Long id) {
        AdminUser admin = adminService.getItem(id);
        if (admin == null) {
            throw new ResourceNotFoundException("AdminUser", id);
        }
        return admin;
    }

    @Operation(summary = "Update admin user")
    @PutMapping("/{id}")
    public Integer update(@PathVariable("id") Long id, @Valid @RequestBody AdminUser admin) {
        int count = adminService.update(id, admin);
        if (count <= 0) {
            Asserts.fail("Failed to update admin user");
        }
        return count;
    }

    @Operation(summary = "Update admin password")
    @PatchMapping("/password")
    public Integer updatePassword(@Valid @RequestBody UpdatePasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return status;
        } else if (status == -1) {
            Asserts.fail("Admin not found");
        } else if (status == -2) {
            Asserts.fail("Incorrect old password");
        } else if (status == -3) {
            Asserts.fail("Invalid password format");
        } else {
            Asserts.fail("Failed to update password");
        }
        return status;
    }

    @Operation(summary = "Delete admin user")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable("id") Long id) {
        int count = adminService.delete(id);
        if (count <= 0) {
            Asserts.fail("Failed to delete admin user");
        }
        return count;
    }

    @Operation(summary = "Update admin status")
    @PatchMapping("/{id}/status")
    public Integer updateStatus(
            @PathVariable("id") Long id, @RequestParam(value = "status") Integer status) {
        AdminUser adminUser = new AdminUser();
        adminUser.setStatus(status != null && status == 1);
        int count = adminService.update(id, adminUser);
        if (count <= 0) {
            Asserts.fail("Failed to update admin status");
        }
        return count;
    }

    @Operation(summary = "Assign roles to admin user")
    @PutMapping("/roles")
    public Integer updateRole(
            @RequestParam("adminId") Long adminId, @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count < 0) {
            Asserts.fail("Failed to update admin roles");
        }
        return count;
    }

    @Operation(summary = "Get roles for admin user")
    @GetMapping("/{adminId}/roles")
    public List<Role> getRoleList(@PathVariable("adminId") Long adminId) {
        return adminService.getRoleList(adminId);
    }
}
