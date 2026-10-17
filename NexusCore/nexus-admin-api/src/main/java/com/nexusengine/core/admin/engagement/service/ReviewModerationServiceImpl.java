package com.nexusengine.core.admin.engagement.service;

import com.nexusengine.core.admin.engagement.dto.ReviewModerationDTO;
import com.nexusengine.core.admin.security.AdminTenantContext;
import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.repository.MemberRepository;
import com.nexusengine.core.domain.engagement.entity.ProductReview;
import com.nexusengine.core.domain.engagement.entity.ReviewMedia;
import com.nexusengine.core.domain.engagement.repository.ProductReviewRepository;
import com.nexusengine.core.domain.engagement.repository.ReviewMediaRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewModerationServiceImpl implements ReviewModerationService {

    private final ProductReviewRepository reviewRepository;
    private final ReviewMediaRepository reviewMediaRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final BrandRepository brandRepository;
    private final AdminTenantContext adminTenantContext;

    @Override
    public Page<ReviewModerationDTO> list(Long productId, Integer status, String keyword, Pageable pageable) {
        List<Long> allowedProductIds = null;
        if (adminTenantContext.isBrandScoped()) {
            Long brandId = adminTenantContext.currentBrandId().get();
            List<Product> brandProducts = productRepository.findByBrandId(brandId);
            if (brandProducts.isEmpty()) {
                return Page.empty(pageable);
            }
            allowedProductIds = brandProducts.stream().map(Product::getId).toList();
        }

        final List<Long> tenantProductIds = allowedProductIds;

        Specification<ProductReview> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (tenantProductIds != null) {
                predicates.add(root.get("productId").in(tenantProductIds));
            }
            if (productId != null) {
                predicates.add(cb.equal(root.get("productId"), productId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (keyword != null && !keyword.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase().trim() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ProductReview> reviewPage = reviewRepository.findAll(spec, pageable);
        List<ProductReview> reviews = reviewPage.getContent();
        if (reviews.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviewPage.getTotalElements());
        }

        List<Long> reviewIds = reviews.stream().map(ProductReview::getId).toList();
        List<ReviewMedia> mediaList = reviewMediaRepository.findByReviewIdIn(reviewIds);
        Map<Long, List<String>> mediaMap = mediaList.stream()
                .collect(Collectors.groupingBy(
                        ReviewMedia::getReviewId,
                        Collectors.mapping(ReviewMedia::getMediaUrl, Collectors.toList())
                ));

        List<Long> prodIds = reviews.stream().map(ProductReview::getProductId).filter(Objects::nonNull).distinct().toList();
        Map<Long, Product> productMap = productRepository.findAllById(prodIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<Long> brandIds = productMap.values().stream().map(Product::getBrandId).filter(Objects::nonNull).distinct().toList();
        Map<Long, Brand> brandMap = brandRepository.findAllById(brandIds).stream()
                .collect(Collectors.toMap(Brand::getId, b -> b));

        List<Long> memberIds = reviews.stream().map(ProductReview::getMemberId).filter(Objects::nonNull).distinct().toList();
        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, m -> m));

        List<ReviewModerationDTO> dtos = reviews.stream().map(r -> {
            Product prod = productMap.get(r.getProductId());
            Brand brand = prod != null && prod.getBrandId() != null ? brandMap.get(prod.getBrandId()) : null;
            Member member = memberMap.get(r.getMemberId());

            return new ReviewModerationDTO(
                    r.getId(),
                    r.getProductId(),
                    prod != null ? prod.getName() : "Product #" + r.getProductId(),
                    prod != null ? prod.getPrimaryImageUrl() : null,
                    brand != null ? brand.getName() : null,
                    r.getMemberId(),
                    member != null ? member.getUsername() : null,
                    member != null ? (member.getDisplayName() != null ? member.getDisplayName() : member.getUsername()) : "Customer",
                    r.getRating(),
                    r.getContent(),
                    r.getStatus(),
                    r.getLikeCount(),
                    r.getDislikeCount(),
                    mediaMap.getOrDefault(r.getId(), Collections.emptyList()),
                    r.getCreatedTime()
            );
        }).toList();

        return new PageImpl<>(dtos, pageable, reviewPage.getTotalElements());
    }

    @Override
    public void updateModerationStatus(Long id, Integer status) {
        ProductReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductReview", id));
        if (adminTenantContext.isBrandScoped()) {
            Product prod = productRepository.findById(review.getProductId()).orElse(null);
            if (prod != null) {
                adminTenantContext.assertBrandAccess(prod.getBrandId());
            }
        }
        review.setStatus(status);
        reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        ProductReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductReview", id));
        if (adminTenantContext.isBrandScoped()) {
            Product prod = productRepository.findById(review.getProductId()).orElse(null);
            if (prod != null) {
                adminTenantContext.assertBrandAccess(prod.getBrandId());
            }
        }
        List<ReviewMedia> media = reviewMediaRepository.findByReviewId(id);
        if (!media.isEmpty()) {
            reviewMediaRepository.deleteAll(media);
        }
        reviewRepository.delete(review);
    }
}
