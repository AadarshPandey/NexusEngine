import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Skeleton,
  Chip,
  alpha,
  Button,
  Alert,
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
  Refresh,
  ArrowForward,
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

// Stat Card Component
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
      position: 'relative',
      overflow: 'hidden',
      border: '1px solid',
      borderColor: 'divider',
      bgcolor: 'background.paper',
      transition: 'transform 0.15s ease, box-shadow 0.15s ease',
      '&:hover': {
        transform: 'translateY(-2px)',
        boxShadow: `0 6px 20px ${alpha(color, 0.15)}`,
      },
    }}
  >
    <CardContent sx={{ p: 2.5, '&:last-child': { pb: 2.5 } }}>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1.5 }}>
        <Typography
          variant="subtitle2"
          sx={{
            color: 'text.secondary',
            fontWeight: 600,
            fontSize: '0.8125rem',
          }}
        >
          {title}
        </Typography>
        <Box
          sx={{
            width: 38,
            height: 38,
            borderRadius: 2,
            bgcolor: alpha(color, 0.12),
            color: color,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          {React.cloneElement(icon as React.ReactElement<any>, {
            style: { fontSize: 20 },
          })}
        </Box>
      </Box>

      <Typography variant="h4" sx={{ fontWeight: 700, color: 'text.primary', mb: 0.75, letterSpacing: '-0.02em' }}>
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
          <Typography sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
            vs last period
          </Typography>
        </Box>
      )}
    </CardContent>
  </Card>
);

// Pending Item Component
interface PendingItemProps {
  label: string;
  count: number;
  icon: React.ReactNode;
  color?: string;
  onClick?: () => void;
}

const PendingItem: React.FC<PendingItemProps> = ({ label, count, icon, color = '#6366F1', onClick }) => (
  <Box
    onClick={onClick}
    sx={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      p: 1.5,
      borderRadius: 1.5,
      border: '1px solid',
      borderColor: 'divider',
      bgcolor: 'background.default',
      cursor: onClick ? 'pointer' : 'default',
      transition: 'background-color 0.15s, border-color 0.15s',
      '&:hover': onClick
        ? {
            bgcolor: 'action.hover',
            borderColor: 'text.secondary',
          }
        : undefined,
    }}
  >
    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
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
          bgcolor: count > 0 ? alpha(color, 0.12) : 'action.hover',
          color: count > 0 ? color : 'text.secondary',
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
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, pt: 1 }}>
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr', lg: 'repeat(4, 1fr)' }, gap: 2.5 }}>
          {[...Array(4)].map((_, i) => (
            <Skeleton key={i} variant="rectangular" height={120} sx={{ borderRadius: 2 }} />
          ))}
        </Box>
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', lg: '2fr 1fr' }, gap: 2.5 }}>
          <Skeleton variant="rectangular" height={380} sx={{ borderRadius: 2 }} />
          <Skeleton variant="rectangular" height={380} sx={{ borderRadius: 2 }} />
        </Box>
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

  return (
    <Box sx={{ pb: 3 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 3 }}>
        <Box>
          <Typography variant="h5" sx={{ fontWeight: 700, color: 'text.primary', letterSpacing: '-0.02em' }}>
            Dashboard Overview
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary', mt: 0.25 }}>
            Real-time business performance and operational queue status
          </Typography>
        </Box>
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

      {/* KPI Cards Grid */}
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr', lg: 'repeat(4, 1fr)' }, gap: 2.5, mb: 3 }}>
        <StatCard
          title="Today's Revenue"
          value={`₹${dashboardData.todayRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
          icon={<AttachMoney />}
          color="#10B981"
          trend="+8.3%"
          trendUp={true}
        />
        <StatCard
          title="Today's Orders"
          value={dashboardData.todayOrders.toLocaleString()}
          icon={<ShoppingCart />}
          color="#6366F1"
          trend="+12.5%"
          trendUp={true}
        />
        <StatCard
          title="Total Products"
          value={dashboardData.totalProducts.toLocaleString()}
          icon={<Inventory2 />}
          color="#0EA5E9"
        />
        <StatCard
          title="Total Customers"
          value={dashboardData.userOverview.totalMembers.toLocaleString()}
          icon={<People />}
          color="#EC4899"
        />
      </Box>

      {/* Main 2-Column Split: Chart & Operational Queue */}
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', lg: '2fr 1fr' }, gap: 2.5 }}>
        {/* Left Column: Order Statistics Chart */}
        <Card sx={{ border: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
          <CardContent sx={{ p: 3 }}>
            <Box sx={{ mb: 2 }}>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>
                Order Statistics
              </Typography>
              <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                14-day order volume and revenue trends
              </Typography>
            </Box>

            <Box sx={{ height: 320 }}>
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
          </CardContent>
        </Card>

        {/* Right Column: Pending Tasks */}
        <Card sx={{ border: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
          <CardContent sx={{ p: 3 }}>
            <Box sx={{ mb: 2 }}>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>
                Pending Tasks
              </Typography>
              <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                Operational items requiring attention
              </Typography>
            </Box>

            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.25 }}>
              <PendingItem
                label="Awaiting Shipment"
                count={dashboardData.pendingTasks.awaitingShipment}
                icon={<LocalShipping />}
                color="#F59E0B"
                onClick={() => navigate('/ordering/orders')}
              />
              <PendingItem
                label="Shipped Orders"
                count={dashboardData.pendingTasks.shippedOrders}
                icon={<LocalShipping />}
                color="#6366F1"
                onClick={() => navigate('/ordering/deliveries')}
              />
              <PendingItem
                label="Completed Orders"
                count={dashboardData.pendingTasks.completedOrders}
                icon={<ShoppingCart />}
                color="#10B981"
                onClick={() => navigate('/ordering/orders')}
              />
              <PendingItem
                label="Pending Returns"
                count={dashboardData.pendingTasks.pendingReturns}
                icon={<AssignmentReturn />}
                color="#EF4444"
                onClick={() => navigate('/ordering/return-requests')}
              />
              <PendingItem
                label="Awaiting Confirmation"
                count={dashboardData.pendingTasks.awaitingConfirmation}
                icon={<Receipt />}
                color="#6366F1"
                onClick={() => navigate('/ordering/orders')}
              />
            </Box>
          </CardContent>
        </Card>
      </Box>
    </Box>
  );
};

export default Dashboard;
