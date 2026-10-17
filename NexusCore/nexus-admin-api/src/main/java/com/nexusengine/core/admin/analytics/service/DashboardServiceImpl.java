package com.nexusengine.core.admin.analytics.service;

import com.nexusengine.core.admin.analytics.dto.DashboardInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        OffsetDateTime todayStart = today.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime yesterdayStart = yesterday.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime todayEnd = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

        // 1. Today's Orders
        Long todayOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", todayStart)
                .setParameter("end", todayEnd)
                .getSingleResult();
        todayOrders = todayOrders != null ? todayOrders : 0L;

        // 2. Today's Revenue
        BigDecimal todayRevenue = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM CustomerOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", todayStart)
                .setParameter("end", todayEnd)
                .getSingleResult();
        todayRevenue = todayRevenue != null ? todayRevenue : BigDecimal.ZERO;

        // 3. Yesterday's Revenue
        BigDecimal yesterdayRevenue = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM CustomerOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", yesterdayStart)
                .setParameter("end", todayStart)
                .getSingleResult();
        yesterdayRevenue = yesterdayRevenue != null ? yesterdayRevenue : BigDecimal.ZERO;

        // 4. Total Products
        Long totalProducts = (Long) entityManager.createQuery("SELECT COUNT(p) FROM Product p")
                .getSingleResult();
        totalProducts = totalProducts != null ? totalProducts : 0L;

        // Pending Tasks
        DashboardInfo.PendingTasks tasks = new DashboardInfo.PendingTasks(
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status = 0").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status = 1").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status = 2").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status = 3").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status = 2").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM ReturnRequest o WHERE o.status = 0").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(o) FROM ReturnRequest o WHERE o.status = 1").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE NOT EXISTS (SELECT 1 FROM ProductSku s WHERE s.productId = p.id AND (COALESCE(s.onHandQty, 0) - COALESCE(s.reservedQty, 0)) > 0)").getSingleResult(),
            0L // Mock for now
        );

        // Product Overview
        DashboardInfo.ProductOverview pOverview = new DashboardInfo.ProductOverview(
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.isPublished = false").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.isPublished = true").getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE EXISTS (SELECT 1 FROM ProductSku s WHERE s.productId = p.id AND (COALESCE(s.onHandQty, 0) - COALESCE(s.reservedQty, 0)) < COALESCE(s.lowStockThreshold, 0))").getSingleResult(),
            totalProducts
        );

        // User Overview
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        OffsetDateTime monthStart = firstDayOfMonth.atStartOfDay().atOffset(ZoneOffset.UTC);
        DashboardInfo.UserOverview uOverview = new DashboardInfo.UserOverview(
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM Member u WHERE u.createdAt >= :start AND u.createdAt < :end").setParameter("start", todayStart).setParameter("end", todayEnd).getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM Member u WHERE u.createdAt >= :start AND u.createdAt < :end").setParameter("start", yesterdayStart).setParameter("end", todayStart).getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM Member u WHERE u.createdAt >= :start").setParameter("start", monthStart).getSingleResult(),
            (Long) entityManager.createQuery("SELECT COUNT(u) FROM Member u").getSingleResult()
        );

        // Monthly Summary
        Long monthOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", monthStart).getSingleResult();

        LocalDate firstDayOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        OffsetDateTime weekStart = firstDayOfWeek.atStartOfDay().atOffset(ZoneOffset.UTC);
        Long weekOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", weekStart).getSingleResult();

        BigDecimal monthRev = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM CustomerOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", monthStart).getSingleResult();
        monthRev = monthRev != null ? monthRev : BigDecimal.ZERO;

        BigDecimal weekRev = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM CustomerOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
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
            OffsetDateTime dStart = d.atStartOfDay().atOffset(ZoneOffset.UTC);
            OffsetDateTime dEnd = d.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

            Long cnt = (Long) entityManager.createQuery("SELECT COUNT(o) FROM CustomerOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", dStart).setParameter("end", dEnd).getSingleResult();
            BigDecimal amt = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM CustomerOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
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
