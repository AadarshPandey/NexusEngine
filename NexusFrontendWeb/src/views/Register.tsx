import React, { useState } from 'react';
import { Box, TextField, Button, Typography, Paper, Alert } from '@mui/material';
import { useNavigate, Link } from 'react-router';
import request from '../utils/request';

const Register: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [authCode, setAuthCode] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [gettingCode, setGettingCode] = useState(false);
  const [countdown, setCountdown] = useState(0);
  const navigate = useNavigate();

  React.useEffect(() => {
    let timer: NodeJS.Timeout;
    if (countdown > 0) {
      timer = setTimeout(() => setCountdown(countdown - 1), 1000);
    }
    return () => clearTimeout(timer);
  }, [countdown]);

  const handleGetAuthCode = async () => {
    if (!email) {
      setError('Please enter an email address to get the auth code.');
      return;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setError('Please enter a valid email address.');
      return;
    }
    try {
      setGettingCode(true);
      setError('');
      await request.get('/sso/getAuthCode', { params: { email } });
      setSuccess('OTP sent successfully. Please check your email inbox.');
      setCountdown(60);
    } catch (err: any) {
      setError(err.message || 'Failed to get auth code.');
    } finally {
      setGettingCode(false);
    }
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !password || !email || !authCode) {
      setError('Please fill in all fields.');
      return;
    }
    if (password.length < 6) {
      setError('Password must be at least 6 characters long.');
      return;
    }
    try {
      const params = new URLSearchParams();
      params.append('username', username);
      params.append('password', password);
      params.append('email', email);
      params.append('authCode', authCode);
      await request.post('/sso/register', params);
      
      setSuccess('Registration successful! You can now log in.');
      setError('');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err: any) {
      let errorMessage = err.message;
      if (errorMessage === 'Success') {
        errorMessage = 'Registration failed.';
      }
      setError(errorMessage || 'Registration failed. Please try again.');
    }
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}>
      <Paper sx={{ p: 4, width: '100%', maxWidth: 400 }}>
        <Typography variant="h5" component="h1" gutterBottom align="center">
          Sign Up
        </Typography>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}
        <form onSubmit={handleRegister}>
          <TextField
            fullWidth
            label="Username"
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            fullWidth
            label="Password"
            type="password"
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <TextField
            fullWidth
            label="Email Address"
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <Box sx={{ display: 'flex', gap: 1, mt: 2, mb: 1 }}>
            <TextField
              fullWidth
              label="Auth Code"
              value={authCode}
              onChange={(e) => setAuthCode(e.target.value)}
            />
            <Button 
              variant="outlined" 
              onClick={handleGetAuthCode} 
              disabled={gettingCode || !email || countdown > 0}
              sx={{ whiteSpace: 'nowrap', minWidth: '120px' }}
            >
              {countdown > 0 ? `Resend (${countdown}s)` : 'Get Code'}
            </Button>
          </Box>
          <Button fullWidth type="submit" variant="contained" color="primary" sx={{ mt: 3, mb: 2 }}>
            Sign Up
          </Button>
          <Box sx={{ textAlign: 'center' }}>
            <Typography variant="body2">
              Already have an account? <Link to="/login">Login here</Link>
            </Typography>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default Register;
