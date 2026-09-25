import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Box, IconButton, Typography } from '@mui/material';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

interface BannerCarouselProps {
  banners: any[];
}

const BannerCarousel: React.FC<BannerCarouselProps> = ({ banners }) => {
  const [activeIndex, setActiveIndex] = useState(0);
  const navigate = useNavigate();

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
          key={banner.id || index}
          onClick={() => banner.url && navigate(banner.url)}
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            opacity: index === activeIndex ? 1 : 0,
            transition: 'opacity 0.5s ease-in-out',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            backgroundImage: `url(${banner.pic})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            cursor: 'pointer',
          }}
        >
          <Box sx={{ 
            p: 2, 
            bgcolor: 'rgba(0,0,0,0.5)', 
            borderRadius: 1, 
            textAlign: 'center',
            mt: 'auto',
            mb: 4
          }}>
            <Typography variant="h4" color="white" sx={{ fontWeight: "bold" }}>
              {banner.name || banner.title || 'Special Promotion'}
            </Typography>
            <Typography variant="subtitle1" color="white">
              {banner.note || banner.description || 'Check out our latest deals!'}
            </Typography>
          </Box>
        </Box>
      ))}

      <IconButton 
        onClick={handlePrev} 
        sx={{ position: 'absolute', left: 16, top: '50%', transform: 'translateY(-50%)', color: 'white', bgcolor: 'rgba(0,0,0,0.3)', '&:hover': { bgcolor: 'rgba(0,0,0,0.5)' } }}
      >
        <ArrowBackIosIcon />
      </IconButton>
      <IconButton 
        onClick={handleNext} 
        sx={{ position: 'absolute', right: 16, top: '50%', transform: 'translateY(-50%)', color: 'white', bgcolor: 'rgba(0,0,0,0.3)', '&:hover': { bgcolor: 'rgba(0,0,0,0.5)' } }}
      >
        <ArrowForwardIosIcon />
      </IconButton>
      
      {/* Indicators */}
      <Box sx={{ position: 'absolute', bottom: 16, left: 0, right: 0, display: 'flex', justifyContent: 'center', gap: 1 }}>
        {banners.map((_, index) => (
          <Box
            key={index}
            onClick={() => setActiveIndex(index)}
            sx={{
              width: 10,
              height: 10,
              borderRadius: '50%',
              bgcolor: index === activeIndex ? 'white' : 'rgba(255,255,255,0.5)',
              cursor: 'pointer',
            }}
          />
        ))}
      </Box>
    </Box>
  );
};

export default BannerCarousel;
