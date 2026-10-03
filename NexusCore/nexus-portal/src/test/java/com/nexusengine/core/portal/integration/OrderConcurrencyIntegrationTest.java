package com.nexusengine.core.portal.integration;

import com.nexusengine.core.model.*;
import com.nexusengine.core.portal.TestFixtures;
import com.nexusengine.core.portal.domain.OrderParam;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.portal.service.OmsPortalOrderService;
import com.nexusengine.core.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
public class OrderConcurrencyIntegrationTest {

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Autowired
    private PmsSkuStockRepository skuStockRepository;

    @Autowired
    private UmsMemberRepository memberRepository;

    @Autowired
    private UmsMemberReceiveAddressRepository addressRepository;

    @Autowired
    private OmsCartItemRepository cartItemRepository;

    @Autowired
    private PmsProductRepository productRepository;
    
    @Autowired
    private OmsOrderSettingRepository orderSettingRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository.deleteAll();
        skuStockRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
        addressRepository.deleteAll();
        
        OmsOrderSetting orderSetting = new OmsOrderSetting();
        orderSetting.setId(1L);
        orderSetting.setNormalOrderOvertime(60);
        orderSettingRepository.save(orderSetting);
    }

    @Test
    void testConcurrentOrderGeneration_PreventsOverselling() throws InterruptedException {
        // 1. Setup Product and SKU with exactly 10 stock
        PmsProduct product = new PmsProduct();
        product.setPrice(new BigDecimal("100.00"));
        product = productRepository.save(product);

        PmsSkuStock sku = new PmsSkuStock();
        sku.setProductId(product.getId());
        sku.setStock(10);
        sku.setLockStock(0);
        sku.setPrice(new BigDecimal("100.00"));
        sku = skuStockRepository.save(sku);

        // 2. Setup Customer A (wants 7)
        UmsMember memberA = new UmsMember();
        memberA.setUsername("userA");
        memberA.setStatus(1);
        memberA = memberRepository.save(memberA);

        UmsMemberReceiveAddress addressA = new UmsMemberReceiveAddress();
        addressA.setMemberId(memberA.getId());
        addressA = addressRepository.save(addressA);

        OmsCartItem cartItemA = new OmsCartItem();
        cartItemA.setMemberId(memberA.getId());
        cartItemA.setProductId(product.getId());
        cartItemA.setProductSkuId(sku.getId());
        cartItemA.setQuantity(7);
        cartItemA.setPrice(new BigDecimal("100.00"));
        cartItemA = cartItemRepository.save(cartItemA);

        // 3. Setup Customer B (wants 5)
        UmsMember memberB = new UmsMember();
        memberB.setUsername("userB");
        memberB.setStatus(1);
        memberB = memberRepository.save(memberB);

        UmsMemberReceiveAddress addressB = new UmsMemberReceiveAddress();
        addressB.setMemberId(memberB.getId());
        addressB = addressRepository.save(addressB);

        OmsCartItem cartItemB = new OmsCartItem();
        cartItemB.setMemberId(memberB.getId());
        cartItemB.setProductId(product.getId());
        cartItemB.setProductSkuId(sku.getId());
        cartItemB.setQuantity(5);
        cartItemB.setPrice(new BigDecimal("100.00"));
        cartItemB = cartItemRepository.save(cartItemB);

        int numberOfThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // Task for Customer A
        UmsMember finalMemberA = memberA;
        UmsMemberReceiveAddress finalAddressA = addressA;
        List<Long> cartIdsA = Collections.singletonList(cartItemA.getId());
        executor.submit(() -> {
            TestFixtures.setAuthenticatedMember(finalMemberA);
            readyLatch.countDown();
            try {
                startLatch.await();
                OrderParam param = new OrderParam(finalAddressA.getId(), null, 1, 0, cartIdsA);
                portalOrderService.generateOrder(param);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                TestFixtures.clearAuthentication();
                doneLatch.countDown();
            }
        });

        // Task for Customer B
        UmsMember finalMemberB = memberB;
        UmsMemberReceiveAddress finalAddressB = addressB;
        List<Long> cartIdsB = Collections.singletonList(cartItemB.getId());
        executor.submit(() -> {
            TestFixtures.setAuthenticatedMember(finalMemberB);
            readyLatch.countDown();
            try {
                startLatch.await();
                OrderParam param = new OrderParam(finalAddressB.getId(), null, 1, 0, cartIdsB);
                portalOrderService.generateOrder(param);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                TestFixtures.clearAuthentication();
                doneLatch.countDown();
            }
        });

        readyLatch.await(); // Wait for all threads to be ready
        startLatch.countDown(); // Unleash them at the exact same time
        doneLatch.await(15, TimeUnit.SECONDS);

        // Assertions
        // Only one of them should succeed since 7 + 5 = 12 > 10.
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(1);

        // Verify Database state
        PmsSkuStock updatedSku = skuStockRepository.findById(sku.getId()).orElseThrow();
        // The lockStock should be either 7 or 5 depending on who won.
        assertThat(updatedSku.getLockStock()).isIn(5, 7);
    }
}
