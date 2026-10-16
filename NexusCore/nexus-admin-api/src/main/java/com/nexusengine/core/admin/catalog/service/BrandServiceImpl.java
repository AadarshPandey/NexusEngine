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

    @Override
    public org.springframework.data.domain.Page<Brand> listAllBrand() {
        return brandRepository.findAll(PageRequest.of(0, 1000));
    }

    @Override
    public int createBrand(BrandParam brandParam) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandParam, brand);
        brandRepository.save(brand);
        return 1;
    }

    @Override
    public int updateBrand(Long id, BrandParam brandParam) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandParam, brand);
        brand.setId(id);
        brandRepository.save(brand);
        return 1;
    }

    @Override
    public int deleteBrand(Long id) {
        brandRepository.deleteById(id);
        return 1;
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        brandRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public org.springframework.data.domain.Page<Brand> listBrand(String keyword, Integer isVisible, int pageNum, int pageSize) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return brandRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (org.springframework.util.StringUtils.hasText(keyword)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
            }
            if (isVisible != null) {
                predicates.add(cb.equal(root.get("isVisible"), isVisible));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    @Override
    public Brand getBrand(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public int updateIsVisible(List<Long> ids, Integer isVisible) {
        List<Brand> brands = brandRepository.findAllById(ids);
        for (Brand brand : brands) {
            brand.setIsVisible(isVisible);
            brandRepository.save(brand);
        }
        return brands.size();
    }
}
