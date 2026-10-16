package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BrandService {
    List<Brand> recommendList(Integer pageNum, Integer pageSize);

    Brand detail(Long brandId);

    Page<Product> productList(Long brandId, Integer pageNum, Integer pageSize);
}
