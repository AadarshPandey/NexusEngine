package com.nexusengine.core.admin.catalog.service;

import com.nexusengine.core.domain.catalog.entity.ProductSku;

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
 * Represents the ProductSkuService component.
 * Provides core functionality and operations for ProductSkuService.
 */
public interface ProductSkuService {
        /**
     * Executes the operation.
     * @param pid the pid
     * @param keyword the keyword
     * @return the result of the operation
     */
    List<ProductSku> getList(Long pid, String keyword);

        /**
     * Executes the operation.
     * @param pid the pid
     * @param skuStockList the skuStockList
     * @return the result of the operation
     */
    int update(Long pid, List<ProductSku> skuStockList);
}
