import React, { useEffect } from 'react';
import { Box, Grid, CircularProgress, Typography } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState, AppDispatch } from '../store';
import { loadHomeData } from '../store/slices/homeSlice';
import BannerCarousel from '../components/BannerCarousel';
import SectionHeader from '../components/SectionHeader';
import ProductCard from '../components/ProductCard';

const Home: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { content, recommendations, loading, error } = useSelector((state: RootState) => state.home);

  useEffect(() => {
    if (!content) {
      dispatch(loadHomeData());
    }
  }, [dispatch, content]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '60vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ mt: 4, textAlign: 'center', color: 'error.main' }}>
        <Typography variant="h6">{error}</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ mt: 2 }}>
      {/* Hero Banner Section */}
      {content?.subjectList && <BannerCarousel banners={content.subjectList} />}

      {/* New Arrivals Section */}
      {content?.newProductList && content.newProductList.length > 0 && (
        <>
          <SectionHeader title="New Arrivals" subtitle="Check out our latest products" />
          <Grid container spacing={4}>
            {content.newProductList.map((product) => (
              <Grid key={product.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                <ProductCard product={product} />
              </Grid>
            ))}
          </Grid>
        </>
      )}

      {/* Hot Products Section */}
      {content?.hotProductList && content.hotProductList.length > 0 && (
        <>
          <SectionHeader title="Hot Products" subtitle="Trending items everyone is buying" />
          <Grid container spacing={4}>
            {content.hotProductList.map((product) => (
              <Grid key={product.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                <ProductCard product={product} />
              </Grid>
            ))}
          </Grid>
        </>
      )}

      {/* Recommendations Section */}
      {recommendations && recommendations.length > 0 && (
        <>
          <SectionHeader title="Recommended For You" subtitle="Handpicked selection just for you" />
          <Grid container spacing={4}>
            {recommendations.map((product) => (
              <Grid key={product.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                <ProductCard product={product} />
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Box>
  );
};

export default Home;
