package com.nexusengine.core.portal.unit;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.OmsPromotionService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.OmsCartItemRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.BeforeEach;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.Test;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.InjectMocks;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.Mock;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Collections;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.List;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Optional;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.junit.jupiter.api.Assertions.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.ArgumentMatchers.any;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmsCartItemServiceImplTest {

    @Mock
    private OmsCartItemRepository cartItemRepository;
    @Mock
    private OmsPromotionService promotionService;
    @Mock
    private UmsMemberService memberService;

    @InjectMocks
    private OmsCartItemServiceImpl cartItemService;

    private UmsMember testMember;
    private OmsCartItem testCartItem;

    @BeforeEach
    void setUp() {
        testMember = new UmsMember();
        testMember.setId(1L);
        testMember.setNickname("tester");

        testCartItem = new OmsCartItem();
        testCartItem.setId(10L);
        testCartItem.setMemberId(1L);
        testCartItem.setProductId(100L);
        testCartItem.setProductSkuId(200L);
        testCartItem.setQuantity(2);
        testCartItem.setDeleteStatus(0);
    }

    @Test
    void add_NewItem_SavesItem() {
        when(memberService.getCurrentMember()).thenReturn(testMember);
        when(cartItemRepository.findByMemberIdAndProductIdAndProductSkuIdAndDeleteStatus(1L, 100L, 200L, 0))
                .thenReturn(null);

        int result = cartItemService.add(testCartItem);

        assertEquals(1, result);
        verify(cartItemRepository).save(any(OmsCartItem.class));
    }

    @Test
    void add_ExistingItem_UpdatesQuantity() {
        OmsCartItem existingItem = new OmsCartItem();
        existingItem.setId(10L);
        existingItem.setQuantity(3);

        when(memberService.getCurrentMember()).thenReturn(testMember);
        when(cartItemRepository.findByMemberIdAndProductIdAndProductSkuIdAndDeleteStatus(1L, 100L, 200L, 0))
                .thenReturn(existingItem);

        int result = cartItemService.add(testCartItem);

        assertEquals(1, result);
        assertEquals(5, existingItem.getQuantity()); // 3 + 2
        verify(cartItemRepository).save(existingItem);
    }

    @Test
    void list_ReturnsCartItems() {
        when(cartItemRepository.findByMemberIdAndDeleteStatus(1L, 0))
                .thenReturn(Collections.singletonList(testCartItem));

        List<OmsCartItem> items = cartItemService.list(1L);

        assertFalse(items.isEmpty());
        assertEquals(10L, items.get(0).getId());
    }

    @Test
    void listPromotion_CalculatesPromotion() {
        when(cartItemRepository.findByMemberIdAndDeleteStatus(1L, 0))
                .thenReturn(Collections.singletonList(testCartItem));
        
        CartPromotionItem promotionItem = new CartPromotionItem();
        promotionItem.setId(10L);
        when(promotionService.calcCartPromotion(anyList()))
                .thenReturn(Collections.singletonList(promotionItem));

        List<CartPromotionItem> result = cartItemService.listPromotion(1L, Collections.singletonList(10L));

        assertFalse(result.isEmpty());
        assertEquals(10L, result.get(0).getId());
        verify(promotionService).calcCartPromotion(anyList());
    }

    @Test
    void updateQuantity_ValidItem_UpdatesAndReturnsOne() {
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(testCartItem));

        int result = cartItemService.updateQuantity(10L, 1L, 5);

        assertEquals(1, result);
        assertEquals(5, testCartItem.getQuantity());
        verify(cartItemRepository).save(testCartItem);
    }

    @Test
    void updateQuantity_InvalidMemberId_ReturnsZero() {
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(testCartItem));

        int result = cartItemService.updateQuantity(10L, 2L, 5); // Different member ID

        assertEquals(0, result);
        assertEquals(2, testCartItem.getQuantity()); // Quantity unchanged
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void delete_ValidIds_UpdatesDeleteStatus() {
        when(cartItemRepository.findAllById(anyList())).thenReturn(Collections.singletonList(testCartItem));

        int result = cartItemService.delete(1L, Collections.singletonList(10L));

        assertEquals(1, result);
        assertEquals(1, testCartItem.getDeleteStatus());
        verify(cartItemRepository).save(testCartItem);
    }

    @Test
    void clear_UpdatesAllItemsToDeleteStatusOne() {
        when(cartItemRepository.findByMemberIdAndDeleteStatus(1L, 0))
                .thenReturn(Collections.singletonList(testCartItem));

        int result = cartItemService.clear(1L);

        assertEquals(1, result);
        assertEquals(1, testCartItem.getDeleteStatus());
        verify(cartItemRepository).saveAll(anyList());
    }
}
