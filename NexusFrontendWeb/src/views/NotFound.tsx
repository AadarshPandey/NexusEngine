import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Link } from 'react-router';

const NotFound: React.FC = () => {
  return (
    <Box sx={{ textAlign: 'center', mt: 8 }}>
      <Typography variant="h1" color="primary">404</Typography>
      <Typography variant="h5" sx={{ mb: 3 }}>Page Not Found</Typography>
      <Button variant="contained" component={Link} to="/">Go Home</Button>
    </Box>
  );
};

export default NotFound;
