package com.nexusengine.core.portal.catalog.service;

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

import java.util.List;


/**
 * Represents the ProductService component.
 * Provides core functionality and operations for ProductService.
 */
public interface ProductService {
        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param brandId the brandId
     * @param productCategoryId the productCategoryId
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @param sort the sort
     * @return the result of the operation
     */
    List<Product> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<CategoryNode> categoryTreeList();

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ProductDetail detail(Long id);
}
