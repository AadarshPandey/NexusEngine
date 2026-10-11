package com.nexusengine.core.search.service.impl;

import org.springframework.util.StringUtils;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.nexusengine.core.search.dao.ProductSearchDao;
import com.nexusengine.core.search.domain.ProductDocument;
import com.nexusengine.core.search.domain.ProductRelatedInfo;
import com.nexusengine.core.search.repository.ProductSearchRepository;
import com.nexusengine.core.search.service.ProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Elasticsearch product search Service implementation
 */
@Service
@lombok.RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchServiceImpl.class);
    private final ProductSearchRepository productRepository;
    private final ProductSearchDao esProductDao;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public int importAll() {
        List<ProductDocument> esProductList = esProductDao.getAllEsProductList(null);
        Iterable<ProductDocument> esProductIterable = productRepository.saveAll(esProductList);
        Iterator<ProductDocument> iterator = esProductIterable.iterator();
        int result = 0;
        while (iterator.hasNext()) {
            result++;
            iterator.next();
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDocument create(Long id) {
        ProductDocument result = null;
        List<ProductDocument> esProductList = esProductDao.getAllEsProductList(id);
        if (esProductList.size() > 0) {
            ProductDocument esProduct = esProductList.get(0);
            result = productRepository.save(esProduct);
        }
        return result;
    }

    @Override
    public void delete(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<ProductDocument> esProductList = new ArrayList<>();
            for (Long id : ids) {
                ProductDocument esProduct = new ProductDocument();
                esProduct.setId(id);
                esProductList.add(esProduct);
            }
            productRepository.deleteAll(esProductList);
        }
    }

    @Override
    public Page<ProductDocument> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        nativeQueryBuilder.withPageable(pageable);
        if (!StringUtils.hasText(keyword)) {
            nativeQueryBuilder.withQuery(QueryBuilders.matchAll(builder -> builder));
        } else {
            nativeQueryBuilder.withQuery(QueryBuilders.multiMatch(builder -> builder
                    .query(keyword)
                    .fields("name^10", "subTitle^5", "keywords^2", "brandName^5", "productSn^5")
            ));
        }
        NativeQuery nativeQuery = nativeQueryBuilder.build();
        SearchHits<ProductDocument> searchHits = elasticsearchTemplate.search(nativeQuery, ProductDocument.class);
        if (searchHits.getTotalHits() <= 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<ProductDocument> searchProductList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageImpl<>(searchProductList, pageable, searchHits.getTotalHits());
    }

    @Override
    public Page<ProductDocument> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        nativeQueryBuilder.withPageable(pageable);
        if (brandId != null || productCategoryId != null) {
            Query boolQuery = QueryBuilders.bool(builder -> {
                if (brandId != null) {
                    builder.must(QueryBuilders.term(b -> b.field("brandId").value(brandId)));
                }
                if (productCategoryId != null) {
                    builder.must(QueryBuilders.term(b -> b.field("productCategoryId").value(productCategoryId)));
                }
                return builder;
            });
            nativeQueryBuilder.withFilter(boolQuery);
        }
        if (!StringUtils.hasText(keyword)) {
            nativeQueryBuilder.withQuery(QueryBuilders.matchAll(builder -> builder));
        } else {
            List<FunctionScore> functionScoreList = new ArrayList<>();
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("name").query(keyword)))
                    .weight(10.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("subTitle").query(keyword)))
                    .weight(5.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("keywords").query(keyword)))
                    .weight(2.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("brandName").query(keyword)))
                    .weight(5.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("productSn").query(keyword)))
                    .weight(5.0).build());
            FunctionScoreQuery.Builder functionScoreQueryBuilder = QueryBuilders.functionScore()
                    .functions(functionScoreList).scoreMode(FunctionScoreMode.Sum).minScore(2.0);
            nativeQueryBuilder.withQuery(builder -> builder.functionScore(functionScoreQueryBuilder.build()));
        }
        if (sort == 1) {
            nativeQueryBuilder.withSort(Sort.by(Sort.Order.desc("id")));
        } else if (sort == 2) {
            nativeQueryBuilder.withSort(Sort.by(Sort.Order.desc("sale")));
        } else if (sort == 3) {
            nativeQueryBuilder.withSort(Sort.by(Sort.Order.asc("price")));
        } else if (sort == 4) {
            nativeQueryBuilder.withSort(Sort.by(Sort.Order.desc("price")));
        }
        nativeQueryBuilder.withSort(Sort.by(Sort.Order.desc("_score")));
        NativeQuery nativeQuery = nativeQueryBuilder.build();
        LOGGER.info("DSL:{}", nativeQuery.getQuery().toString());
        SearchHits<ProductDocument> searchHits = elasticsearchTemplate.search(nativeQuery, ProductDocument.class);
        if (searchHits.getTotalHits() <= 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<ProductDocument> searchProductList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageImpl<>(searchProductList, pageable, searchHits.getTotalHits());
    }

    @Override
    public Page<ProductDocument> recommend(Long id, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<ProductDocument> esProductList = esProductDao.getAllEsProductList(id);
        if (esProductList.size() > 0) {
            ProductDocument esProduct = esProductList.get(0);
            String keyword = esProduct.getName();
            Long brandId = esProduct.getBrandId();
            Long productCategoryId = esProduct.getProductCategoryId();
            NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
            nativeQueryBuilder.withPageable(pageable);
            nativeQueryBuilder.withFilter(QueryBuilders.bool(build -> build.mustNot(QueryBuilders.term(b -> b.field("id").value(id)))));
            List<FunctionScore> functionScoreList = new ArrayList<>();
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("name").query(keyword)))
                    .weight(8.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("subTitle").query(keyword)))
                    .weight(2.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("keywords").query(keyword)))
                    .weight(2.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("brandId").query(brandId)))
                    .weight(5.0).build());
            functionScoreList.add(new FunctionScore.Builder()
                    .filter(QueryBuilders.match(builder -> builder.field("productCategoryId").query(productCategoryId)))
                    .weight(3.0).build());
            FunctionScoreQuery.Builder functionScoreQueryBuilder = QueryBuilders.functionScore()
                    .functions(functionScoreList).scoreMode(FunctionScoreMode.Sum).minScore(2.0);
            nativeQueryBuilder.withQuery(builder -> builder.functionScore(functionScoreQueryBuilder.build()));
            NativeQuery nativeQuery = nativeQueryBuilder.build();
            LOGGER.info("DSL:{}", nativeQuery.getQuery().toString());
            SearchHits<ProductDocument> searchHits = elasticsearchTemplate.search(nativeQuery, ProductDocument.class);
            if (searchHits.getTotalHits() <= 0) {
                return new PageImpl<>(Collections.emptyList(), pageable, 0);
            }
            List<ProductDocument> searchProductList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
            return new PageImpl<>(searchProductList, pageable, searchHits.getTotalHits());
        }
        return new PageImpl<>(Collections.emptyList());
    }

    @Override
    public ProductRelatedInfo searchRelatedInfo(String keyword) {
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        if (!StringUtils.hasText(keyword)) {
            nativeQueryBuilder.withQuery(QueryBuilders.matchAll(builder -> builder));
        } else {
            nativeQueryBuilder.withQuery(QueryBuilders.multiMatch(builder -> builder.fields("name", "subTitle", "keywords").query(keyword)));
        }
        nativeQueryBuilder.withAggregation("brandNames", AggregationBuilders.terms(builder -> builder.field("brandName").size(10)));
        nativeQueryBuilder.withAggregation("productCategoryNames", AggregationBuilders.terms(builder -> builder.field("productCategoryName").size(10)));
        Aggregation aggregation = new Aggregation.Builder().nested(builder -> builder.path("attrValueList"))
                .aggregations("productAttrs", new Aggregation.Builder()
                        .filter(b -> b.term(a -> a.field("attrValueList.type").value("1")))
                        .aggregations("attrIds", new Aggregation.Builder().terms(b -> b.field("attrValueList.productAttributeId").size(10))
                                .aggregations("attrValues", new Aggregation.Builder().terms(b -> b.field("attrValueList.value").size(10)).build())
                                .aggregations("attrNames", new Aggregation.Builder().terms(b -> b.field("attrValueList.name").size(10)).build())
                                .build()).build()).build();
        nativeQueryBuilder.withAggregation("allAttrValues", aggregation);
        NativeQuery nativeQuery = nativeQueryBuilder.build();
        LOGGER.info("DSL:{}", nativeQueryBuilder.getQuery().toString());
        SearchHits<ProductDocument> searchHits = elasticsearchTemplate.search(nativeQuery, ProductDocument.class);
        return convertProductRelatedInfo(searchHits);
    }

    private ProductRelatedInfo convertProductRelatedInfo(SearchHits<ProductDocument> response) {
        ProductRelatedInfo productRelatedInfo = new ProductRelatedInfo();
        Map<String, ElasticsearchAggregation> esAggregationMap = ((ElasticsearchAggregations) response.getAggregations()).aggregationsAsMap();
        ElasticsearchAggregation brandNames = esAggregationMap.get("brandNames");
        List<String> brandNameList = new ArrayList<>();
        List<StringTermsBucket> brandNameBuckets = ((StringTermsAggregate) brandNames.aggregation().getAggregate()._get()).buckets().array();
        for (StringTermsBucket bucket : brandNameBuckets) {
            brandNameList.add(bucket.key().stringValue());
        }
        productRelatedInfo.setBrandNames(brandNameList);
        ElasticsearchAggregation productCategoryNames = esAggregationMap.get("productCategoryNames");
        List<String> productCategoryNameList = new ArrayList<>();
        List<StringTermsBucket> productCategoryNameBuckets = ((StringTermsAggregate) productCategoryNames.aggregation().getAggregate()._get()).buckets().array();
        for (StringTermsBucket bucket : productCategoryNameBuckets) {
            productCategoryNameList.add(bucket.key().stringValue());
        }
        productRelatedInfo.setProductCategoryNames(productCategoryNameList);
        ElasticsearchAggregation productAttrs = esAggregationMap.get("allAttrValues");
        List<LongTermsBucket> attrIdBuckets = ((LongTermsAggregate) ((FilterAggregate) ((NestedAggregate) productAttrs.aggregation().getAggregate()._get()).aggregations().get("productAttrs")._get()).aggregations().get("attrIds")._get()).buckets().array();
        List<ProductRelatedInfo.ProductAttr> attrList = new ArrayList<>();
        for (LongTermsBucket item : attrIdBuckets) {
            ProductRelatedInfo.ProductAttr attr = new ProductRelatedInfo.ProductAttr();
            attr.setAttrId(item.key());
            List<String> attrValueList = new ArrayList<>();
            List<StringTermsBucket> attrValues = ((StringTermsAggregate) item.aggregations().get("attrValues")._get()).buckets().array();
            List<StringTermsBucket> attrNames = ((StringTermsAggregate) item.aggregations().get("attrNames")._get()).buckets().array();
            for (StringTermsBucket attrValue : attrValues) {
                attrValueList.add(attrValue.key().stringValue());
            }
            attr.setAttrValues(attrValueList);
            if (!CollectionUtils.isEmpty(attrNames)) {
                attr.setAttrName(attrNames.get(0).key().stringValue());
            }
            attrList.add(attr);
        }
        productRelatedInfo.setProductAttrs(attrList);
        return productRelatedInfo;
    }
}
