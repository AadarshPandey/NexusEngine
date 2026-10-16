/**
 * Analytics Bounded Context Type Definitions
 */

export interface DashboardInfo {
  todayOrders: number
  todayRevenue: number
  yesterdayRevenue: number
  totalProducts: number
  chartData: Array<{
    date: string
    orderCount: number
    orderAmount: number
  }>
  pendingTasks: {
    awaitingPayment: number
    awaitingShipment: number
    shippedOrders: number
    completedOrders: number
    awaitingConfirmation: number
    pendingReturns: number
    pendingRefunds: number
    outOfStockItems: number
    expiringAds: number
  }
  productOverview: {
    unlisted: number
    listed: number
    lowStockThreshold: number
    total: number
  }
  userOverview: {
    todayNew: number
    yesterdayNew: number
    thisMonth: number
    totalMembers: number
  }
  monthlySummary: {
    monthlyOrders: number
    monthlyOrdersTrend: string
    monthlyOrdersTrendUp: boolean
    weeklyOrders: number
    weeklyOrdersTrend: string
    weeklyOrdersTrendUp: boolean
    monthlyRevenue: number
    monthlyRevenueTrend: string
    monthlyRevenueTrendUp: boolean
    weeklyRevenue: number
    weeklyRevenueTrend: string
    weeklyRevenueTrendUp: boolean
  }
}
