package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.common.api.CommonResult;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.promotion.entity.FlashSaleSlot;
import com.nexusengine.core.domain.promotion.repository.FlashSaleSlotRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/flashSession")
@Tag(name = "FlashSaleSlotController")
public class FlashSaleSlotController {
    @Autowired
    private FlashSaleSlotRepository sessionRepository;

    @GetMapping("/list")
    public CommonResult<List<FlashSaleSlot>> list() {
        return CommonResult.success(sessionRepository.findAll());
    }

    @GetMapping("/selectList")
    public CommonResult<List<FlashSaleSlot>> selectList(@RequestParam("flashPromotionId") Long flashPromotionId) {
        return CommonResult.success(sessionRepository.findAll());
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody FlashSaleSlot session) {
        sessionRepository.save(session);
        return CommonResult.success(null);
    }

    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable("id") Long id, @RequestBody FlashSaleSlot session) {
        session.setId(id);
        sessionRepository.save(session);
        return CommonResult.success(null);
    }
    
    @PostMapping("/update/status/{id}")
    public CommonResult updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        FlashSaleSlot session = sessionRepository.findById(id).orElse(null);
        if (session != null) {
            session.setStatus(status);
            sessionRepository.save(session);
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable("id") Long id) {
        sessionRepository.deleteById(id);
        return CommonResult.success(null);
    }
}
