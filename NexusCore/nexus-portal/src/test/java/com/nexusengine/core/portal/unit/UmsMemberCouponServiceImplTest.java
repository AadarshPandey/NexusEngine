package com.nexusengine.core.portal.unit;

import com.nexusengine.core.common.exception.ApiException;
import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.dao.PortalProductDao;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.domain.SmsCouponHistoryDetail;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.portal.service.impl.UmsMemberCouponServiceImpl;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.repository.SmsCouponHistoryRepository;
import com.nexusengine.core.repository.SmsCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UmsMemberCouponServiceImplTest {

    @Mock
    private UmsMemberService memberService;
    @Mock
    private SmsCouponRepository couponRepository;
    @Mock
    private SmsCouponHistoryRepository couponHistoryRepository;
    @Mock
    private PmsProductRepository productRepository;
    @Mock
    private PortalProductDao portalProductDao;

    @InjectMocks
    private UmsMemberCouponServiceImpl couponService;

    private UmsMember testMember;
    private SmsCoupon testCoupon;

    @BeforeEach
    void setUp() {
        testMember = new UmsMember();
        testMember.setId(1L);
        testMember.setNickname("testUser");

        testCoupon = new SmsCoupon();
        testCoupon.setId(10L);
        testCoupon.setCount(100);
        testCoupon.setPerLimit(1);
        testCoupon.setEnableTime(new Date(System.currentTimeMillis() - 10000)); // already enabled
    }

    @Test
    void add_Success() {
        when(memberService.getCurrentMember()).thenReturn(testMember);
        when(couponRepository.findById(10L)).thenReturn(Optional.of(testCoupon));
        when(couponHistoryRepository.countByCouponIdAndMemberId(10L, 1L)).thenReturn(0L);

        assertDoesNotThrow(() -> couponService.add(10L));

        assertEquals(99, testCoupon.getCount());
        verify(couponHistoryRepository).save(any(SmsCouponHistory.class));
        verify(couponRepository).save(testCoupon);
    }

    @Test
    void add_OutOfStock_ThrowsException() {
        testCoupon.setCount(0);
        when(memberService.getCurrentMember()).thenReturn(testMember);
        when(couponRepository.findById(10L)).thenReturn(Optional.of(testCoupon));

        assertThrows(ApiException.class, () -> couponService.add(10L));
        verify(couponHistoryRepository, never()).save(any());
    }

    @Test
    void add_AlreadyClaimed_ThrowsException() {
        when(memberService.getCurrentMember()).thenReturn(testMember);
        when(couponRepository.findById(10L)).thenReturn(Optional.of(testCoupon));
        when(couponHistoryRepository.countByCouponIdAndMemberId(10L, 1L)).thenReturn(1L); // already claimed 1

        assertThrows(ApiException.class, () -> couponService.add(10L));
        verify(couponHistoryRepository, never()).save(any());
    }

    @Test
    void listHistory_ReturnsHistory() {
        when(memberService.getCurrentMember()).thenReturn(testMember);

        SmsCouponHistory history = new SmsCouponHistory();
        history.setId(100L);
        when(couponHistoryRepository.findByMemberIdAndUseStatus(1L, 0))
                .thenReturn(Collections.singletonList(history));

        List<SmsCouponHistory> result = couponService.listHistory(0);

        assertFalse(result.isEmpty());
        assertEquals(100L, result.get(0).getId());
    }

    @Test
    void listCart_ReturnsEnableList() {
        when(memberService.getCurrentMember()).thenReturn(testMember);

        SmsCouponHistoryDetail detail = new SmsCouponHistoryDetail();
        SmsCoupon coupon = new SmsCoupon();
        coupon.setUseType(0); // All products
        coupon.setMinPoint(new BigDecimal("50"));
        coupon.setEndTime(new Date(System.currentTimeMillis() + 100000)); // valid future
        detail.setCoupon(coupon);

        when(portalProductDao.getDetailList(1L)).thenReturn(Collections.singletonList(detail));

        CartPromotionItem cartItem = new CartPromotionItem();
        cartItem.setPrice(new BigDecimal("100"));
        cartItem.setReduceAmount(new BigDecimal("0"));
        cartItem.setQuantity(1);

        List<SmsCouponHistoryDetail> result = couponService.listCart(Collections.singletonList(cartItem), 1);

        assertFalse(result.isEmpty());
    }

    @Test
    void list_ReturnsCoupons() {
        when(memberService.getCurrentMember()).thenReturn(testMember);

        SmsCouponHistory history = new SmsCouponHistory();
        history.setCouponId(10L);
        when(couponHistoryRepository.findByMemberIdAndUseStatus(1L, 0))
                .thenReturn(Collections.singletonList(history));

        when(couponRepository.findAllById(Collections.singletonList(10L)))
                .thenReturn(Collections.singletonList(testCoupon));

        List<SmsCoupon> result = couponService.list(0);

        assertFalse(result.isEmpty());
        assertEquals(10L, result.get(0).getId());
    }
}
