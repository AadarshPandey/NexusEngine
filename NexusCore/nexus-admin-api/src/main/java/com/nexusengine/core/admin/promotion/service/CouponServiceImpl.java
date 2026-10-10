package com.nexusengine.core.admin.promotion.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.domain.promotion.repository.CouponRepository;
import com.nexusengine.core.domain.promotion.repository.CouponCategoryRepository;
import com.nexusengine.core.domain.promotion.repository.CouponProductRepository;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponProductRepository productRelationRepository;
    private final CouponCategoryRepository productCategoryRelationRepository;

    @Override
    public int create(CouponParam couponParam) {
        Coupon coupon = couponParam.coupon();
                        coupon.setReceiveCount(0);
        couponRepository.save(coupon);
        if (coupon.getUseType().equals(2)) {
            for (CouponProduct rel : couponParam.productRelationList()) {
                rel.setCouponId(coupon.getId());
            }
            productRelationRepository.saveAll(couponParam.productRelationList());
        }
        if (coupon.getUseType().equals(1)) {
            for (CouponCategory rel : couponParam.productCategoryRelationList()) {
                rel.setCouponId(coupon.getId());
            }
            productCategoryRelationRepository.saveAll(couponParam.productCategoryRelationList());
        }
        return 1;
    }

    @Override
    public int delete(Long id) {
        couponRepository.deleteById(id);
        productRelationRepository.deleteByCouponId(id);
        productCategoryRelationRepository.deleteByCouponId(id);
        return 1;
    }

    @Override
    public int update(Long id, CouponParam couponParam) {
        Coupon coupon = couponParam.coupon();
        coupon.setId(id);
        couponRepository.save(coupon);
        if (coupon.getUseType().equals(2)) {
            for (CouponProduct rel : couponParam.productRelationList()) {
                rel.setCouponId(id);
            }
            productRelationRepository.deleteByCouponId(id);
            productRelationRepository.saveAll(couponParam.productRelationList());
        }
        if (coupon.getUseType().equals(1)) {
            for (CouponCategory rel : couponParam.productCategoryRelationList()) {
                rel.setCouponId(id);
            }
            productCategoryRelationRepository.deleteByCouponId(id);
            productCategoryRelationRepository.saveAll(couponParam.productCategoryRelationList());
        }
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<Coupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum - 1, pageSize);
        org.springframework.data.jpa.domain.Specification<Coupon> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (cn.hutool.core.util.StrUtil.isNotEmpty(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return couponRepository.findAll(spec, pageable);
    }

    @Override
    public CouponParam getItem(Long id) {
        Coupon coupon = couponRepository.findById(id).orElse(null);
        if (coupon == null) return null;
        return new CouponParam(coupon, productRelationRepository.findByCouponId(id), productCategoryRelationRepository.findByCouponId(id));
    }
}
