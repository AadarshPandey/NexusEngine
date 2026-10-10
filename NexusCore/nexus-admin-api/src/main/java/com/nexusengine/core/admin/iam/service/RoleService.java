package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;

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
import com.nexusengine.core.domain.iam.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Auto-generated documentation
 * Created by macro on 2018/9/30.
 */
public interface RoleService {
        /**
     * Executes the operation.
     * @param role the role
     * @return the result of the operation
     */
    int create(Role role);

        /**
     * Executes the operation.
     * @param id the id
     * @param role the role
     * @return the result of the operation
     */
    int update(Long id, Role role);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<Role> list();

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    Page<Role> list(String keyword, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param adminId the adminId
     * @return the result of the operation
     */
    List<ConsoleMenu> getMenuList(Long adminId);

        /**
     * Executes the operation.
     * @param roleId the roleId
     * @return the result of the operation
     */
    List<ConsoleMenu> listMenu(Long roleId);

        /**
     * Executes the operation.
     * @param roleId the roleId
     * @return the result of the operation
     */
    List<Permission> listResource(Long roleId);

        /**
     * Executes the operation.
     * @param roleId the roleId
     * @param menuIds the menuIds
     * @return the result of the operation
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

        /**
     * Executes the operation.
     * @param roleId the roleId
     * @param resourceIds the resourceIds
     * @return the result of the operation
     */
    @Transactional
    int allocResource(Long roleId, List<Long> resourceIds);
}
