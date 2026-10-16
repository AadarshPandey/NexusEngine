package com.nexusengine.core.admin.promotion.controller;

import com.nexusengine.core.common.exception.ResourceNotFoundException;
import com.nexusengine.core.domain.promotion.entity.FlashSaleSlot;
import com.nexusengine.core.domain.promotion.repository.FlashSaleSlotRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flashSession")
@Tag(name = "Promotion - Flash Sale Slots", description = "Admin flash sale slot management APIs")
@RequiredArgsConstructor
public class FlashSaleSlotController {
    private final FlashSaleSlotRepository sessionRepository;

    @Operation(summary = "List all flash sale slots")
    @GetMapping("/list")
    public List<FlashSaleSlot> list() {
        return sessionRepository.findAll();
    }

    @Operation(summary = "List selectable flash sale slots")
    @GetMapping("/selectList")
    public List<FlashSaleSlot> selectList(
            @RequestParam("flashPromotionId") Long flashPromotionId) {
        return sessionRepository.findAll();
    }

    @Operation(summary = "Create flash sale slot")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public FlashSaleSlot create(@Valid @RequestBody FlashSaleSlot session) {
        return sessionRepository.save(session);
    }

    @Operation(summary = "Update flash sale slot")
    @PostMapping("/update/{id}")
    public FlashSaleSlot update(
            @PathVariable("id") Long id, @Valid @RequestBody FlashSaleSlot session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Operation(summary = "Update flash sale slot status")
    @PostMapping("/update/status/{id}")
    public FlashSaleSlot updateStatus(
            @PathVariable("id") Long id, @RequestParam("status") Integer status) {
        FlashSaleSlot session =
                sessionRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("FlashSaleSlot", id));
        session.setStatus(status != null && status == 1);
        return sessionRepository.save(session);
    }

    @Operation(summary = "Delete flash sale slot")
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        sessionRepository.deleteById(id);
    }
}
