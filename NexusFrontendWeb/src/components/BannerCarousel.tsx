import React, { useState, useEffect } from 'react';
import { Box, IconButton, Typography } from '@mui/material';
import type { CmsSubject } from '../api/home';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

interface BannerCarouselProps {
  banners: CmsSubject[];
}

const BannerCarousel: React.FC<BannerCarouselProps> = ({ banners }) => {
  const [activeIndex, setActiveIndex] = useState(0);

  useEffect(() => {
    if (!banners || banners.length === 0) return;
    const timer = setInterval(() => {
      setActiveIndex((prev) => (prev + 1) % banners.length);
    }, 5000);
    return () => clearInterval(timer);
  }, [banners]);

  if (!banners || banners.length === 0) return null;

  const handlePrev = () => setActiveIndex((prev) => (prev - 1 + banners.length) % banners.length);
  const handleNext = () => setActiveIndex((prev) => (prev + 1) % banners.length);

  return (
    <Box sx={{ position: 'relative', width: '100%', height: { xs: 200, md: 400 }, overflow: 'hidden', borderRadius: 2, bgcolor: '#000' }}>
      {banners.map((banner, index) => (
        <Box
          key={banner.id}
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            opacity: index === activeIndex ? 1 : 0,
            transition: 'opacity 0.8s ease-in-out',
            backgroundImage: `url(${banner.pic || 'https://via.placeholder.com/1200x400'})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
          }}
        >
          <Box sx={{ position: 'absolute', bottom: 0, left: 0, right: 0, bgcolor: 'rgba(0,0,0,0.5)', p: 2, color: 'white' }}>
            <Typography variant="h5">{banner.title}</Typography>
          </Box>
        </Box>
      ))}
      <IconButton onClick={handlePrev} sx={{ position: 'absolute', left: 16, top: '50%', transform: 'translateY(-50%)', color: 'white', bgcolor: 'rgba(0,0,0,0.3)', '&:hover': { bgcolor: 'rgba(0,0,0,0.5)' } }}>
        <ArrowBackIosIcon />
      </IconButton>
      <IconButton onClick={handleNext} sx={{ position: 'absolute', right: 16, top: '50%', transform: 'translateY(-50%)', color: 'white', bgcolor: 'rgba(0,0,0,0.3)', '&:hover': { bgcolor: 'rgba(0,0,0,0.5)' } }}>
        <ArrowForwardIosIcon />
      </IconButton>
    </Box>
  );
};

export default BannerCarousel;
