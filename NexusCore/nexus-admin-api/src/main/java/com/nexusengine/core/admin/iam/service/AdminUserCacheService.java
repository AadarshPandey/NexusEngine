package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.AdminUser;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.iam.entity.Permission;

import java.util.List;


/**
 * Auto-generated documentation
 * Created by macro on 2020/3/13.
 */
public interface AdminUserCacheService {
        /**
     * Executes the operation.
     * @param adminId the adminId
     */
    void delAdmin(Long adminId);

        /**
     * Executes the operation.
     * @param adminId the adminId
     */
    void delResourceList(Long adminId);

        /**
     * Executes the operation.
     * @param roleId the roleId
     */
    void delResourceListByRole(Long roleId);

        /**
     * Executes the operation.
     * @param roleIds the roleIds
     */
    void delResourceListByRoleIds(List<Long> roleIds);

        /**
     * Executes the operation.
     * @param resourceId the resourceId
     */
    void delResourceListByResource(Long resourceId);

        /**
     * Executes the operation.
     * @param username the username
     * @return the result of the operation
     */
    AdminUser getAdmin(String username);

        /**
     * Executes the operation.
     * @param admin the admin
     */
    void setAdmin(AdminUser admin);

        /**
     * Executes the operation.
     * @param adminId the adminId
     * @return the result of the operation
     */
    List<Permission> getResourceList(Long adminId);

        /**
     * Executes the operation.
     * @param adminId the adminId
     * @param resourceList the resourceList
     */
    void setResourceList(Long adminId, List<Permission> resourceList);
}
