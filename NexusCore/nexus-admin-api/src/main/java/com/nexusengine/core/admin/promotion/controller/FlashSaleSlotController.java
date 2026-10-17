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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/promotion/flash-sale-slots")
@Tag(name = "Promotion - Flash Sale Slots", description = "Admin flash sale slot management APIs")
@RequiredArgsConstructor
public class FlashSaleSlotController {
    private final FlashSaleSlotRepository sessionRepository;
    private final com.nexusengine.core.domain.promotion.repository.FlashSaleItemRepository itemRepository;

    @Operation(summary = "List all flash sale slots")
    @GetMapping
    public List<FlashSaleSlot> list() {
        return sessionRepository.findAll();
    }

    @Operation(summary = "List selectable flash sale slots")
    @GetMapping("/selectable")
    public List<FlashSaleSlot> selectList(
            @RequestParam(value = "flashPromotionId", required = false) Long flashPromotionId) {
        return sessionRepository.findAll();
    }

    @Operation(summary = "List flash sale items for a campaign and slot")
    @GetMapping("/items")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public org.springframework.data.domain.Page<com.nexusengine.core.domain.promotion.entity.FlashSaleItem> listItems(
            @RequestParam("flashSaleId") Long flashSaleId,
            @RequestParam("slotId") Long slotId,
            @org.springframework.data.web.PageableDefault(size = 20) org.springframework.data.domain.Pageable pageable) {
        var page = itemRepository.findByFlashPromotionIdAndFlashPromotionSessionId(flashSaleId, slotId, pageable);
        page.getContent().forEach(item -> {
            if (item.getProduct() != null) {
                org.hibernate.Hibernate.initialize(item.getProduct().getGalleryList());
            }
        });
        return page;
    }

    @Operation(summary = "Create flash sale slot")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlashSaleSlot create(@Valid @RequestBody FlashSaleSlot session) {
        return sessionRepository.save(session);
    }

    @Operation(summary = "Update flash sale slot")
    @PutMapping("/{id}")
    public FlashSaleSlot update(
            @PathVariable("id") Long id, @Valid @RequestBody FlashSaleSlot session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Operation(summary = "Update flash sale slot status")
    @PatchMapping("/{id}/status")
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        sessionRepository.deleteById(id);
    }
}
