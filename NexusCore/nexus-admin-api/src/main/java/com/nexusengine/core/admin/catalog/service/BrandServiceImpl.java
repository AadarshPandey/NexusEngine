package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;


import com.nexusengine.core.admin.catalog.dto.BrandParam;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.entity.Brand;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;


@Service("adminBrandServiceImpl")
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final com.nexusengine.core.admin.security.AdminTenantContext adminTenantContext;

    @Override
    public org.springframework.data.domain.Page<Brand> listAllBrand() {
        if (adminTenantContext.isBrandScoped()) {
            Long brandId = adminTenantContext.currentBrandId().get();
            return new org.springframework.data.domain.PageImpl<>(brandRepository.findAllById(List.of(brandId)));
        }
        return brandRepository.findAll(PageRequest.of(0, 1000));
    }

    @Override
    public int createBrand(BrandParam brandParam) {
        if (adminTenantContext.isBrandScoped()) {
            throw new com.nexusengine.core.common.exception.ApiException(com.nexusengine.core.common.api.ResultCode.FORBIDDEN);
        }
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandParam, brand);
        brand.setIsVisible(brandParam.isVisible() != null && brandParam.isVisible() == 1);
        brandRepository.save(brand);
        return 1;
    }

    @Override
    public int updateBrand(Long id, BrandParam brandParam) {
        adminTenantContext.assertBrandAccess(id);
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandParam, brand);
        brand.setIsVisible(brandParam.isVisible() != null && brandParam.isVisible() == 1);
        brand.setId(id);
        brandRepository.save(brand);
        return 1;
    }

    @Override
    public int deleteBrand(Long id) {
        adminTenantContext.assertBrandAccess(id);
        brandRepository.deleteById(id);
        return 1;
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        for (Long id : ids) {
            adminTenantContext.assertBrandAccess(id);
        }
        brandRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public org.springframework.data.domain.Page<Brand> listBrand(String keyword, Integer isVisible, int pageNum, int pageSize) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return brandRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (adminTenantContext.isBrandScoped()) {
                predicates.add(cb.equal(root.get("id"), adminTenantContext.currentBrandId().get()));
            }
            if (org.springframework.util.StringUtils.hasText(keyword)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
            }
            if (isVisible != null) {
                predicates.add(cb.equal(root.get("isVisible"), isVisible == 1));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    @Override
    public Brand getBrand(Long id) {
        adminTenantContext.assertBrandAccess(id);
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public int updateIsVisible(List<Long> ids, Integer isVisible) {
        for (Long id : ids) {
            adminTenantContext.assertBrandAccess(id);
        }
        List<Brand> brands = brandRepository.findAllById(ids);
        for (Brand brand : brands) {
            brand.setIsVisible(isVisible != null && isVisible == 1);
            brandRepository.save(brand);
        }
        return brands.size();
    }
}
