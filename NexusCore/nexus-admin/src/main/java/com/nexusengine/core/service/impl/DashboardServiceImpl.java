package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.DashboardInfo;
import com.nexusengine.core.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DashboardInfo getDashboardInfo() {
        DashboardInfo info = new DashboardInfo();
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Date todayStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date yesterdayStart = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date todayEnd = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // 1. Today's Orders
        Long todayOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", todayStart)
                .setParameter("end", todayEnd)
                .getSingleResult();
        info.setTodayOrders(todayOrders != null ? todayOrders : 0L);

        // 2. Today's Revenue
        BigDecimal todayRevenue = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", todayStart)
                .setParameter("end", todayEnd)
                .getSingleResult();
        info.setTodayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO);

        // 3. Yesterday's Revenue
        BigDecimal yesterdayRevenue = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", yesterdayStart)
                .setParameter("end", todayStart)
                .getSingleResult();
        info.setYesterdayRevenue(yesterdayRevenue != null ? yesterdayRevenue : BigDecimal.ZERO);

        // 4. Total Products
        Long totalProducts = (Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p")
                .getSingleResult();
        info.setTotalProducts(totalProducts != null ? totalProducts : 0L);

        // Pending Tasks
        DashboardInfo.PendingTasks tasks = new DashboardInfo.PendingTasks();
        tasks.setAwaitingPayment((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 0").getSingleResult());
        tasks.setAwaitingShipment((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 1").getSingleResult());
        tasks.setShippedOrders((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 2").getSingleResult());
        tasks.setCompletedOrders((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 3").getSingleResult());
        tasks.setAwaitingConfirmation((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.status = 2").getSingleResult());
        tasks.setPendingReturns((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrderReturnApply o WHERE o.status = 0").getSingleResult());
        tasks.setPendingRefunds((Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrderReturnApply o WHERE o.status = 1").getSingleResult());
        tasks.setOutOfStockItems((Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE p.stock <= 0").getSingleResult());
        tasks.setExpiringAds(0L); // Mock for now
        info.setPendingTasks(tasks);

        // Product Overview
        DashboardInfo.ProductOverview pOverview = new DashboardInfo.ProductOverview();
        pOverview.setListed((Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE p.publishStatus = 1").getSingleResult());
        pOverview.setUnlisted((Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE p.publishStatus = 0").getSingleResult());
        pOverview.setLowStock((Long) entityManager.createQuery("SELECT COUNT(p) FROM PmsProduct p WHERE p.stock < p.lowStock").getSingleResult());
        pOverview.setTotal(info.getTotalProducts());
        info.setProductOverview(pOverview);

        // User Overview
        DashboardInfo.UserOverview uOverview = new DashboardInfo.UserOverview();
        uOverview.setTodayNew((Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u WHERE u.createTime >= :start AND u.createTime < :end")
                .setParameter("start", todayStart).setParameter("end", todayEnd).getSingleResult());
        uOverview.setYesterdayNew((Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u WHERE u.createTime >= :start AND u.createTime < :end")
                .setParameter("start", yesterdayStart).setParameter("end", todayStart).getSingleResult());
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        Date monthStart = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        uOverview.setThisMonth((Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u WHERE u.createTime >= :start")
                .setParameter("start", monthStart).getSingleResult());
        uOverview.setTotalMembers((Long) entityManager.createQuery("SELECT COUNT(u) FROM UmsMember u").getSingleResult());
        info.setUserOverview(uOverview);

        // Monthly Summary
        DashboardInfo.MonthlySummary mSummary = new DashboardInfo.MonthlySummary();
        Long monthOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", monthStart).getSingleResult();
        mSummary.setMonthlyOrders(monthOrders);
        mSummary.setMonthlyOrdersTrend("+5%");
        mSummary.setMonthlyOrdersTrendUp(true);
        
        LocalDate firstDayOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        Date weekStart = Date.from(firstDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Long weekOrders = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", weekStart).getSingleResult();
        mSummary.setWeeklyOrders(weekOrders);
        mSummary.setWeeklyOrdersTrend("+12%");
        mSummary.setWeeklyOrdersTrendUp(true);

        BigDecimal monthRev = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", monthStart).getSingleResult();
        mSummary.setMonthlyRevenue(monthRev != null ? monthRev : BigDecimal.ZERO);
        mSummary.setMonthlyRevenueTrend("+8%");
        mSummary.setMonthlyRevenueTrendUp(true);

        BigDecimal weekRev = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.status IN (1,2,3)")
                .setParameter("start", weekStart).getSingleResult();
        mSummary.setWeeklyRevenue(weekRev != null ? weekRev : BigDecimal.ZERO);
        mSummary.setWeeklyRevenueTrend("+15%");
        mSummary.setWeeklyRevenueTrendUp(true);
        info.setMonthlySummary(mSummary);

        // Chart Data (last 15 days)
        List<DashboardInfo.ChartData> chartList = new ArrayList<>();
        for (int i = 14; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            Date dStart = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dEnd = Date.from(d.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            Long cnt = (Long) entityManager.createQuery("SELECT COUNT(o) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", dStart).setParameter("end", dEnd).getSingleResult();
            BigDecimal amt = (BigDecimal) entityManager.createQuery("SELECT SUM(o.payAmount) FROM OmsOrder o WHERE o.createTime >= :start AND o.createTime < :end AND o.status IN (1,2,3)")
                .setParameter("start", dStart).setParameter("end", dEnd).getSingleResult();
                
            DashboardInfo.ChartData cd = new DashboardInfo.ChartData();
            cd.setDate(d.getMonthValue() + "/" + d.getDayOfMonth());
            cd.setOrderCount(cnt != null ? cnt : 0L);
            cd.setOrderAmount(amt != null ? amt : BigDecimal.ZERO);
            chartList.add(cd);
        }
        info.setChartData(chartList);

        return info;
    }
}
