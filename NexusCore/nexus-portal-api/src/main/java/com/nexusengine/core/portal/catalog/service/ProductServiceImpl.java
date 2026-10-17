package com.nexusengine.core.portal.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.repository.ProductAttributeValueRepository;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.CategoryRepository;
import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.repository.AttributeDefinitionRepository;

import org.springframework.util.StringUtils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import com.nexusengine.core.portal.catalog.dto.CategoryNode;
import com.nexusengine.core.portal.catalog.dao.ProductDao;
import com.nexusengine.core.portal.catalog.dto.ProductDetail;

/**
 * Portal product management Service implementation
 */
@Service("portalProductServiceImpl")
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final AttributeDefinitionRepository productAttributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductSkuRepository skuStockRepository;
    private final ProductDao productDao;

    private void initializeMediaList(List<Product> products) {
        if (products != null) {
            products.forEach(p -> {
                if (p.getGalleryList() != null) {
                    org.hibernate.Hibernate.initialize(p.getGalleryList());
                }
            });
        }
    }

    @Override
    public List<Product> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        Sort sortOrder;
        switch (sort) {
            case 1: sortOrder = Sort.by(Sort.Direction.DESC, "id"); break;
            case 2: sortOrder = Sort.by(Sort.Direction.DESC, "unitsSold"); break;
            case 3: sortOrder = Sort.by(Sort.Direction.ASC, "listPrice"); break;
            case 4: sortOrder = Sort.by(Sort.Direction.DESC, "listPrice"); break;
            default: sortOrder = Sort.by(Sort.Direction.DESC, "id"); break;
        }

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt")));
            predicates.add(cb.equal(root.get("isPublished"), true));

            if (StringUtils.hasText(keyword)) {
                String kw = "%" + keyword.toLowerCase() + "%";
                jakarta.persistence.criteria.Join<Object, Object> categoryJoin = root.join("productCategory", jakarta.persistence.criteria.JoinType.LEFT);
                jakarta.persistence.criteria.Join<Object, Object> brandJoin = root.join("brand", jakarta.persistence.criteria.JoinType.LEFT);

                Predicate namePredicate = cb.like(cb.lower(root.get("name")), kw);
                Predicate keywordsPredicate = cb.like(cb.lower(root.get("keywords")), kw);
                Predicate categoryPredicate = cb.like(cb.lower(categoryJoin.get("name")), kw);
                Predicate brandPredicate = cb.like(cb.lower(brandJoin.get("name")), kw);

                predicates.add(cb.or(namePredicate, keywordsPredicate, categoryPredicate, brandPredicate));
            }

            if (brandId != null) {
                predicates.add(cb.equal(root.get("brandId"), brandId));
            }
            if (productCategoryId != null) {
                predicates.add(cb.equal(root.get("productCategoryId"), productCategoryId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Product> list = productRepository.findAll(spec, PageRequest.of(pageNum - 1, pageSize, sortOrder)).getContent();
        initializeMediaList(list);
        return list;
    }

    @Override
    public List<CategoryNode> categoryTreeList() {
        List<Category> allList = productCategoryRepository.findAll(PageRequest.of(0, 1000)).getContent();
        return allList.stream()
                .filter(item -> item.getParentId() == null || item.getParentId().equals(0L))
                .map(item -> covert(item, allList))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDetail detail(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null && product.getGalleryList() != null) {
            org.hibernate.Hibernate.initialize(product.getGalleryList());
        }
        if (product == null) return new ProductDetail(null, null, null, null, null, null);

        Brand brand = null;
        if (product.getBrandId() != null) {
            brand = brandRepository.findById(product.getBrandId()).orElse(null);
        }

        List<AttributeDefinition> productAttributeList = productAttributeRepository
                .findByProductAttributeCategoryId(product.getProductAttributeCategoryId());

        List<ProductAttributeValue> valueList = null;
        if (productAttributeList != null && !productAttributeList.isEmpty()) {
            List<Long> attributeIds = productAttributeList.stream().map(AttributeDefinition::getId).collect(Collectors.toList());
            valueList = productAttributeValueRepository.findByProductIdAndProductAttributeIdIn(product.getId(), attributeIds);
        }

        List<ProductSku> skuStockList = skuStockRepository.findByProductId(product.getId());

        List<Coupon> couponList = productDao.getAvailableCouponList(product.getId(), product.getProductCategoryId());

        return new ProductDetail(product, brand, productAttributeList, valueList, skuStockList, couponList);
    }

    private CategoryNode covert(Category item, List<Category> allList) {
        List<CategoryNode> children = allList.stream()
                .filter(subItem -> subItem.getParentId() != null && subItem.getParentId().equals(item.getId()))
                .map(subItem -> covert(subItem, allList)).collect(Collectors.toList());
        return new CategoryNode(item, children);
    }
}
