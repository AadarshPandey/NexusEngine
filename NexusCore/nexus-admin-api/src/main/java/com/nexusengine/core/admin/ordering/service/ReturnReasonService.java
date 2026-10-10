package com.nexusengine.core.admin.ordering.service;

import com.nexusengine.core.domain.ordering.entity.ReturnReason;

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
 * Represents the ReturnReasonService component.
 * Provides core functionality and operations for ReturnReasonService.
 */
public interface ReturnReasonService {
        /**
     * Executes the operation.
     * @param returnReason the returnReason
     */
    int create(ReturnReason returnReason);

        /**
     * Executes the operation.
     * @param id the id
     * @param returnReason the returnReason
     */
    int update(Long id, ReturnReason returnReason);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<ReturnReason> list(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param status the status
     * @return the result of the operation
     */
    int updateStatus(List<Long> ids, Integer status);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ReturnReason getItem(Long id);
}
