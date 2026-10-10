package com.nexusengine.core.portal.home.service;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

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
import com.nexusengine.core.domain.catalog.entity.Category;

import java.util.List;


/**
 * Represents the HomeService component.
 * Provides core functionality and operations for HomeService.
 */
public interface HomeService {

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    HomeContentResult content();

        /**
     * Executes the operation.
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<Product> recommendProductList(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param parentId the parentId
     * @return the result of the operation
     */
    List<Category> getProductCateList(Long parentId);

        /**
     * Executes the operation.
     * @param cateId the cateId
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<Product> hotProductList(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<Product> newProductList(Integer pageNum, Integer pageSize);
}
