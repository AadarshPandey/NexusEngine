package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.PermissionGroup;

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

import java.util.List;


/**
 * Represents the PermissionGroupService component.
 * Provides core functionality and operations for PermissionGroupService.
 */
public interface PermissionGroupService {

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<PermissionGroup> listAll();

        /**
     * Executes the operation.
     * @param permissionGroup the permissionGroup
     * @return the result of the operation
     */
    int create(PermissionGroup permissionGroup);

        /**
     * Executes the operation.
     * @param id the id
     * @param permissionGroup the permissionGroup
     * @return the result of the operation
     */
    int update(Long id, PermissionGroup permissionGroup);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);
}
