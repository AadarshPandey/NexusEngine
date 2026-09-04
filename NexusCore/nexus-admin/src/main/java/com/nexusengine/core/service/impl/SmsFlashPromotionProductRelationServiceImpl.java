package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.SmsFlashPromotionProduct;
import com.nexusengine.core.repository.SmsFlashPromotionProductRelationRepository;
import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import com.nexusengine.core.service.SmsFlashPromotionProductRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsFlashPromotionProductRelationServiceImpl implements SmsFlashPromotionProductRelationService {
    @Autowired
    private SmsFlashPromotionProductRelationRepository relationRepository;

    @Override
    public int create(List<SmsFlashPromotionProductRelation> relationList) {
        relationRepository.saveAll(relationList);
        return relationList.size();
    }

    @Override
    public int update(Long id, SmsFlashPromotionProductRelation relation) {
        relation.setId(id);
        relationRepository.save(relation);
        return 1;
    }

    @Override
    public int delete(Long id) {
        relationRepository.deleteById(id);
        return 1;
    }

    @Override
    public SmsFlashPromotionProductRelation getItem(Long id) {
        return relationRepository.findById(id).orElse(null);
    }

    @Override
    public List<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        List<SmsFlashPromotionProductRelation> relations = relationRepository.findByFlashPromotionIdAndFlashPromotionSessionId(flashPromotionId, flashPromotionSessionId);
        List<SmsFlashPromotionProduct> result = new ArrayList<>();
        for (SmsFlashPromotionProductRelation relation : relations) {
            SmsFlashPromotionProduct product = new SmsFlashPromotionProduct();
            product.setId(relation.getId());
            product.setFlashPromotionPrice(relation.getFlashPromotionPrice());
            product.setFlashPromotionCount(relation.getFlashPromotionCount());
            product.setFlashPromotionLimit(relation.getFlashPromotionLimit());
            result.add(product);
        }
        return result;
    }

    @Override
    public long getCount(Long flashPromotionId, Long flashPromotionSessionId) {
        return relationRepository.countByFlashPromotionIdAndFlashPromotionSessionId(flashPromotionId, flashPromotionSessionId);
    }
}
