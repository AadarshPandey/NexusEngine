import React from 'react';
import { useNavigate } from 'react-router';
import { Box, Typography, Button } from '@mui/material';
import { Home } from '@mui/icons-material';

const NotFound: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #F8FAFC 0%, #E2E8F0 100%)',
      }}
    >
      <Typography
        variant="h1"
        sx={{
          fontSize: '8rem',
          fontWeight: 800,
          background: 'linear-gradient(135deg, #6366F1, #EC4899)',
          backgroundClip: 'text',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          lineHeight: 1,
          mb: 2,
        }}
      >
        404
      </Typography>
      <Typography variant="h5" sx={{ color: '#64748B', mb: 1, fontWeight: 600 }}>
        Page Not Found
      </Typography>
      <Typography variant="body1" sx={{ color: '#94A3B8', mb: 4, textAlign: 'center', maxWidth: 400 }}>
        The page you are looking for doesn't exist or has been moved.
      </Typography>
      <Button
        variant="contained"
        startIcon={<Home />}
        onClick={() => navigate('/home')}
        sx={{
          background: 'linear-gradient(135deg, #6366F1, #818CF8)',
          px: 4,
          py: 1.5,
        }}
      >
        Back to Dashboard
      </Button>
    </Box>
  );
};

export default NotFound;
