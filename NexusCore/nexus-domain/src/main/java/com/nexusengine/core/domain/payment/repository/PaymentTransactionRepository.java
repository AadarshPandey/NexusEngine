package com.nexusengine.core.domain.payment.repository;

import com.nexusengine.core.domain.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByTransactionId(String transactionId);
    List<PaymentTransaction> findByOrderIdOrderByCreateTimeAsc(Long orderId);
    List<PaymentTransaction> findByOrderIdInOrderByCreateTimeAsc(List<Long> orderIds);
}
