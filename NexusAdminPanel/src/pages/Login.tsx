import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  InputAdornment,
  IconButton,
  CircularProgress,
  Alert,
} from '@mui/material';
import { Person, Lock, Visibility, VisibilityOff } from '@mui/icons-material';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { userLogin } from '@/store/slices/userSlice';

const Login: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const savedUsername = useAppSelector((state) => state.user.userInfo.username);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    setUsername(savedUsername || 'admin');
  }, [savedUsername]);

  const handleLogin = async (e?: React.FormEvent) => {
    e?.preventDefault();
    setError('');

    if (!username.trim()) {
      setError('Please enter a valid username');
      return;
    }
    if (password.length < 3) {
      setError('Password must be at least 3 characters');
      return;
    }

    setLoading(true);
    try {
      await dispatch(
        userLogin({ username: username.trim(), password }),
      ).unwrap();
      navigate('/');
    } catch (err: any) {
      setError(err?.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #0F172A 0%, #1E293B 50%, #334155 100%)',
        position: 'relative',
        overflow: 'hidden',
      }}
    >
      {/* Decorative gradient orbs */}
      <Box
        sx={{
          position: 'absolute',
          width: 400,
          height: 400,
          borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(99, 102, 241, 0.15) 0%, transparent 70%)',
          top: -100,
          right: -100,
        }}
      />
      <Box
        sx={{
          position: 'absolute',
          width: 300,
          height: 300,
          borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(236, 72, 153, 0.1) 0%, transparent 70%)',
          bottom: -50,
          left: -50,
        }}
      />

      <Card
        sx={{
          width: 420,
          maxWidth: '90vw',
          borderRadius: 4,
          boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)',
          border: '1px solid rgba(255, 255, 255, 0.06)',
          backdropFilter: 'blur(20px)',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          position: 'relative',
          overflow: 'visible',
          '&::before': {
            content: '""',
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            height: 4,
            background: 'linear-gradient(90deg, #6366F1, #EC4899, #6366F1)',
            borderRadius: '16px 16px 0 0',
          },
        }}
      >
        <CardContent sx={{ p: 5 }}>
          {/* Logo */}
          <Box sx={{ textAlign: 'center', mb: 4 }}>
            <Box
              sx={{
                width: 56,
                height: 56,
                borderRadius: 3,
                background: 'linear-gradient(135deg, #6366F1 0%, #818CF8 100%)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                mx: 'auto',
                mb: 2,
                boxShadow: '0 8px 24px rgba(99, 102, 241, 0.35)',
              }}
            >
              <Typography variant="h5" sx={{ color: '#fff', fontWeight: 800 }}>
                N
              </Typography>
            </Box>
            <Typography
              variant="h5"
              sx={{
                fontWeight: 700,
                background: 'linear-gradient(135deg, #6366F1, #EC4899)',
                backgroundClip: 'text',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              Nexus Admin Panel
            </Typography>
            <Typography variant="body2" sx={{ color: '#64748B', mt: 0.5 }}>
              Sign in to your admin account
            </Typography>
          </Box>

          {error && (
            <Alert severity="error" sx={{ mb: 3, borderRadius: 2 }}>
              {error}
            </Alert>
          )}

          <form onSubmit={handleLogin}>
            <TextField
              fullWidth
              label="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              sx={{ mb: 2.5 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Person sx={{ color: '#94A3B8', fontSize: 20 }} />
                  </InputAdornment>
                ),
              }}
            />
            <TextField
              fullWidth
              label="Password"
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
              sx={{ mb: 3 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Lock sx={{ color: '#94A3B8', fontSize: 20 }} />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                      size="small"
                    >
                      {showPassword ? (
                        <VisibilityOff sx={{ fontSize: 18 }} />
                      ) : (
                        <Visibility sx={{ fontSize: 18 }} />
                      )}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <Button
              fullWidth
              variant="contained"
              size="large"
              type="submit"
              disabled={loading}
              sx={{
                py: 1.5,
                fontSize: '0.9375rem',
                fontWeight: 600,
                background: 'linear-gradient(135deg, #6366F1 0%, #818CF8 100%)',
                boxShadow: '0 8px 24px rgba(99, 102, 241, 0.35)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #4F46E5 0%, #6366F1 100%)',
                  boxShadow: '0 12px 28px rgba(99, 102, 241, 0.45)',
                },
                '&:disabled': {
                  background: '#E2E8F0',
                },
              }}
            >
              {loading ? (
                <CircularProgress size={24} sx={{ color: '#fff' }} />
              ) : (
                'Sign In'
              )}
            </Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Login;
