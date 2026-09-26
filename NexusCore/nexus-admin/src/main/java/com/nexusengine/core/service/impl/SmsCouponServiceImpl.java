package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.dto.SmsCouponParam;
import com.nexusengine.core.repository.SmsCouponRepository;
import com.nexusengine.core.repository.SmsCouponProductCategoryRelationRepository;
import com.nexusengine.core.repository.SmsCouponProductRelationRepository;
import com.nexusengine.core.model.*;
import com.nexusengine.core.service.SmsCouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class SmsCouponServiceImpl implements SmsCouponService {
    private final SmsCouponRepository couponRepository;
    private final SmsCouponProductRelationRepository productRelationRepository;
    private final SmsCouponProductCategoryRelationRepository productCategoryRelationRepository;

    @Override
    public int create(SmsCouponParam couponParam) {
        SmsCoupon smsCoupon = couponParam.smsCoupon();
        smsCoupon.setCount(smsCoupon.getPublishCount());
        smsCoupon.setUseCount(0);
        smsCoupon.setReceiveCount(0);
        couponRepository.save(smsCoupon);
        if (smsCoupon.getUseType().equals(2)) {
            for (SmsCouponProductRelation rel : couponParam.productRelationList()) {
                rel.setCouponId(smsCoupon.getId());
            }
            productRelationRepository.saveAll(couponParam.productRelationList());
        }
        if (smsCoupon.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelation rel : couponParam.productCategoryRelationList()) {
                rel.setCouponId(smsCoupon.getId());
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
    public int update(Long id, SmsCouponParam couponParam) {
        SmsCoupon smsCoupon = couponParam.smsCoupon();
        smsCoupon.setId(id);
        couponRepository.save(smsCoupon);
        if (smsCoupon.getUseType().equals(2)) {
            for (SmsCouponProductRelation rel : couponParam.productRelationList()) {
                rel.setCouponId(id);
            }
            productRelationRepository.deleteByCouponId(id);
            productRelationRepository.saveAll(couponParam.productRelationList());
        }
        if (smsCoupon.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelation rel : couponParam.productCategoryRelationList()) {
                rel.setCouponId(id);
            }
            productCategoryRelationRepository.deleteByCouponId(id);
            productCategoryRelationRepository.saveAll(couponParam.productCategoryRelationList());
        }
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum - 1, pageSize);
        org.springframework.data.jpa.domain.Specification<SmsCoupon> spec = (root, query, cb) -> {
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
    public SmsCouponParam getItem(Long id) {
        SmsCoupon coupon = couponRepository.findById(id).orElse(null);
        if (coupon == null) return null;
        return new SmsCouponParam(coupon, productRelationRepository.findByCouponId(id), productCategoryRelationRepository.findByCouponId(id));
    }
}
