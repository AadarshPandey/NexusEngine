package com.nexusengine.core.admin.promotion.service;

import com.nexusengine.core.domain.promotion.entity.FlashSale;


import org.springframework.data.domain.Page;


public interface FlashSaleService {
    int create(FlashSale flashPromotion);
    int update(Long id, FlashSale flashPromotion);
    int delete(Long id);
    int updateStatus(Long id, Integer status);
    FlashSale getItem(Long id);
    Page<FlashSale> list(String keyword, Integer pageNum, Integer pageSize);
}
