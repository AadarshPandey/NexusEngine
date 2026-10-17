package com.nexusengine.core.admin.engagement.service;

import com.nexusengine.core.admin.engagement.dto.ReviewModerationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewModerationService {
    Page<ReviewModerationDTO> list(Long productId, Integer status, String keyword, Pageable pageable);
    void updateModerationStatus(Long id, Integer status);
    void delete(Long id);
}
