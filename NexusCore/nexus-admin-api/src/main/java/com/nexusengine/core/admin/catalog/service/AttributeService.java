package com.nexusengine.core.admin.catalog.service;

import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Represents the AttributeService component.
 * Provides core functionality and operations for AttributeService.
 */
public interface AttributeService {
        /**
     * Executes the operation.
     * @param cid the cid
     * @param type the type
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<AttributeDefinition> getList(Long cid, Integer type, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param attributeParam the attributeParam
     * @return the result of the operation
     */
    @Transactional
    int create(AttributeParam attributeParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param productAttributeParam the productAttributeParam
     * @return the result of the operation
     */
    int update(Long id, AttributeParam productAttributeParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    AttributeDefinition getItem(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    @Transactional
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param productCategoryId the productCategoryId
     * @return the result of the operation
     */
    List<AttributeInfo> getProductAttrInfo(Long productCategoryId);
}
