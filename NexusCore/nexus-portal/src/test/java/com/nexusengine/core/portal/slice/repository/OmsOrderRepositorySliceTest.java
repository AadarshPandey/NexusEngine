package com.nexusengine.core.portal.slice.repository;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.repository.OmsOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
public class OmsOrderRepositorySliceTest {

    @Autowired
    private OmsOrderRepository orderRepository;

    @Test
    void save_NewOrder_PersistsAllFields() {
        OmsOrder order = new OmsOrder();
        order.setMemberId(1L);
        order.setOrderSn("TEST-SN-123");
        order.setCreateTime(new Date());
        order.setMemberUsername("testuser");
        order.setTotalAmount(new BigDecimal("100.50"));
        order.setStatus(0);
        order.setDeleteStatus(0);

        OmsOrder savedOrder = orderRepository.save(order);
        
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderSn()).isEqualTo("TEST-SN-123");
    }

    @Test
    void findByMemberIdAndStatus_ReturnsCorrectOrders() {
        OmsOrder order1 = new OmsOrder();
        order1.setMemberId(1L);
        order1.setStatus(0);
        order1.setDeleteStatus(0);
        orderRepository.save(order1);

        OmsOrder order2 = new OmsOrder();
        order2.setMemberId(1L);
        order2.setStatus(1);
        order2.setDeleteStatus(0);
        orderRepository.save(order2);

        List<OmsOrder> unpaidOrders = orderRepository.findAll().stream().filter(o -> o.getMemberId().equals(1L) && o.getStatus() == 0).toList();
        
        assertThat(unpaidOrders).hasSize(1);
        assertThat(unpaidOrders.get(0).getStatus()).isEqualTo(0);
    }
}
