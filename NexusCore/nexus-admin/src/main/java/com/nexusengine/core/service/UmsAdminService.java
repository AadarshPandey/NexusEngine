package com.nexusengine.core.service;

import com.nexusengine.core.dto.UmsAdminParam;
import com.nexusengine.core.dto.UpdateAdminPasswordParam;
import com.nexusengine.core.model.UmsAdmin;
import com.nexusengine.core.model.UmsResource;
import com.nexusengine.core.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface UmsAdminService {
    /**
     * Auto-generated documentation
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * Auto-generated documentation
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    String login(String username,String password);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    String refreshToken(String oldToken);

    /**
     * Auto-generated documentation
     */
    UmsAdmin getItem(Long id);

    /**
     * Auto-generated documentation
     */
    List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    int update(Long id, UmsAdmin admin);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * Auto-generated documentation
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * Auto-generated documentation
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * Auto-generated documentation
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * Auto-generated documentation
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Auto-generated documentation
     */
    UmsAdminCacheService getCacheService();

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    void logout(String username);
}
