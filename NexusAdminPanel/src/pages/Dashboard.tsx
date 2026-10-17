import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Grid,
  Typography,
  Skeleton,
  Chip,
  alpha,
  Button,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Divider,
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
  Refresh,
  Security,
  ArrowForward,
  SyncAlt,
} from '@mui/icons-material';
import {
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Area,
  AreaChart,
  Legend,
} from 'recharts';

import { getDashboardInfoAPI, type DashboardInfo } from '@/apis/analytics';

// Premium Stat Card Component
interface StatCardProps {
  title: string;
  value: string;
  icon: React.ReactNode;
  color: string;
  trend?: string;
  trendUp?: boolean;
  subtext?: string;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, icon, color, trend, trendUp, subtext }) => (
  <Card
    sx={{
      height: '100%',
      position: 'relative',
      overflow: 'hidden',
      border: '1px solid',
      borderColor: 'divider',
      bgcolor: 'background.paper',
      transition: 'transform 0.2s, box-shadow 0.2s',
      '&:hover': {
        transform: 'translateY(-2px)',
        boxShadow: `0 8px 24px ${alpha(color, 0.15)}`,
      },
      '&::after': {
        content: '""',
        position: 'absolute',
        top: 0,
        right: 0,
        width: 100,
        height: 100,
        background: `radial-gradient(circle, ${alpha(color, 0.14)} 0%, transparent 70%)`,
        borderRadius: '0 0 0 100px',
      },
    }}
  >
    <CardContent sx={{ p: 2.5, '&:last-child': { pb: 2.5 } }}>
      <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', mb: 1.5 }}>
        <Typography
          variant="subtitle2"
          sx={{
            color: 'text.secondary',
            fontWeight: 600,
            fontSize: '0.8125rem',
            textTransform: 'uppercase',
            letterSpacing: '0.04em',
          }}
        >
          {title}
        </Typography>
        <Box
          sx={{
            width: 40,
            height: 40,
            borderRadius: 2.5,
            background: `linear-gradient(135deg, ${color}, ${alpha(color, 0.7)})`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: `0 4px 12px ${alpha(color, 0.3)}`,
          }}
        >
          {React.cloneElement(icon as React.ReactElement<any>, {
            style: { color: '#fff', fontSize: 20 },
          })}
        </Box>
      </Box>

      <Typography variant="h4" sx={{ fontWeight: 700, color: 'text.primary', mb: 1, letterSpacing: '-0.02em' }}>
        {value}
      </Typography>

      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 1 }}>
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
                fontWeight: 700,
                color: trendUp ? '#10B981' : '#EF4444',
              }}
            >
              {trend}
            </Typography>
            <Typography sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
              vs prior
            </Typography>
          </Box>
        )}
        {subtext && (
          <Typography sx={{ fontSize: '0.75rem', color: 'text.secondary', fontWeight: 500 }}>
            {subtext}
          </Typography>
        )}
      </Box>
    </CardContent>
  </Card>
);

// Operational Queue Action Item
interface ActionQueueItemProps {
  label: string;
  count: number;
  icon: React.ReactNode;
  severity: 'default' | 'primary' | 'warning' | 'error' | 'success';
  onClick?: () => void;
}

const ActionQueueItem: React.FC<ActionQueueItemProps> = ({ label, count, icon, severity, onClick }) => {
  const colorMap = {
    default: { bg: 'action.hover', text: 'text.secondary', border: 'transparent' },
    primary: { bg: alpha('#6366F1', 0.12), text: '#818CF8', border: alpha('#6366F1', 0.25) },
    warning: { bg: alpha('#F59E0B', 0.12), text: '#FBBF24', border: alpha('#F59E0B', 0.25) },
    error: { bg: alpha('#EF4444', 0.12), text: '#F87171', border: alpha('#EF4444', 0.25) },
    success: { bg: alpha('#10B981', 0.12), text: '#34D399', border: alpha('#10B981', 0.25) },
  };

  const style = colorMap[severity];

  return (
    <Box
      onClick={onClick}
      sx={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        p: 1.25,
        px: 1.5,
        borderRadius: 1.5,
        border: '1px solid',
        borderColor: 'divider',
        bgcolor: 'background.default',
        cursor: onClick ? 'pointer' : 'default',
        transition: 'all 0.15s ease',
        '&:hover': onClick
          ? {
              bgcolor: 'action.hover',
              borderColor: 'text.secondary',
              transform: 'translateX(2px)',
            }
          : undefined,
      }}
    >
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.25 }}>
        {React.cloneElement(icon as React.ReactElement<any>, {
          style: { fontSize: 18, color: 'var(--text-secondary, #94a3b8)' },
        })}
        <Typography sx={{ fontSize: '0.8125rem', fontWeight: 500, color: 'text.primary' }}>
          {label}
        </Typography>
      </Box>

      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <Chip
          label={count}
          size="small"
          sx={{
            bgcolor: style.bg,
            color: style.text,
            border: `1px solid ${style.border}`,
            fontWeight: 700,
            fontSize: '0.75rem',
            height: 22,
            minWidth: 28,
          }}
        />
        {onClick && <ArrowForward sx={{ fontSize: 14, color: 'text.secondary' }} />}
      </Box>
    </Box>
  );
};

const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const [dashboardData, setDashboardData] = useState<DashboardInfo | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchDashboardData = () => {
    setLoading(true);
    setError(null);
    getDashboardInfoAPI()
      .then((data) => {
        if (data && data.chartData) {
          data.chartData = [...data.chartData].reverse();
        }
        setDashboardData(data);
        setLoading(false);
      })
      .catch((err: any) => {
        console.error('Failed to load dashboard data:', err);
        setError(err?.message || 'Failed to load dashboard data. Please check backend connection.');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2.5, pt: 2 }}>
        <Skeleton variant="rectangular" height={50} sx={{ borderRadius: 2 }} />
        <Grid container spacing={2.5}>
          {[...Array(4)].map((_, i) => (
            <Grid size={{ xs: 12, sm: 6, lg: 3 }} key={i}>
              <Skeleton variant="rectangular" height={130} sx={{ borderRadius: 2 }} />
            </Grid>
          ))}
        </Grid>
        <Grid container spacing={2.5}>
          <Grid size={{ xs: 12, lg: 8 }}>
            <Skeleton variant="rectangular" height={400} sx={{ borderRadius: 2 }} />
          </Grid>
          <Grid size={{ xs: 12, lg: 4 }}>
            <Skeleton variant="rectangular" height={400} sx={{ borderRadius: 2 }} />
          </Grid>
        </Grid>
      </Box>
    );
  }

  if (error || !dashboardData) {
    return (
      <Box sx={{ pt: 4, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 3 }}>
        <Alert severity="error" sx={{ width: '100%', maxWidth: 600 }}>
          {error || 'Unable to load dashboard information. Please try again.'}
        </Alert>
        <Button variant="contained" onClick={fetchDashboardData} sx={{ bgcolor: '#6366F1' }}>
          Retry Loading Dashboard
        </Button>
      </Box>
    );
  }

  const pendingAttentionCount =
    (dashboardData.pendingTasks.awaitingShipment || 0) +
    (dashboardData.pendingTasks.pendingReturns || 0) +
    (dashboardData.pendingTasks.outOfStockItems || 0);

  return (
    <Box sx={{ pb: 3 }}>
      {/* Executive Command Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <Typography variant="h5" sx={{ fontWeight: 700, color: 'text.primary', letterSpacing: '-0.02em' }}>
              Commerce & Operations Command Center
            </Typography>
            <Chip
              icon={<Box sx={{ width: 8, height: 8, borderRadius: '50%', bgcolor: '#10B981', ml: 1 }} />}
              label="System Operational"
              size="small"
              sx={{
                bgcolor: alpha('#10B981', 0.1),
                color: '#10B981',
                fontWeight: 600,
                border: `1px solid ${alpha('#10B981', 0.25)}`,
              }}
            />
          </Box>
          <Typography variant="body2" sx={{ color: 'text.secondary', mt: 0.5 }}>
            Real-time multi-tenant transaction monitoring, operational fulfillment queues, and transactional delivery telemetry.
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
          <Chip
            label="Multi-Tenant Isolation: Active"
            size="small"
            variant="outlined"
            sx={{ borderColor: 'divider', color: 'text.secondary', fontWeight: 500 }}
          />
          <Button
            variant="outlined"
            size="small"
            startIcon={<Refresh />}
            onClick={fetchDashboardData}
            sx={{ borderColor: 'divider', color: 'text.primary', textTransform: 'none', fontWeight: 600 }}
          >
            Refresh
          </Button>
        </Box>
      </Box>

      {/* Row 1: Executive KPI Cards */}
      <Grid container spacing={2.5} sx={{ mb: 2.5 }}>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Today's Gross Revenue"
            value={`₹${dashboardData.todayRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
            icon={<AttachMoney />}
            color="#10B981"
            trend="+8.3%"
            trendUp={true}
            subtext={`Yesterday: ₹${dashboardData.yesterdayRevenue.toLocaleString(undefined, { minimumFractionDigits: 0 })}`}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Today's Orders"
            value={dashboardData.todayOrders.toLocaleString()}
            icon={<ShoppingCart />}
            color="#6366F1"
            trend="+12.5%"
            trendUp={true}
            subtext={`Weekly: ${dashboardData.monthlySummary.weeklyOrders} orders`}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Active Catalog SKUs"
            value={dashboardData.totalProducts.toLocaleString()}
            icon={<Inventory2 />}
            color="#0EA5E9"
            subtext={`${dashboardData.productOverview.listed} Listed • ${dashboardData.productOverview.lowStockThreshold} Low Stock`}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Customer Base"
            value={dashboardData.userOverview.totalMembers.toLocaleString()}
            icon={<People />}
            color="#EC4899"
            subtext={`+${dashboardData.userOverview.thisMonth} new this month`}
          />
        </Grid>
      </Grid>

      {/* Row 2: Analytics & Operational Queues */}
      <Grid container spacing={2.5} sx={{ mb: 2.5 }}>
        {/* Left: Revenue & Order Trajectory + Performance Matrix Strip */}
        <Grid size={{ xs: 12, lg: 8 }}>
          <Card sx={{ border: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
            <CardContent sx={{ p: 3, pb: 2.5 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2.5 }}>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    Transaction Dynamics & Velocity
                  </Typography>
                  <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                    14-day rolling order minQuantity and realized revenue trends
                  </Typography>
                </Box>
              </Box>

              <Box sx={{ height: 300 }}>
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={dashboardData.chartData}>
                    <defs>
                      <linearGradient id="colorOrders" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#6366F1" stopOpacity={0.25} />
                        <stop offset="95%" stopColor="#6366F1" stopOpacity={0} />
                      </linearGradient>
                      <linearGradient id="colorAmount" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#10B981" stopOpacity={0.25} />
                        <stop offset="95%" stopColor="#10B981" stopOpacity={0} />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                    <XAxis
                      dataKey="date"
                      tick={{ fontSize: 12, fill: '#94a3b8' }}
                      tickLine={false}
                      axisLine={{ stroke: '#334155' }}
                    />
                    <YAxis
                      yAxisId="left"
                      tick={{ fontSize: 12, fill: '#94a3b8' }}
                      tickLine={false}
                      axisLine={false}
                    />
                    <YAxis
                      yAxisId="right"
                      orientation="right"
                      tick={{ fontSize: 12, fill: '#94a3b8' }}
                      tickLine={false}
                      axisLine={false}
                    />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: '#1e293b',
                        borderRadius: 8,
                        border: '1px solid #334155',
                        boxShadow: '0 8px 24px rgba(0,0,0,0.5)',
                        fontSize: 13,
                        color: '#f8fafc',
                      }}
                    />
                    <Legend wrapperStyle={{ fontSize: 13, paddingTop: 10 }} />
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
              </Box>

              {/* Integrated Performance Matrix Strip */}
              <Divider sx={{ my: 2 }} />
              <Grid container spacing={2}>
                {[
                  {
                    label: 'Weekly Orders',
                    value: dashboardData.monthlySummary.weeklyOrders,
                    trend: dashboardData.monthlySummary.weeklyOrdersTrend,
                    up: dashboardData.monthlySummary.weeklyOrdersTrendUp,
                  },
                  {
                    label: 'Monthly Orders',
                    value: dashboardData.monthlySummary.monthlyOrders,
                    trend: dashboardData.monthlySummary.monthlyOrdersTrend,
                    up: dashboardData.monthlySummary.monthlyOrdersTrendUp,
                  },
                  {
                    label: 'Weekly Revenue',
                    value: `₹${dashboardData.monthlySummary.weeklyRevenue.toLocaleString(undefined, { minimumFractionDigits: 0 })}`,
                    trend: dashboardData.monthlySummary.weeklyRevenueTrend,
                    up: dashboardData.monthlySummary.weeklyRevenueTrendUp,
                  },
                  {
                    label: 'Monthly Revenue',
                    value: `₹${dashboardData.monthlySummary.monthlyRevenue.toLocaleString(undefined, { minimumFractionDigits: 0 })}`,
                    trend: dashboardData.monthlySummary.monthlyRevenueTrend,
                    up: dashboardData.monthlySummary.monthlyRevenueTrendUp,
                  },
                ].map((item) => (
                  <Grid size={{ xs: 6, sm: 3 }} key={item.label}>
                    <Box sx={{ p: 1 }}>
                      <Typography sx={{ color: 'text.secondary', fontSize: '0.75rem', fontWeight: 500, mb: 0.5 }}>
                        {item.label}
                      </Typography>
                      <Typography variant="h6" sx={{ fontWeight: 700, color: 'text.primary', mb: 0.25 }}>
                        {item.value}
                      </Typography>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <Typography
                          sx={{
                            fontSize: '0.75rem',
                            fontWeight: 700,
                            color: item.up ? '#10B981' : '#EF4444',
                          }}
                        >
                          {item.trend}
                        </Typography>
                        <Typography sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                          vs prev
                        </Typography>
                      </Box>
                    </Box>
                  </Grid>
                ))}
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Right: Operational Action Center */}
        <Grid size={{ xs: 12, lg: 4 }}>
          <Card sx={{ height: '100%', border: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
            <CardContent sx={{ p: 3 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    Operational Queues
                  </Typography>
                  <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                    Action items needing attention
                  </Typography>
                </Box>
                {pendingAttentionCount > 0 && (
                  <Chip
                    label={`${pendingAttentionCount} pending`}
                    size="small"
                    color="warning"
                    sx={{ fontWeight: 700 }}
                  />
                )}
              </Box>

              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                <ActionQueueItem
                  label="Awaiting Shipment"
                  count={dashboardData.pendingTasks.awaitingShipment}
                  icon={<LocalShipping />}
                  severity={dashboardData.pendingTasks.awaitingShipment > 0 ? 'warning' : 'default'}
                  onClick={() => navigate('/ordering/orders')}
                />
                <ActionQueueItem
                  label="Shipped in Transit"
                  count={dashboardData.pendingTasks.shippedOrders}
                  icon={<LocalShipping />}
                  severity="primary"
                  onClick={() => navigate('/ordering/deliveries')}
                />
                <ActionQueueItem
                  label="Awaiting Confirmation"
                  count={dashboardData.pendingTasks.awaitingConfirmation}
                  icon={<Receipt />}
                  severity="default"
                  onClick={() => navigate('/ordering/orders')}
                />
                <ActionQueueItem
                  label="Completed Orders"
                  count={dashboardData.pendingTasks.completedOrders}
                  icon={<ShoppingCart />}
                  severity="success"
                  onClick={() => navigate('/ordering/orders')}
                />
                <ActionQueueItem
                  label="Pending Return Requests"
                  count={dashboardData.pendingTasks.pendingReturns}
                  icon={<AssignmentReturn />}
                  severity={dashboardData.pendingTasks.pendingReturns > 0 ? 'error' : 'default'}
                  onClick={() => navigate('/ordering/return-requests')}
                />
                <ActionQueueItem
                  label="Low Stock / Out of Stock"
                  count={dashboardData.pendingTasks.outOfStockItems}
                  icon={<NewReleases />}
                  severity={dashboardData.pendingTasks.outOfStockItems > 0 ? 'error' : 'default'}
                  onClick={() => navigate('/catalog/products')}
                />
                <ActionQueueItem
                  label="Expiring Advertisements"
                  count={dashboardData.pendingTasks.expiringAds}
                  icon={<Campaign />}
                  severity="default"
                  onClick={() => navigate('/promotion/banners')}
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Row 3: Platform Governance & Security Telemetry */}
      <Grid container spacing={2.5}>
        {/* Left: Transactional Outbox Delivery */}
        <Grid size={{ xs: 12, lg: 7 }}>
          <Card sx={{ height: '100%', border: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
            <CardContent sx={{ p: 3 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2, flexWrap: 'wrap', gap: 1 }}>
                <Box>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <SyncAlt sx={{ color: '#6366F1', fontSize: 20 }} />
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      Transactional Outbox Reliability
                    </Typography>
                  </Box>
                  <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                    Guaranteed at-least-once cross-context event publishing (messaging.outbox_event)
                  </Typography>
                </Box>
                {dashboardData.outboxMetrics && (
                  <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                    <Chip label={`Total: ${dashboardData.outboxMetrics.totalEvents}`} size="small" variant="outlined" />
                    <Chip label={`Published: ${dashboardData.outboxMetrics.publishedEvents}`} size="small" color="success" />
                    <Chip label={`Pending: ${dashboardData.outboxMetrics.pendingEvents}`} size="small" color="warning" />
                    <Chip label={`Failed: ${dashboardData.outboxMetrics.failedEvents}`} size="small" color="error" />
                  </Box>
                )}
              </Box>

              <TableContainer sx={{ maxHeight: 260, overflowY: 'auto' }}>
                <Table size="small" stickyHeader>
                  <TableHead>
                    <TableRow>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Aggregate</TableCell>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Event Type</TableCell>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Status</TableCell>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Retries</TableCell>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Created At</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {dashboardData.outboxMetrics?.recentEvents && dashboardData.outboxMetrics.recentEvents.length > 0 ? (
                      dashboardData.outboxMetrics.recentEvents.map((ev) => (
                        <TableRow key={ev.id} hover>
                          <TableCell>
                            <Typography variant="body2" sx={{ fontWeight: 600, fontSize: '0.8125rem' }}>
                              {ev.aggregateType} #{ev.aggregateId}
                            </Typography>
                          </TableCell>
                          <TableCell>
                            <Chip label={ev.eventType} size="small" sx={{ fontSize: '0.7rem', height: 20 }} />
                          </TableCell>
                          <TableCell>
                            <Chip
                              label={ev.status}
                              size="small"
                              color={ev.status === 'PUBLISHED' ? 'success' : ev.status === 'PENDING' ? 'warning' : 'error'}
                              sx={{ fontSize: '0.7rem', height: 20, fontWeight: 600 }}
                            />
                          </TableCell>
                          <TableCell sx={{ fontSize: '0.8125rem' }}>{ev.attemptCount}</TableCell>
                          <TableCell sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
                            {ev.createdAt ? new Date(ev.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }) : '-'}
                          </TableCell>
                        </TableRow>
                      ))
                    ) : (
                      <TableRow>
                        <TableCell colSpan={5} align="center" sx={{ py: 3, color: 'text.secondary' }}>
                          No outbox events recorded yet
                        </TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </TableContainer>
            </CardContent>
          </Card>
        </Grid>

        {/* Right: IAM Administrative Access Log */}
        <Grid size={{ xs: 12, lg: 5 }}>
          <Card sx={{ height: '100%', border: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
            <CardContent sx={{ p: 3 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
                <Security sx={{ color: '#0EA5E9', fontSize: 20 }} />
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  IAM Security & Console Access
                </Typography>
              </Box>
              <Typography variant="body2" sx={{ color: 'text.secondary', mb: 2 }}>
                Recent administrative session logins (iam.admin_login_event)
              </Typography>

              <TableContainer sx={{ maxHeight: 260, overflowY: 'auto' }}>
                <Table size="small" stickyHeader>
                  <TableHead>
                    <TableRow>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Admin User</TableCell>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Client IP</TableCell>
                      <TableCell sx={{ fontWeight: 600, bgcolor: 'background.paper' }}>Login Time</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {dashboardData.recentAdminLogins && dashboardData.recentAdminLogins.length > 0 ? (
                      dashboardData.recentAdminLogins.map((login) => (
                        <TableRow key={login.id} hover>
                          <TableCell>
                            <Box>
                              <Typography variant="body2" sx={{ fontWeight: 600, fontSize: '0.8125rem' }}>
                                {login.username}
                              </Typography>
                              <Typography variant="caption" sx={{ color: 'text.secondary' }}>
                                UID: {login.adminId}
                              </Typography>
                            </Box>
                          </TableCell>
                          <TableCell sx={{ fontSize: '0.75rem', fontFamily: 'monospace' }}>
                            {login.ipAddress || '127.0.0.1'}
                          </TableCell>
                          <TableCell sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
                            {login.occurredAt
                              ? new Date(login.occurredAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })
                              : '-'}
                          </TableCell>
                        </TableRow>
                      ))
                    ) : (
                      <TableRow>
                        <TableCell colSpan={3} align="center" sx={{ py: 3, color: 'text.secondary' }}>
                          No administrative login events
                        </TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </TableContainer>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
