package com.nexusengine.core.controller;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.SmsFlashPromotionSession;
import com.nexusengine.core.repository.SmsFlashPromotionSessionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flashSession")
@Tag(name = "SmsFlashPromotionSessionController")
public class SmsFlashPromotionSessionController {
    @Autowired
    private SmsFlashPromotionSessionRepository sessionRepository;

    @GetMapping("/list")
    public CommonResult<List<SmsFlashPromotionSession>> list() {
        return CommonResult.success(sessionRepository.findAll());
    }

    @GetMapping("/selectList")
    public CommonResult<List<SmsFlashPromotionSession>> selectList(@RequestParam Long flashPromotionId) {
        return CommonResult.success(sessionRepository.findAll());
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody SmsFlashPromotionSession session) {
        sessionRepository.save(session);
        return CommonResult.success(null);
    }

    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsFlashPromotionSession session) {
        session.setId(id);
        sessionRepository.save(session);
        return CommonResult.success(null);
    }
    
    @PostMapping("/update/status/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        SmsFlashPromotionSession session = sessionRepository.findById(id).orElse(null);
        if (session != null) {
            session.setStatus(status);
            sessionRepository.save(session);
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        sessionRepository.deleteById(id);
        return CommonResult.success(null);
    }
}
