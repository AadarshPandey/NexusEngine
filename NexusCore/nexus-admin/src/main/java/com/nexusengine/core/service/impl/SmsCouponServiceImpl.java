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
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        couponRepository.save(couponParam);
        if (couponParam.getUseType().equals(2)) {
            for (SmsCouponProductRelation rel : couponParam.getProductRelationList()) {
                rel.setCouponId(couponParam.getId());
            }
            productRelationRepository.saveAll(couponParam.getProductRelationList());
        }
        if (couponParam.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelation rel : couponParam.getProductCategoryRelationList()) {
                rel.setCouponId(couponParam.getId());
            }
            productCategoryRelationRepository.saveAll(couponParam.getProductCategoryRelationList());
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
        couponParam.setId(id);
        couponRepository.save(couponParam);
        if (couponParam.getUseType().equals(2)) {
            for (SmsCouponProductRelation rel : couponParam.getProductRelationList()) {
                rel.setCouponId(id);
            }
            productRelationRepository.deleteByCouponId(id);
            productRelationRepository.saveAll(couponParam.getProductRelationList());
        }
        if (couponParam.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelation rel : couponParam.getProductCategoryRelationList()) {
                rel.setCouponId(id);
            }
            productCategoryRelationRepository.deleteByCouponId(id);
            productCategoryRelationRepository.saveAll(couponParam.getProductCategoryRelationList());
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
        SmsCouponParam param = new SmsCouponParam();
        BeanUtils.copyProperties(coupon, param);
        param.setProductRelationList(productRelationRepository.findByCouponId(id));
        param.setProductCategoryRelationList(productCategoryRelationRepository.findByCouponId(id));
        return param;
    }
}
