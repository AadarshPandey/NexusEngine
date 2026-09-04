import React, { useState, useEffect, useMemo } from 'react';
import {
  Box,
  Card,
  CardContent,
  Grid,
  Typography,
  Skeleton,
  Chip,
  alpha,
} from '@mui/material';
import {
  ShoppingCart,
  AttachMoney,
  TrendingUp,
  TrendingDown,
  Inventory2,
  People,
  LocalShipping,
  AssignmentReturn,
  Receipt,
  NewReleases,
  Campaign,
} from '@mui/icons-material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Area,
  AreaChart,
  Legend,
} from 'recharts';

import { getDashboardInfoAPI, type DashboardInfo } from '@/apis/dashboard';

// Stat card component
interface StatCardProps {
  title: string;
  value: string;
  icon: React.ReactNode;
  color: string;
  trend?: string;
  trendUp?: boolean;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, icon, color, trend, trendUp }) => (
  <Card
    sx={{
      height: '100%',
      position: 'relative',
      overflow: 'hidden',
      '&::after': {
        content: '""',
        position: 'absolute',
        top: 0,
        right: 0,
        width: 80,
        height: 80,
        background: `radial-gradient(circle, ${alpha(color, 0.12)} 0%, transparent 70%)`,
        borderRadius: '0 0 0 80px',
      },
    }}
  >
    <CardContent sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="subtitle1" sx={{ color: '#94A3B8', fontSize: '0.8125rem', mb: 1 }}>
            {title}
          </Typography>
          <Typography variant="h4" sx={{ fontWeight: 700, color: '#1E293B', mb: 1 }}>
            {value}
          </Typography>
          {trend && (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
              {trendUp ? (
                <TrendingUp sx={{ fontSize: 16, color: '#10B981' }} />
              ) : (
                <TrendingDown sx={{ fontSize: 16, color: '#EF4444' }} />
              )}
              <Typography
                sx={{
                  fontSize: '0.75rem',
                  fontWeight: 600,
                  color: trendUp ? '#10B981' : '#EF4444',
                }}
              >
                {trend}
              </Typography>
              <Typography sx={{ fontSize: '0.75rem', color: '#94A3B8' }}>
                vs last period
              </Typography>
            </Box>
          )}
        </Box>
        <Box
          sx={{
            width: 48,
            height: 48,
            borderRadius: 3,
            background: `linear-gradient(135deg, ${color}, ${alpha(color, 0.7)})`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: `0 4px 14px ${alpha(color, 0.35)}`,
          }}
        >
          {React.cloneElement(icon as React.ReactElement, {
            sx: { color: '#fff', fontSize: 24 },
          })}
        </Box>
      </Box>
    </CardContent>
  </Card>
);

// Pending item
interface PendingItemProps {
  label: string;
  count: number;
  icon: React.ReactNode;
}

const PendingItem: React.FC<PendingItemProps> = ({ label, count, icon }) => (
  <Box
    sx={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      py: 1.5,
      px: 2,
      borderRadius: 2,
      '&:hover': { backgroundColor: '#F8FAFC' },
      transition: 'background-color 0.15s',
    }}
  >
    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
      {React.cloneElement(icon as React.ReactElement, {
        sx: { fontSize: 18, color: '#64748B' },
      })}
      <Typography sx={{ fontSize: '0.8125rem', color: '#475569' }}>{label}</Typography>
    </Box>
    <Chip
      label={count}
      size="small"
      sx={{
        backgroundColor: alpha('#EF4444', 0.1),
        color: '#EF4444',
        fontWeight: 600,
        fontSize: '0.75rem',
        height: 24,
      }}
    />
  </Box>
);

const Dashboard: React.FC = () => {
  const [dashboardData, setDashboardData] = useState<DashboardInfo | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getDashboardInfoAPI().then(res => {
      // Reverse chart data so oldest is first
      const data = res.data;
      if (data && data.chartData) {
        data.chartData = [...data.chartData].reverse();
      }
      setDashboardData(data);
      setLoading(false);
    }).catch(() => {
      setLoading(false);
    });
  }, []);

  if (loading || !dashboardData) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 4 }}>
        {[...Array(6)].map((_, i) => (
          <Skeleton key={i} variant="rectangular" height={100} sx={{ borderRadius: 2 }} />
        ))}
      </Box>
    );
  }

  return (
    <Box>
      {/* Stats Row */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Today's Orders"
            value={dashboardData.todayOrders.toLocaleString()}
            icon={<ShoppingCart />}
            color="#6366F1"
            trend="+12.5%"
            trendUp={true}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Today's Revenue"
            value={`₹${dashboardData.todayRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
            icon={<AttachMoney />}
            color="#10B981"
            trend="+8.3%"
            trendUp={true}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Yesterday's Revenue"
            value={`₹${dashboardData.yesterdayRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
            icon={<AttachMoney />}
            color="#F59E0B"
            trend="-3.1%"
            trendUp={false}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Total Products"
            value={dashboardData.totalProducts.toLocaleString()}
            icon={<Inventory2 />}
            color="#EC4899"
          />
        </Grid>
      </Grid>

      {/* Chart + Pending Tasks */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        {/* Chart */}
        <Grid size={{ xs: 12, lg: 8 }}>
          <Card>
            <CardContent sx={{ p: 3 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 3 }}>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    Order Statistics
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#94A3B8' }}>
                    Order count and revenue trends
                  </Typography>
                </Box>
              </Box>
              <Box sx={{ height: 360 }}>
                {loading ? (
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 4 }}>
                    {[...Array(6)].map((_, i) => (
                      <Skeleton key={i} variant="rectangular" height={40} sx={{ borderRadius: 1 }} />
                    ))}
                  </Box>
                ) : (
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={dashboardData.chartData}>
                      <defs>
                        <linearGradient id="colorOrders" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#6366F1" stopOpacity={0.15} />
                          <stop offset="95%" stopColor="#6366F1" stopOpacity={0} />
                        </linearGradient>
                        <linearGradient id="colorAmount" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#10B981" stopOpacity={0.15} />
                          <stop offset="95%" stopColor="#10B981" stopOpacity={0} />
                        </linearGradient>
                      </defs>
                      <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                      <XAxis
                        dataKey="date"
                        tick={{ fontSize: 12, fill: '#94A3B8' }}
                        tickLine={false}
                        axisLine={{ stroke: '#E2E8F0' }}
                      />
                      <YAxis
                        yAxisId="left"
                        tick={{ fontSize: 12, fill: '#94A3B8' }}
                        tickLine={false}
                        axisLine={false}
                      />
                      <YAxis
                        yAxisId="right"
                        orientation="right"
                        tick={{ fontSize: 12, fill: '#94A3B8' }}
                        tickLine={false}
                        axisLine={false}
                      />
                      <Tooltip
                        contentStyle={{
                          borderRadius: 12,
                          border: '1px solid #E2E8F0',
                          boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                          fontSize: 13,
                        }}
                      />
                      <Legend wrapperStyle={{ fontSize: 13 }} />
                      <Area
                        yAxisId="left"
                        type="monotone"
                        dataKey="orderCount"
                        name="Orders"
                        stroke="#6366F1"
                        strokeWidth={2}
                        fillOpacity={1}
                        fill="url(#colorOrders)"
                      />
                      <Area
                        yAxisId="right"
                        type="monotone"
                        dataKey="orderAmount"
                        name="Revenue (₹)"
                        stroke="#10B981"
                        strokeWidth={2}
                        fillOpacity={1}
                        fill="url(#colorAmount)"
                      />
                    </AreaChart>
                  </ResponsiveContainer>
                )}
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Pending Tasks */}
        <Grid size={{ xs: 12, lg: 4 }}>
          <Card sx={{ height: '100%' }}>
            <CardContent sx={{ p: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Pending Tasks
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
                <PendingItem label="Awaiting Payment" count={dashboardData.pendingTasks.awaitingPayment} icon={<Receipt />} />
                <PendingItem label="Awaiting Shipment" count={dashboardData.pendingTasks.awaitingShipment} icon={<LocalShipping />} />
                <PendingItem label="Shipped Orders" count={dashboardData.pendingTasks.shippedOrders} icon={<LocalShipping />} />
                <PendingItem label="Completed Orders" count={dashboardData.pendingTasks.completedOrders} icon={<ShoppingCart />} />
                <PendingItem label="Awaiting Confirmation" count={dashboardData.pendingTasks.awaitingConfirmation} icon={<ShoppingCart />} />
                <PendingItem label="Pending Returns" count={dashboardData.pendingTasks.pendingReturns} icon={<AssignmentReturn />} />
                <PendingItem label="Pending Refunds" count={dashboardData.pendingTasks.pendingRefunds} icon={<AttachMoney />} />
                <PendingItem label="Out of Stock Items" count={dashboardData.pendingTasks.outOfStockItems} icon={<NewReleases />} />
                <PendingItem label="Expiring Ads" count={dashboardData.pendingTasks.expiringAds} icon={<Campaign />} />
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Overview Row */}
      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card>
            <CardContent sx={{ p: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                Product Overview
              </Typography>
              <Grid container spacing={2}>
                {[
                  { label: 'Unlisted', value: dashboardData.productOverview.unlisted, color: '#EF4444' },
                  { label: 'Listed', value: dashboardData.productOverview.listed, color: '#10B981' },
                  { label: 'Low Stock', value: dashboardData.productOverview.lowStock, color: '#F59E0B' },
                  { label: 'Total', value: dashboardData.productOverview.total, color: '#6366F1' },
                ].map((item) => (
                  <Grid size={{ xs: 6, sm: 3 }} key={item.label}>
                    <Box sx={{ textAlign: 'center' }}>
                      <Typography
                        variant="h4"
                        sx={{ fontWeight: 700, color: item.color, mb: 0.5 }}
                      >
                        {item.value}
                      </Typography>
                      <Typography sx={{ fontSize: '0.8125rem', color: '#64748B', fontWeight: 500 }}>
                        {item.label}
                      </Typography>
                    </Box>
                  </Grid>
                ))}
              </Grid>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card>
            <CardContent sx={{ p: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                User Overview
              </Typography>
              <Grid container spacing={2}>
                {[
                  { label: 'Today New', value: dashboardData.userOverview.todayNew, color: '#EF4444' },
                  { label: 'Yesterday New', value: dashboardData.userOverview.yesterdayNew, color: '#F59E0B' },
                  { label: 'This Month', value: dashboardData.userOverview.thisMonth, color: '#10B981' },
                  { label: 'Total Members', value: dashboardData.userOverview.totalMembers, color: '#6366F1' },
                ].map((item) => (
                  <Grid size={{ xs: 6, sm: 3 }} key={item.label}>
                    <Box sx={{ textAlign: 'center' }}>
                      <Typography
                        variant="h4"
                        sx={{ fontWeight: 700, color: item.color, mb: 0.5 }}
                      >
                        {item.value}
                      </Typography>
                      <Typography sx={{ fontSize: '0.8125rem', color: '#64748B', fontWeight: 500 }}>
                        {item.label}
                      </Typography>
                    </Box>
                  </Grid>
                ))}
              </Grid>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Monthly stats */}
      <Grid container spacing={3} sx={{ mt: 0 }}>
        <Grid size={{ xs: 12 }}>
          <Card>
            <CardContent sx={{ p: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                Monthly Summary
              </Typography>
              <Grid container spacing={3}>
                {[
                  { label: 'Monthly Orders', value: dashboardData.monthlySummary.monthlyOrders, trend: dashboardData.monthlySummary.monthlyOrdersTrend, up: dashboardData.monthlySummary.monthlyOrdersTrendUp },
                  { label: 'Weekly Orders', value: dashboardData.monthlySummary.weeklyOrders, trend: dashboardData.monthlySummary.weeklyOrdersTrend, up: dashboardData.monthlySummary.weeklyOrdersTrendUp },
                  { label: 'Monthly Revenue', value: `₹${dashboardData.monthlySummary.monthlyRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`, trend: dashboardData.monthlySummary.monthlyRevenueTrend, up: dashboardData.monthlySummary.monthlyRevenueTrendUp },
                  { label: 'Weekly Revenue', value: `₹${dashboardData.monthlySummary.weeklyRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`, trend: dashboardData.monthlySummary.weeklyRevenueTrend, up: dashboardData.monthlySummary.weeklyRevenueTrendUp },
                ].map((item) => (
                  <Grid size={{ xs: 12, sm: 6, md: 3 }} key={item.label}>
                    <Box sx={{ p: 2 }}>
                      <Typography sx={{ color: '#94A3B8', fontSize: '0.8125rem', mb: 1 }}>
                        {item.label}
                      </Typography>
                      <Typography variant="h5" sx={{ fontWeight: 700, color: '#1E293B', mb: 1 }}>
                        {item.value}
                      </Typography>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <Typography
                          sx={{
                            fontSize: '0.8125rem',
                            fontWeight: 600,
                            color: item.up ? '#10B981' : '#EF4444',
                          }}
                        >
                          {item.trend}
                        </Typography>
                        <Typography sx={{ fontSize: '0.75rem', color: '#94A3B8' }}>
                          vs previous period
                        </Typography>
                      </Box>
                    </Box>
                  </Grid>
                ))}
              </Grid>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
