package com.nexusengine.core.service.impl;

import com.nexusengine.core.model.SmsFlashPromotion;
import com.nexusengine.core.repository.SmsFlashPromotionRepository;
import com.nexusengine.core.service.SmsFlashPromotionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class SmsFlashPromotionServiceImpl implements SmsFlashPromotionService {
    private final SmsFlashPromotionRepository flashPromotionRepository;

    public SmsFlashPromotionServiceImpl(SmsFlashPromotionRepository flashPromotionRepository) {
        this.flashPromotionRepository = flashPromotionRepository;
    }

    @Override
    public int create(SmsFlashPromotion flashPromotion) {
        flashPromotion.setCreateTime(OffsetDateTime.now());
        flashPromotionRepository.save(flashPromotion);
        return 1;
    }

    @Override
    public int update(Long id, SmsFlashPromotion flashPromotion) {
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
        Optional<SmsFlashPromotion> optional = flashPromotionRepository.findById(id);
        if (optional.isPresent()) {
            SmsFlashPromotion flashPromotion = optional.get();
            flashPromotion.setStatus(status);
            flashPromotionRepository.save(flashPromotion);
            return 1;
        }
        return 0;
    }

    @Override
    public SmsFlashPromotion getItem(Long id) {
        return flashPromotionRepository.findById(id).orElse(null);
    }

    @Override
    public Page<SmsFlashPromotion> list(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        if (keyword != null && !keyword.isEmpty()) {
            return flashPromotionRepository.findByTitleContaining(keyword, pageable);
        }
        return flashPromotionRepository.findAll(pageable);
    }
}
