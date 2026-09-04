import React, { useState } from 'react';
import { Box, TextField, Button, Typography, Paper, Alert } from '@mui/material';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import { login } from '../store/slices/authSlice';
import request from '../utils/request';

const Login: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const params = new URLSearchParams();
      params.append('username', username);
      params.append('password', password);
      const response = await request.post('/sso/login', params);
      if (response.data?.token) {
        dispatch(login({ token: response.data.token, user: { username } }));
        navigate('/');
      }
    } catch (err: any) {
      let errorMessage = err.message;
      if (errorMessage === 'Success') {
        errorMessage = 'Invalid username or password.';
      }
      setError(errorMessage || 'Login failed. Please check your credentials.');
    }
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}>
      <Paper sx={{ p: 4, width: '100%', maxWidth: 400 }}>
        <Typography variant="h5" component="h1" gutterBottom align="center">
          Customer Login
        </Typography>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <form onSubmit={handleLogin}>
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
          <Button fullWidth type="submit" variant="contained" color="primary" sx={{ mt: 3 }}>
            Sign In
          </Button>
        </form>
      </Paper>
    </Box>
  );
};

export default Login;
