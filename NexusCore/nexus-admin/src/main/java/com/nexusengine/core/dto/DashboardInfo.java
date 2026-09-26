package com.nexusengine.core.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record DashboardInfo(
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
