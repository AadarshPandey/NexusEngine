package com.nexusengine.core.portal.unit;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.common.exception.ApiException;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.common.service.RedisService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.OmsOrderSettingRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.component.CancelOrderSender;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.dao.PortalOrderDao;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.OrderParam;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.OmsCartItemService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.UmsMemberReceiveAddressService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.OmsOrderItemRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.OmsOrderRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.PmsSkuStockRepository;
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
import java.math.BigDecimal;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.*;

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
public class OmsPortalOrderServiceImplTest {

    @Mock
    private UmsMemberService memberService;
    @Mock
    private OmsCartItemService cartItemService;
    @Mock
    private UmsMemberReceiveAddressService memberReceiveAddressService;
    @Mock
    private PmsSkuStockRepository skuStockRepository;
    @Mock
    private OmsOrderRepository orderRepository;
    @Mock
    private OmsOrderItemRepository orderItemRepository;
    @Mock
    private PortalOrderDao portalOrderDao;
    @Mock
    private CancelOrderSender cancelOrderSender;
    @Mock
    private RedisService redisService;
    @Mock
    private OmsOrderSettingRepository orderSettingRepository;
    @Mock
    private com.nexusengine.core.repository.PmsProductRepository productRepository;

    @InjectMocks
    private OmsPortalOrderServiceImpl orderService;

    private UmsMember currentMember;
    private OrderParam orderParam;
    private CartPromotionItem cartPromotionItem;
    private PmsSkuStock skuStock;

    @BeforeEach
    void setUp() {
        currentMember = new UmsMember();
        currentMember.setId(1L);
        currentMember.setUsername("testuser");
        currentMember.setIntegration(100);

        orderParam = new OrderParam();
        orderParam.setMemberReceiveAddressId(1L);
        orderParam.setPayType(1);
        orderParam.setCartIds(Collections.singletonList(1L));

        cartPromotionItem = new CartPromotionItem();
        cartPromotionItem.setId(1L);
        cartPromotionItem.setProductId(1L);
        cartPromotionItem.setProductSkuId(1L);
        cartPromotionItem.setQuantity(2);
        cartPromotionItem.setPrice(new BigDecimal("100.00"));
        cartPromotionItem.setReduceAmount(new BigDecimal("0.00"));
        cartPromotionItem.setIntegration(0);
        cartPromotionItem.setGrowth(0);
        cartPromotionItem.setRealStock(10); // Added realStock to pass hasStock() check

        skuStock = new PmsSkuStock();
        skuStock.setId(1L);
        skuStock.setStock(10);
        skuStock.setLockStock(0);
    }

    @Test
    void generateOrder_Success() {
        when(memberService.getCurrentMember()).thenReturn(currentMember);
        when(cartItemService.listPromotion(eq(1L), anyList()))
                .thenReturn(Collections.singletonList(cartPromotionItem));
        when(memberReceiveAddressService.getItem(1L)).thenReturn(new UmsMemberReceiveAddress());
        when(skuStockRepository.findById(1L)).thenReturn(Optional.of(skuStock));
        when(redisService.incr(any(), anyLong())).thenReturn(1L);
        OmsOrderSetting omsOrderSetting = new OmsOrderSetting();
        omsOrderSetting.setNormalOrderOvertime(120);
        when(orderSettingRepository.findById(1L)).thenReturn(Optional.of(omsOrderSetting));
        
        when(orderRepository.save(any(OmsOrder.class))).thenAnswer(invocation -> {
            OmsOrder order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });
        
        PmsProduct mockProduct = new PmsProduct();
        mockProduct.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Map<String, Object> result = orderService.generateOrder(orderParam);

        assertNotNull(result);
        assertTrue(result.containsKey("order"));
        
        OmsOrder savedOrder = (OmsOrder) result.get("order");
        assertEquals(100L, savedOrder.getId());
        assertEquals(new BigDecimal("200.00"), savedOrder.getTotalAmount()); // 2 * 100
        
        verify(skuStockRepository).save(any(PmsSkuStock.class)); // Verifies lockStock
        verify(orderItemRepository).saveAll(anyList());
        verify(cancelOrderSender).sendMessage(eq(100L), anyLong());
    }

    @Test
    void generateOrder_NoAddress_ThrowsException() {
        orderParam.setMemberReceiveAddressId(null);

        assertThrows(ApiException.class, () -> orderService.generateOrder(orderParam));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void generateOrder_InsufficientStock_ThrowsException() {
        cartPromotionItem.setRealStock(1); // Need 2, only have 1

        when(memberService.getCurrentMember()).thenReturn(currentMember);
        when(cartItemService.listPromotion(eq(1L), anyList()))
                .thenReturn(Collections.singletonList(cartPromotionItem));

        assertThrows(ApiException.class, () -> orderService.generateOrder(orderParam));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelOrder_Success() {
        OmsOrder order = new OmsOrder();
        order.setId(100L);
        order.setStatus(0); // Unpaid

        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setProductSkuId(1L);
        orderItem.setProductQuantity(2);

        when(orderRepository.findByIdAndStatusAndDeleteStatus(100L, 0, 0))
                .thenReturn(Collections.singletonList(order));
        when(orderItemRepository.findByOrderId(100L)).thenReturn(Collections.singletonList(orderItem));

        orderService.cancelOrder(100L);

        assertEquals(4, order.getStatus()); // Cancelled status
        verify(orderRepository).save(order);
        verify(portalOrderDao).releaseSkuStockLock(anyList());
    }
}
