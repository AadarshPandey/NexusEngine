package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.SmsFlashPromotionSessionDetail;
import com.nexusengine.core.repository.SmsFlashPromotionSessionRepository;
import com.nexusengine.core.model.SmsFlashPromotionSession;
import com.nexusengine.core.service.SmsFlashPromotionProductRelationService;
import com.nexusengine.core.service.SmsFlashPromotionSessionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmsFlashPromotionSessionServiceImpl implements SmsFlashPromotionSessionService {
    @Autowired
    private SmsFlashPromotionSessionRepository promotionSessionRepository;
    @Autowired
    private SmsFlashPromotionProductRelationService relationService;

    @Override
    public int create(SmsFlashPromotionSession promotionSession) {
        promotionSession.setCreateTime(new Date());
        promotionSessionRepository.save(promotionSession);
        return 1;
    }

    @Override
    public int update(Long id, SmsFlashPromotionSession promotionSession) {
        promotionSession.setId(id);
        promotionSessionRepository.save(promotionSession);
        return 1;
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashPromotionSession session = promotionSessionRepository.findById(id).orElse(new SmsFlashPromotionSession());
        session.setId(id);
        session.setStatus(status);
        promotionSessionRepository.save(session);
        return 1;
    }

    @Override
    public int delete(Long id) {
        promotionSessionRepository.deleteById(id);
        return 1;
    }

    @Override
    public SmsFlashPromotionSession getItem(Long id) {
        return promotionSessionRepository.findById(id).orElse(null);
    }

    @Override
    public List<SmsFlashPromotionSession> list() {
        return promotionSessionRepository.findAll();
    }

    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> result = new ArrayList<>();
        List<SmsFlashPromotionSession> list = promotionSessionRepository.findAll();
        for (SmsFlashPromotionSession promotionSession : list) {
            SmsFlashPromotionSessionDetail detail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(promotionSession, detail);
            long count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
