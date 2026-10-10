package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.common.api.CommonPage;
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
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;

import java.util.List;


/**
 * Represents the BrandService component.
 * Provides core functionality and operations for BrandService.
 */
public interface BrandService {
        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<Brand> recommendList(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @return the result of the operation
     */
    Brand detail(Long brandId);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    CommonPage<Product> productList(Long brandId, Integer pageNum, Integer pageSize);
}
