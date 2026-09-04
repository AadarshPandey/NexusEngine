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
import java.util.List;

@Service
public class SmsCouponServiceImpl implements SmsCouponService {
    @Autowired
    private SmsCouponRepository couponRepository;
    @Autowired
    private SmsCouponProductRelationRepository productRelationRepository;
    @Autowired
    private SmsCouponProductCategoryRelationRepository productCategoryRelationRepository;

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
    public List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        return couponRepository.findAll();
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
