import React from 'react';
import { Typography, Box, Divider } from '@mui/material';

interface SectionHeaderProps {
  title: string;
  subtitle?: string;
}

const SectionHeader: React.FC<SectionHeaderProps> = ({ title, subtitle }) => {
  return (
    <Box sx={{ my: 4, textAlign: 'center' }}>
      <Typography variant="h4" component="h2" sx={{ fontWeight: 'bold', mb: 1 }}>
        {title}
      </Typography>
      {subtitle && (
        <Typography variant="subtitle1" color="text.secondary" gutterBottom>
          {subtitle}
        </Typography>
      )}
      <Divider sx={{ width: 100, mx: 'auto', mt: 2, borderBottomWidth: 3, borderColor: 'primary.main' }} />
    </Box>
  );
};

export default SectionHeader;
