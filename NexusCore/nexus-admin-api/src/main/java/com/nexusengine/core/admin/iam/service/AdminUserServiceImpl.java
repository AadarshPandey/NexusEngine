package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.Role;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.domain.iam.entity.AdminLoginEvent;
import com.nexusengine.core.domain.iam.entity.AdminUserRole;

import org.springframework.util.StringUtils;
import com.nexusengine.core.admin.iam.dto.AdminUserDetails;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.util.RequestUtil;
import com.nexusengine.core.domain.iam.repository.AdminLoginEventRepository;
import com.nexusengine.core.domain.iam.repository.AdminUserRepository;
import com.nexusengine.core.domain.iam.repository.AdminUserRoleRepository;


import com.nexusengine.core.security.util.JwtTokenUtil;
import com.nexusengine.core.security.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import com.nexusengine.core.admin.iam.dto.AdminUserParam;
import com.nexusengine.core.admin.iam.dto.UpdatePasswordParam;


/**
 * Admin Service Implementation
 */
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserRepository adminRepository;
    private final AdminUserRoleRepository adminRoleRelationRepository;
    private final AdminLoginEventRepository loginLogRepository;

    @Override
    public AdminUser getAdminByUsername(String username) {
        AdminUser admin = getCacheService().getAdmin(username);
        if (admin != null && admin.getPassword() != null) return admin;
        admin = adminRepository.findByUsername(username);
        if (admin != null) {
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public AdminUser register(AdminUserParam adminUserParam) {
        if (adminUserParam.password() == null || adminUserParam.password().length() < 8) {
            Asserts.fail("Password must be at least 8 characters long");
        }
        if (!adminUserParam.password().matches(".*[A-Z].*") || !adminUserParam.password().matches(".*[a-z].*") || !adminUserParam.password().matches(".*\\\\d.*")) {
            Asserts.fail("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserParam, adminUser);
        adminUser.setCreateTime(java.time.OffsetDateTime.now());
        adminUser.setStatus(1);
        AdminUser existing = adminRepository.findByUsername(adminUser.getUsername());
        if (existing != null) {
            return null;
        }
        String encodePassword = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encodePassword);
        adminRepository.save(adminUser);
        return adminUser;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("Invalid username or password");
            }
            if(!userDetails.isEnabled()){
                throw new BadCredentialsException("Invalid username or password");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("Login exception: {}", e.getMessage());
        }
        return token;
    }

    private void insertLoginLog(String username) {
        AdminUser admin = getAdminByUsername(username);
        if(admin==null) return;
        AdminLoginEvent loginLog = new AdminLoginEvent();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(java.time.OffsetDateTime.now());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(RequestUtil.getRequestIp(request));
        loginLogRepository.save(loginLog);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public AdminUser getItem(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public org.springframework.data.domain.Page<AdminUser> list(String keyword, Integer pageSize, Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id"));
        if (StringUtils.hasText(keyword)) {
            return adminRepository.findAll((Specification<AdminUser>) (root, query, cb) -> {
                String pattern = "%" + keyword + "%";
                return cb.or(
                        cb.like(root.get("username"), pattern),
                        cb.like(root.get("nickName"), pattern)
                );
            }, pageable);
        }
        return adminRepository.findAll(pageable);
    }

    @Override
    public int update(Long id, AdminUser admin) {
        admin.setId(id);
        AdminUser rawAdmin = adminRepository.findById(id).orElse(null);
        if (rawAdmin != null) {
            if(rawAdmin.getPassword().equals(admin.getPassword())){
                admin.setPassword(null);
            }else{
                if(!StringUtils.hasText(admin.getPassword())){
                    admin.setPassword(null);
                }else{
                    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                }
            }
        }
        adminRepository.save(admin);
        getCacheService().delAdmin(id);
        return 1;
    }

    @Override
    public int delete(Long id) {
        adminRepository.deleteById(id);
        getCacheService().delAdmin(id);
        getCacheService().delResourceList(id);
        return 1;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        adminRoleRelationRepository.deleteByAdminId(adminId);
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<AdminUserRole> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                AdminUserRole roleRelation = new AdminUserRole();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationRepository.saveAll(list);
        }
        getCacheService().delResourceList(adminId);
        return count;
    }

    @Override
    public List<Role> getRoleList(Long adminId) {
        return adminRoleRelationRepository.getRoleList(adminId);
    }

    @Override
    public List<Permission> getResourceList(Long adminId) {
        List<Permission> resourceList = getCacheService().getResourceList(adminId);
        if (resourceList != null && !resourceList.isEmpty()) {
            return resourceList;
        }
        resourceList = adminRoleRelationRepository.getResourceList(adminId);
        if (resourceList != null && !resourceList.isEmpty()) {
            getCacheService().setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdatePasswordParam param) {
        if (!StringUtils.hasText(param.username())
                || !StringUtils.hasText(param.oldPassword())
                || !StringUtils.hasText(param.newPassword())) {
            return -1;
        }
        if (param.newPassword().length() < 8) {
            Asserts.fail("Password must be at least 8 characters long");
        }
        if (!param.newPassword().matches(".*[A-Z].*") || !param.newPassword().matches(".*[a-z].*") || !param.newPassword().matches(".*\\\\d.*")) {
            Asserts.fail("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        AdminUser adminUser = adminRepository.findByUsername(param.username());
        if(adminUser == null){
            return -2;
        }
        if(!passwordEncoder.matches(param.oldPassword(),adminUser.getPassword())){
            return -3;
        }
        adminUser.setPassword(passwordEncoder.encode(param.newPassword()));
        adminRepository.save(adminUser);
        getCacheService().delAdmin(adminUser.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        AdminUser admin = getAdminByUsername(username);
        if (admin != null) {
            List<Permission> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("Invalid username or password");
    }

    @Override
    public AdminUserCacheService getCacheService() {
        return SpringUtil.getBean(AdminUserCacheService.class);
    }

    @Override
    public void logout(String username) {
        AdminUser admin = getCacheService().getAdmin(username);
        if (admin != null) {
            getCacheService().delAdmin(admin.getId());
            getCacheService().delResourceList(admin.getId());
        }
    }
}
