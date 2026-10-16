package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.AdminUserRole;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.domain.iam.repository.AdminUserRoleRepository;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service managing Redis caching for admin user details and permissions.
 */
@Service
@RequiredArgsConstructor
public class AdminUserCacheServiceImpl implements AdminUserCacheService {
    private final AdminUserService adminService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AdminUserRoleRepository adminRoleRelationRepository;

    @Value("${redis.database:nexus}")
    private String redisDatabase;

    @Value("${redis.expire.common:86400}")
    private Long redisExpire;

    @Value("${redis.key.admin:iam:admin:}")
    private String redisKeyAdmin;

    @Value("${redis.key.resourceList:iam:resourceList:}")
    private String redisKeyResourceList;

    @Override
    public void delAdmin(Long adminId) {
        AdminUser admin = adminService.getItem(adminId);
        if (admin != null) {
            String key = redisDatabase + ":" + redisKeyAdmin + ":" + admin.getUsername();
            redisTemplate.delete(key);
        }
    }

    @Override
    public void delResourceList(Long adminId) {
        String key = redisDatabase + ":" + redisKeyResourceList + ":" + adminId;
        redisTemplate.delete(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        List<AdminUserRole> relationList = adminRoleRelationRepository.findByRoleId(roleId);
        if (relationList != null && !relationList.isEmpty()) {
            String keyPrefix = redisDatabase + ":" + redisKeyResourceList + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        List<AdminUserRole> relationList = adminRoleRelationRepository.findByRoleIdIn(roleIds);
        if (relationList != null && !relationList.isEmpty()) {
            String keyPrefix = redisDatabase + ":" + redisKeyResourceList + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminRoleRelationRepository.getAdminIdList(resourceId);
        if (adminIdList != null && !adminIdList.isEmpty()) {
            String keyPrefix = redisDatabase + ":" + redisKeyResourceList + ":";
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId).collect(Collectors.toList());
            redisTemplate.delete(keys);
        }
    }

    @Override
    public AdminUser getAdmin(String username) {
        String key = redisDatabase + ":" + redisKeyAdmin + ":" + username;
        return (AdminUser) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setAdmin(AdminUser admin) {
        String key = redisDatabase + ":" + redisKeyAdmin + ":" + admin.getUsername();
        redisTemplate.opsForValue().set(key, admin, Duration.ofSeconds(redisExpire));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Permission> getResourceList(Long adminId) {
        String key = redisDatabase + ":" + redisKeyResourceList + ":" + adminId;
        return (List<Permission>) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setResourceList(Long adminId, List<Permission> resourceList) {
        String key = redisDatabase + ":" + redisKeyResourceList + ":" + adminId;
        redisTemplate.opsForValue().set(key, resourceList, Duration.ofSeconds(redisExpire));
    }
}
