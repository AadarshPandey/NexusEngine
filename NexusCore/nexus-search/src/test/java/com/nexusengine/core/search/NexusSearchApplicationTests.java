package com.nexusengine.core.search;

import com.nexusengine.core.search.dao.ProductSearchDao;
import com.nexusengine.core.search.domain.ProductDocument;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class NexusSearchApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(NexusSearchApplicationTests.class);

    private final ProductSearchDao productDao;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    NexusSearchApplicationTests(ProductSearchDao productDao, ElasticsearchTemplate elasticsearchTemplate) {
        this.productDao = productDao;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Test
    public void contextLoads() {
    }
    @Test
    public void testGetAllProductDocumentList(){
        List<ProductDocument> esProductList = productDao.getAllEsProductList(null);
        log.info("ES product list: {}", esProductList);
    }
    @Test
    public void testProductDocumentMapping(){
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(ProductDocument.class);
        indexOperations.putMapping(indexOperations.createMapping(ProductDocument.class));
        Map mapping = indexOperations.getMapping();
        log.info("ES product mapping: {}", mapping);
    }

}
