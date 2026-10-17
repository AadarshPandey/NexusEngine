package com.nexusengine.core.admin.analytics.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardInfo(
    Long todayOrders,
    BigDecimal todayRevenue,
    BigDecimal yesterdayRevenue,
    Long totalProducts,
    List<ChartData> chartData,
    PendingTasks pendingTasks,
    ProductOverview productOverview,
    UserOverview userOverview,
    MonthlySummary monthlySummary,
    OutboxMetrics outboxMetrics,
    List<RecentAdminLogin> recentAdminLogins
) {
    public DashboardInfo(
        Long todayOrders,
        BigDecimal todayRevenue,
        BigDecimal yesterdayRevenue,
        Long totalProducts,
        List<ChartData> chartData,
        PendingTasks pendingTasks,
        ProductOverview productOverview,
        UserOverview userOverview,
        MonthlySummary monthlySummary
    ) {
        this(todayOrders, todayRevenue, yesterdayRevenue, totalProducts, chartData, pendingTasks, productOverview, userOverview, monthlySummary, null, List.of());
    }

    public record OutboxMetrics(
        Long totalEvents,
        Long publishedEvents,
        Long pendingEvents,
        Long failedEvents,
        List<OutboxEventSummary> recentEvents
    ) {}

    public record OutboxEventSummary(
        Long id,
        String aggregateType,
        String aggregateId,
        String eventType,
        String status,
        Integer attemptCount,
        java.time.OffsetDateTime createdAt
    ) {}

    public record RecentAdminLogin(
        Long id,
        Long adminId,
        String username,
        String ipAddress,
        java.time.OffsetDateTime occurredAt
    ) {}

    public record ChartData(
        String date,
        Long orderCount,
        BigDecimal orderAmount
    ) {}

    public record PendingTasks(
        Long awaitingPayment,
        Long awaitingShipment,
        Long shippedOrders,
        Long completedOrders,
        Long awaitingConfirmation,
        Long pendingReturns,
        Long pendingRefunds,
        Long outOfStockItems,
        Long expiringAds
    ) {}

    public record ProductOverview(
        Long unlisted,
        Long listed,
        Long lowStock,
        Long total
    ) {}

    public record UserOverview(
        Long todayNew,
        Long yesterdayNew,
        Long thisMonth,
        Long totalMembers
    ) {}

    public record MonthlySummary(
        Long monthlyOrders,
        String monthlyOrdersTrend,
        Boolean monthlyOrdersTrendUp,
        Long weeklyOrders,
        String weeklyOrdersTrend,
        Boolean weeklyOrdersTrendUp,
        BigDecimal monthlyRevenue,
        String monthlyRevenueTrend,
        Boolean monthlyRevenueTrendUp,
        BigDecimal weeklyRevenue,
        String weeklyRevenueTrend,
        Boolean weeklyRevenueTrendUp
    ) {}
}
