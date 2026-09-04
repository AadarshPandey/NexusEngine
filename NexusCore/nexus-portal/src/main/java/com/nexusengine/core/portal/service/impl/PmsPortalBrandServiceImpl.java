package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.dao.HomeDao;
import com.nexusengine.core.portal.service.PmsPortalBrandService;
import com.nexusengine.core.repository.PmsBrandRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Portal brand management Service implementation
 */
@Service
public class PmsPortalBrandServiceImpl implements PmsPortalBrandService {
    @Autowired
    private HomeDao homeDao;
    @Autowired
    private PmsBrandRepository brandRepository;
    @Autowired
    private PmsProductRepository productRepository;

    @Override
    public List<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return homeDao.getRecommendBrandList(offset, pageSize);
    }

    @Override
    public PmsBrand detail(Long brandId) {
        return brandRepository.findById(brandId).orElse(null);
    }

    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        Page<PmsProduct> page = productRepository.findByBrandIdAndDeleteStatusAndPublishStatus(
                brandId, 0, 1, PageRequest.of(pageNum - 1, pageSize));
        return CommonPage.restPage(page);
    }
}
