package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.DashboardInfo;
import com.nexusengine.core.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DashboardInfo getDashboardInfo() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime yesterdayStart = yesterday.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();

        // 1. Today's Orders
        Long todayOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", todayStart)
                .setParameter("end", todayEnd)
                .getSingleResult();
        todayOrders = todayOrders != null ? todayOrders : 0L;

        // 2. Today's Revenue
        BigDecimal todayRevenue = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", todayStart)
                .setParameter("end", todayEnd)
                .getSingleResult();
        todayRevenue = todayRevenue != null ? todayRevenue : BigDecimal.ZERO;

        // 3. Yesterday's Revenue
        BigDecimal yesterdayRevenue = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", yesterdayStart)
                .setParameter("end", todayStart)
                .getSingleResult();
        yesterdayRevenue = yesterdayRevenue != null ? yesterdayRevenue : BigDecimal.ZERO;

        // 4. Total Products
        Long totalProducts = (Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p")
                .getSingleResult();
        totalProducts = totalProducts != null ? totalProducts : 0L;

        // Pending Tasks
        DashboardInfo.PendingTasks tasks = new DashboardInfo.PendingTasks(
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 0").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 1").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 2").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 3").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 2").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrderReturnApply o WHERE o.status = 0").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrderReturnApply o WHERE o.status = 1").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE NOT EXISTS (SELECT 1 FROM PmsSkuStock s WHERE s.productId = p.id AND (s.stock - s.lockStock) > 0)").getSingleResult(),
            0L // Mock for now
        );

        // Product Overview
        DashboardInfo.ProductOverview pOverview = new DashboardInfo.ProductOverview(
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE p.publishStatus = 0").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE p.publishStatus = 1").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE EXISTS (SELECT 1 FROM PmsSkuStock s WHERE s.productId = p.id AND (s.stock - s.lockStock) < s.lowStock)").getSingleResult(),
            totalProducts
        );

        // User Overview
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDateTime monthStart = firstDayOfMonth.atStartOfDay();
        DashboardInfo.UserOverview uOverview = new DashboardInfo.UserOverview(
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u WHERE u.createTime >= :start AND u.createTime < :end").setParameter("start", todayStart).setParameter("end", todayEnd).getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u WHERE u.createTime >= :start AND u.createTime < :end").setParameter("start", yesterdayStart).setParameter("end", todayStart).getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u WHERE u.createTime >= :start").setParameter("start", monthStart).getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u").getSingleResult()
        );

        // Monthly Summary
        Long monthOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", monthStart).getSingleResult();
        
        LocalDate firstDayOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDateTime weekStart = firstDayOfWeek.atStartOfDay();
        Long weekOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", weekStart).getSingleResult();

        BigDecimal monthRev = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", monthStart).getSingleResult();
        monthRev = monthRev != null ? monthRev : BigDecimal.ZERO;

        BigDecimal weekRev = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", weekStart).getSingleResult();
        weekRev = weekRev != null ? weekRev : BigDecimal.ZERO;

        DashboardInfo.MonthlySummary mSummary = new DashboardInfo.MonthlySummary(
            monthOrders, "+5%", true,
            weekOrders, "+12%", true,
            monthRev, "+8%", true,
            weekRev, "+15%", true
        );

        // Chart Data (last 15 days)
        List<DashboardInfo.ChartData> chartList = new ArrayList<>();
        for (int i = 14; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            LocalDateTime dStart = d.atStartOfDay();
            LocalDateTime dEnd = d.plusDays(1).atStartOfDay();
            
            Long cnt = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", dStart).setParameter("end", dEnd).getSingleResult();
            BigDecimal amt = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", dStart).setParameter("end", dEnd).getSingleResult();
                
            chartList.add(new DashboardInfo.ChartData(
                d.getMonthValue() + "/" + d.getDayOfMonth(),
                cnt != null ? cnt : 0L,
                amt != null ? amt : BigDecimal.ZERO
            ));
        }

        return new DashboardInfo(todayOrders, todayRevenue, yesterdayRevenue, totalProducts, chartList, tasks, pOverview, uOverview, mSummary);
    }
}
