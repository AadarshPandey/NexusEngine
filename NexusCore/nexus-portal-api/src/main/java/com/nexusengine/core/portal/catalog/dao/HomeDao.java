package com.nexusengine.core.portal.catalog.dao;

import com.nexusengine.core.domain.catalog.entity.Brand;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Home page data access - replaces legacy MyBatis HomeDao
 */
@Repository
public class HomeDao {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public HomeDao(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    public List<Brand> getRecommendBrandList(int offset, int limit) {
        // Return first N brands sorted by id since sort column was removed
        return brandRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    public List<Product> getNewProductList(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    public List<Product> getHotProductList(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset / Math.max(limit, 1), limit, Sort.by(Sort.Direction.DESC, "unitsSold"))).getContent();
    }
}
