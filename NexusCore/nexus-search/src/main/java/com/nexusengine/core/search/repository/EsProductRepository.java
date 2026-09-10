package com.nexusengine.core.search.repository;

import com.nexusengine.core.search.domain.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Represents the EsProductRepository component.
 * Provides core functionality and operations for EsProductRepository.
 */
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {
    /**
     *
     */
    Page<EsProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords,Pageable page);

}
