package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.SmsFlashPromotionRepository;
import com.nexusengine.core.model.SmsFlashPromotion;
import com.nexusengine.core.service.SmsFlashPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class SmsFlashPromotionServiceImpl implements SmsFlashPromotionService {
    @Autowired
    private SmsFlashPromotionRepository flashPromotionRepository;

    @Override
    public int create(SmsFlashPromotion flashPromotion) {
        flashPromotion.setCreateTime(new Date());
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
        SmsFlashPromotion flashPromotion = flashPromotionRepository.findById(id).orElse(new SmsFlashPromotion());
        flashPromotion.setId(id);
        flashPromotion.setStatus(status);
        flashPromotionRepository.save(flashPromotion);
        return 1;
    }

    @Override
    public SmsFlashPromotion getItem(Long id) {
        return flashPromotionRepository.findById(id).orElse(null);
    }

    @Override
    public List<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum) {
        return flashPromotionRepository.findAll();
    }
}
