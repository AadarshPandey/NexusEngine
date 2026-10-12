package com.nexusengine.core.portal.ordering.integration;

import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.customer.entity.MemberAddress;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.ordering.entity.CartItem;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.repository.MemberRepository;
import com.nexusengine.core.domain.ordering.repository.OrderPolicyRepository;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import com.nexusengine.core.domain.ordering.repository.CartItemRepository;
import com.nexusengine.core.domain.customer.repository.MemberAddressRepository;

import com.nexusengine.core.portal.TestFixtures;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.portal.ordering.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import com.nexusengine.core.portal.ordering.dto.OrderParam;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
@Testcontainers
public class OrderConcurrencyIntegrationTest {

    @Autowired
    private OrderService portalOrderService;

    @Autowired
    private ProductSkuRepository skuStockRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAddressRepository addressRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderPolicyRepository orderSettingRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository.deleteAll();

        OrderPolicy orderSetting = orderSettingRepository.findById(1L).orElseGet(() -> {
            OrderPolicy policy = new OrderPolicy();
            policy.setId(1L);
            return policy;
        });
        orderSetting.setPaymentTimeoutMin(60);
        orderSettingRepository.save(orderSetting);
    }

    @Test
    void testConcurrentOrderGeneration_PreventsOverselling() throws InterruptedException {
        Product product = new Product();
        product.setProductCategoryId(1L);
        product.setName("Concurrency Test Product");
        product.setProductSn("TEST-CONC-" + System.currentTimeMillis());
        product = productRepository.save(product);

        ProductSku sku = new ProductSku();
        sku.setProductId(product.getId());
        sku.setOnHandQty(10);
        sku.setReservedQty(0);
        sku.setSalePrice(new BigDecimal("100.00"));
        sku.setListPrice(new BigDecimal("100.00"));
        sku = skuStockRepository.save(sku);

        // 2. Setup Customer A (wants 7)
        Member memberA = TestFixtures.member();
        memberA.setId(null);
        memberA.setUsername("userA_" + System.currentTimeMillis());
        memberA = memberRepository.save(memberA);

        MemberAddress addressA = new MemberAddress();
        addressA.setMemberId(memberA.getId());
        addressA.setDefaultStatus(0);
        addressA = addressRepository.save(addressA);

        CartItem cartItemA = new CartItem();
        cartItemA.setMemberId(memberA.getId());
        cartItemA.setProductId(product.getId());
        cartItemA.setProductSkuId(sku.getId());
        cartItemA.setQuantity(7);
        cartItemA.setPrice(new BigDecimal("100.00"));
        cartItemA = cartItemRepository.save(cartItemA);

        // 3. Setup Customer B (wants 5)
        Member memberB = TestFixtures.member();
        memberB.setId(null);
        memberB.setUsername("userB_" + System.currentTimeMillis());
        memberB = memberRepository.save(memberB);

        MemberAddress addressB = new MemberAddress();
        addressB.setMemberId(memberB.getId());
        addressB.setDefaultStatus(0);
        addressB = addressRepository.save(addressB);

        CartItem cartItemB = new CartItem();
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
        Member finalMemberA = memberA;
        MemberAddress finalAddressA = addressA;
        List<Long> cartIdsA = Collections.singletonList(cartItemA.getId());
        executor.submit(() -> {
            TestFixtures.setAuthenticatedMember(finalMemberA);
            readyLatch.countDown();
            try {
                startLatch.await();
                OrderParam param = new OrderParam(finalAddressA.getId(), null, 0, 0, cartIdsA);
                portalOrderService.generateOrder(param);
                successCount.incrementAndGet();
            } catch (Throwable e) {
                System.err.println("THREAD A FAILED: " + e.getClass().getName() + " -> " + e.getMessage());
                failCount.incrementAndGet();
            } finally {
                TestFixtures.clearAuthentication();
                doneLatch.countDown();
            }
        });

        // Task for Customer B
        Member finalMemberB = memberB;
        MemberAddress finalAddressB = addressB;
        List<Long> cartIdsB = Collections.singletonList(cartItemB.getId());
        executor.submit(() -> {
            TestFixtures.setAuthenticatedMember(finalMemberB);
            readyLatch.countDown();
            try {
                startLatch.await();
                OrderParam param = new OrderParam(finalAddressB.getId(), null, 0, 0, cartIdsB);
                portalOrderService.generateOrder(param);
                successCount.incrementAndGet();
            } catch (Throwable e) {
                System.err.println("THREAD B FAILED: " + e.getClass().getName() + " -> " + e.getMessage());
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
        ProductSku updatedSku = skuStockRepository.findById(sku.getId()).orElseThrow();
        // The reservedQty should be either 7 or 5 depending on who won.
        assertThat(updatedSku.getReservedQty()).isIn(5, 7);
    }
}
