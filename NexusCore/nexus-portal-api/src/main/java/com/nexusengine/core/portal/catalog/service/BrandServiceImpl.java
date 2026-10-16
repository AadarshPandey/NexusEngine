package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.portal.catalog.dao.HomeDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    public Page<Product> productList(Long brandId, Integer pageNum, Integer pageSize) {
        return productRepository.findByBrandIdAndDeletedAtIsNullAndIsPublished(
                brandId, true, PageRequest.of(pageNum - 1, pageSize));
    }
}
