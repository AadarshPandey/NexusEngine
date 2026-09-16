package com.nexusengine.core.repository;

import com.nexusengine.core.model.OmsPaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OmsPaymentTransactionRepository extends JpaRepository<OmsPaymentTransaction, Long> {
    Optional<OmsPaymentTransaction> findByTransactionId(String transactionId);
}
