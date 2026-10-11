package com.nexusengine.core.portal.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.common.api.CommonPage;

import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import com.nexusengine.core.portal.catalog.dao.HomeDao;


/**
 * Portal brand management Service implementation
 */
@Service("portalBrandServiceImpl")
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final HomeDao homeDao;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Brand> recommendList(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return homeDao.getRecommendBrandList(offset, pageSize);
    }

    @Override
    public Brand detail(Long brandId) {
        return brandRepository.findById(brandId).orElse(null);
    }

    @Override
    public CommonPage<Product> productList(Long brandId, Integer pageNum, Integer pageSize) {
        Page<Product> page = productRepository.findByBrandIdAndDeleteStatusAndPublishStatus(
                brandId, 0, 1, PageRequest.of(pageNum - 1, pageSize));
        return CommonPage.restPage(page);
    }
}
