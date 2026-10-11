package com.nexusengine.core.portal.promotion.integration;

import com.nexusengine.core.domain.promotion.entity.FlashSaleItem;
import com.nexusengine.core.domain.promotion.repository.FlashSaleItemRepository;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.customer.repository.MemberRepository;

import com.nexusengine.core.portal.TestFixtures;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import com.nexusengine.core.portal.ordering.service.FlashSaleOrderService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
@Testcontainers
public class FlashSaleConcurrencyIntegrationTest {

    @Autowired
    private FlashSaleOrderService flashSaleOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FlashSaleItemRepository flashRelationRepository;

    @Autowired
    private CustomerOrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        flashRelationRepository.deleteAll();
        orderRepository.deleteAll();
        // Clear redis keys manually if needed
    }

    @Test
    void testFlashSaleConcurrency_PreventsOverselling() throws InterruptedException {
        // Setup Flash Promotion Relation with exactly 10 stock
        FlashSaleItem relation = new FlashSaleItem();
        relation.setProductId(1L);
        relation.setFlashPromotionId(1L);
        relation.setFlashPromotionSessionId(1L);
        relation.setFlashPromotionCount(10);
        relation.setFlashPromotionPrice(new BigDecimal("99.99"));
        flashRelationRepository.save(relation);

        // Pre-warm Redis Stock
        String stockKey = "flash:stock:1:1:1";
        redisTemplate.opsForValue().set(stockKey, "10");

        int numberOfUsers = 15; // 15 users trying to buy, but only 10 stock available
        ExecutorService executor = Executors.newFixedThreadPool(numberOfUsers);
        CountDownLatch readyLatch = new CountDownLatch(numberOfUsers);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfUsers);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfUsers; i++) {
            Member member = new Member();
            member.setUsername("flashUser" + i);
            member.setStatus(1);
            member = memberRepository.save(member);
            final Member currentMember = member;

            executor.submit(() -> {
                TestFixtures.setAuthenticatedMember(currentMember);
                readyLatch.countDown();
                try {
                    startLatch.await();
                    // Each user tries to buy 1 item
                    var result = flashSaleOrderService.generateFlashOrder(1L, 1L, 1L, 1);
                    if (result.getCode() == 200) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    TestFixtures.clearAuthentication();
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown(); // Unleash them at the exact same time
        doneLatch.await(15, TimeUnit.SECONDS);

        // Assertions
        assertThat(successCount.get()).isEqualTo(10); // Exactly 10 should succeed
        assertThat(failCount.get()).isEqualTo(5);  // Exactly 5 should be blocked

        // Wait a bit for RabbitMQ listener to process the DB updates
        Thread.sleep(3000);

        FlashSaleItem updatedRelation = flashRelationRepository.findById(relation.getId()).orElseThrow();
        assertThat(updatedRelation.getFlashPromotionCount()).isEqualTo(0);

        long totalOrders = orderRepository.count();
        assertThat(totalOrders).isEqualTo(10);
    }
}
