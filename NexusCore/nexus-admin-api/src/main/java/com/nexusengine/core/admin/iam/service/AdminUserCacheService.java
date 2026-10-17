package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.AdminUser;

public interface AdminUserCacheService {
    void delAdmin(Long adminId);

    AdminUser getAdmin(String username);

    void setAdmin(AdminUser admin);
}
