package com.nexusengine.core.portal.promotion.integration;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.portal.TestFixtures;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.portal.promotion.service.MemberCouponService;
import com.nexusengine.core.domain.promotion.repository.CouponRepository;
import com.nexusengine.core.domain.customer.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
@Testcontainers
public class CouponConcurrencyIntegrationTest {

    private final MemberCouponService memberCouponService;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    @Autowired
    CouponConcurrencyIntegrationTest(MemberCouponService memberCouponService, CouponRepository couponRepository, MemberRepository memberRepository) {
        this.memberCouponService = memberCouponService;
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }





    @BeforeEach
    void setUp() {
        TestFixtures.clearAuthentication();
    }

    @Test
    void concurrentCouponClaim_OnlyOneSucceeds() throws InterruptedException {
        // Setup Member
        Member member = TestFixtures.member();
        member.setUsername("concurrencyTestUser");
        member = memberRepository.save(member);

        // Setup Coupon
        Coupon coupon = TestFixtures.coupon(0, new java.math.BigDecimal("50"), java.math.BigDecimal.ZERO, 1);
        coupon.setCount(10); // Plenty of stock
        coupon.setId(null); // let DB generate
        coupon = couponRepository.save(coupon);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);

        Long couponId = coupon.getId();
        Member authMember = member;

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    TestFixtures.setAuthenticatedMember(authMember);
                    memberCouponService.add(couponId);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                    // Expected: concurrent threads failing to claim coupon throw exceptions
                }
            });
        }
        
        latch.countDown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Even though 10 threads tried, perLimit=1, so only 1 should succeed
        assertThat(successCount.get()).isEqualTo(1);
    }
}
