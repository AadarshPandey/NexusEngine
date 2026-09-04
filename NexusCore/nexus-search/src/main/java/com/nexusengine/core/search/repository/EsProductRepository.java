package com.nexusengine.core.search.repository;

import com.nexusengine.core.search.domain.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Auto-generated documentation
 * Created by macro on 2018/6/19.
 */
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {
    /**
     * Auto-generated documentation
     *
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    Page<EsProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords,Pageable page);

}
