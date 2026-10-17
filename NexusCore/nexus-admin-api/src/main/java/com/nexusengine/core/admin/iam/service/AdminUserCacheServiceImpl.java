package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.AdminUser;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service managing Redis caching for admin user details.
 */
@Service
@RequiredArgsConstructor
public class AdminUserCacheServiceImpl implements AdminUserCacheService {
    private final AdminUserService adminService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.database:nexus}")
    private String redisDatabase;

    @Value("${redis.expire.common:86400}")
    private Long redisExpire;

    @Value("${redis.key.admin:iam:admin:}")
    private String redisKeyAdmin;

    @Override
    public void delAdmin(Long adminId) {
        AdminUser admin = adminService.getItem(adminId);
        if (admin != null) {
            String key = redisDatabase + ":" + redisKeyAdmin + ":" + admin.getUsername();
            redisTemplate.delete(key);
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
}
