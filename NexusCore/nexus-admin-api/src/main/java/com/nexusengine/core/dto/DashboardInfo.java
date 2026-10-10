package com.nexusengine.core.dto;

import java.math.BigDecimal;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
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
