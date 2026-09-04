package com.nexusengine.core.repository;
import com.nexusengine.core.model.OmsOrderOperateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OmsOrderOperateHistoryRepository extends JpaRepository<OmsOrderOperateHistory, Long> {
    List<OmsOrderOperateHistory> findByOrderId(Long orderId);
}
