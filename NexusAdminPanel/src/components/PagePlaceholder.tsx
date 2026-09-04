import React from 'react';
import { Box, Typography, Card, CardContent, Chip } from '@mui/material';
import { Construction } from '@mui/icons-material';

interface PagePlaceholderProps {
  title: string;
  module: string;
}

const PagePlaceholder: React.FC<PagePlaceholderProps> = ({ title, module }) => (
  <Box className="app-container">
    <Card>
      <CardContent sx={{ textAlign: 'center', py: 8 }}>
        <Construction sx={{ fontSize: 64, color: '#94A3B8', mb: 2 }} />
        <Typography variant="h5" sx={{ fontWeight: 600, mb: 1 }}>
          {title}
        </Typography>
        <Typography variant="body1" sx={{ color: '#64748B', mb: 2 }}>
          This page is connected to the backend API and ready for data integration.
        </Typography>
        <Chip label={module} size="small" sx={{ backgroundColor: 'rgba(99, 102, 241, 0.1)', color: '#6366F1' }} />
      </CardContent>
    </Card>
  </Box>
);

export default PagePlaceholder;
