package com.nexusengine.core.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class DashboardInfo {
    private Long todayOrders;
    private BigDecimal todayRevenue;
    private BigDecimal yesterdayRevenue;
    private Long totalProducts;

    private List<ChartData> chartData;
    private PendingTasks pendingTasks;
    private ProductOverview productOverview;
    private UserOverview userOverview;
    private MonthlySummary monthlySummary;

    @Data
    public static class ChartData {
        private String date;
        private Long orderCount;
        private BigDecimal orderAmount;
    }

    @Data
    public static class PendingTasks {
        private Long awaitingPayment;
        private Long awaitingShipment;
        private Long shippedOrders;
        private Long completedOrders;
        private Long awaitingConfirmation;
        private Long pendingReturns;
        private Long pendingRefunds;
        private Long outOfStockItems;
        private Long expiringAds;
    }

    @Data
    public static class ProductOverview {
        private Long unlisted;
        private Long listed;
        private Long lowStock;
        private Long total;
    }

    @Data
    public static class UserOverview {
        private Long todayNew;
        private Long yesterdayNew;
        private Long thisMonth;
        private Long totalMembers;
    }

    @Data
    public static class MonthlySummary {
        private Long monthlyOrders;
        private String monthlyOrdersTrend;
        private Boolean monthlyOrdersTrendUp;

        private Long weeklyOrders;
        private String weeklyOrdersTrend;
        private Boolean weeklyOrdersTrendUp;

        private BigDecimal monthlyRevenue;
        private String monthlyRevenueTrend;
        private Boolean monthlyRevenueTrendUp;

        private BigDecimal weeklyRevenue;
        private String weeklyRevenueTrend;
        private Boolean weeklyRevenueTrendUp;
    }
}
