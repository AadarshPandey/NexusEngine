package com.nexusengine.core.admin.promotion.service;

import com.nexusengine.core.domain.promotion.entity.FlashSale;


import com.nexusengine.core.domain.promotion.repository.FlashSaleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;


@Service
public class FlashSaleServiceImpl implements FlashSaleService {
    private final FlashSaleRepository flashPromotionRepository;

    public FlashSaleServiceImpl(FlashSaleRepository flashPromotionRepository) {
        this.flashPromotionRepository = flashPromotionRepository;
    }

    @Override
    public int create(FlashSale flashPromotion) {
        flashPromotion.setCreateTime(OffsetDateTime.now());
        flashPromotionRepository.save(flashPromotion);
        return 1;
    }

    @Override
    public int update(Long id, FlashSale flashPromotion) {
        flashPromotion.setId(id);
        flashPromotionRepository.save(flashPromotion);
        return 1;
    }

    @Override
    public int delete(Long id) {
        flashPromotionRepository.deleteById(id);
        return 1;
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        Optional<FlashSale> optional = flashPromotionRepository.findById(id);
        if (optional.isPresent()) {
            FlashSale flashPromotion = optional.get();
            flashPromotion.setStatus(status != null && status == 1);
            flashPromotionRepository.save(flashPromotion);
            return 1;
        }
        return 0;
    }

    @Override
    public FlashSale getItem(Long id) {
        return flashPromotionRepository.findById(id).orElse(null);
    }

    @Override
    public Page<FlashSale> list(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        if (keyword != null && !keyword.isEmpty()) {
            return flashPromotionRepository.findByTitleContaining(keyword, pageable);
        }
        return flashPromotionRepository.findAll(pageable);
    }
}
