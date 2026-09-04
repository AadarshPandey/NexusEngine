package com.nexusengine.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.bo.AdminUserDetails;
import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.common.util.RequestUtil;
import com.nexusengine.core.dto.UmsAdminParam;
import com.nexusengine.core.dto.UpdateAdminPasswordParam;
import com.nexusengine.core.repository.UmsAdminLoginLogRepository;
import com.nexusengine.core.repository.UmsAdminRepository;
import com.nexusengine.core.repository.UmsAdminRoleRelationRepository;
import com.nexusengine.core.model.*;
import com.nexusengine.core.security.util.JwtTokenUtil;
import com.nexusengine.core.security.util.SpringUtil;
import com.nexusengine.core.service.UmsAdminCacheService;
import com.nexusengine.core.service.UmsAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Admin Service Implementation
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsAdminRepository adminRepository;
    @Autowired
    private UmsAdminRoleRelationRepository adminRoleRelationRepository;
    @Autowired
    private UmsAdminLoginLogRepository loginLogRepository;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdmin admin = getCacheService().getAdmin(username);
        if (admin != null) return admin;
        admin = adminRepository.findByUsername(username);
        if (admin != null) {
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        UmsAdmin existing = adminRepository.findByUsername(umsAdmin.getUsername());
        if (existing != null) {
            return null;
        }
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminRepository.save(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("Success");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("Success");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("Success", e.getMessage());
        }
        return token;
    }

    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if(admin==null) return;
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
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
    public UmsAdmin getItem(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        return adminRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = adminRepository.findById(id).orElse(null);
        if (rawAdmin != null) {
            if(rawAdmin.getPassword().equals(admin.getPassword())){
                admin.setPassword(null);
            }else{
                if(StrUtil.isEmpty(admin.getPassword())){
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
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
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
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationRepository.getRoleList(adminId);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        List<UmsResource> resourceList = getCacheService().getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            return resourceList;
        }
        resourceList = adminRoleRelationRepository.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            getCacheService().setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        UmsAdmin umsAdmin = adminRepository.findByUsername(param.getUsername());
        if(umsAdmin == null){
            return -2;
        }
        if(!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        adminRepository.save(umsAdmin);
        getCacheService().delAdmin(umsAdmin.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("Success");
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }

    @Override
    public void logout(String username) {
        UmsAdmin admin = getCacheService().getAdmin(username);
        if (admin != null) {
            getCacheService().delAdmin(admin.getId());
            getCacheService().delResourceList(admin.getId());
        }
    }
}
