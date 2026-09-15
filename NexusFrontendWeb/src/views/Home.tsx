import React, { useEffect, useState } from 'react';
import { Box, Grid, CircularProgress, Typography, TextField, InputAdornment, IconButton } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from 'react-router';
import type { RootState, AppDispatch } from '../store';
import { loadHomeData } from '../store/slices/homeSlice';
import BannerCarousel from '../components/BannerCarousel';
import SectionHeader from '../components/SectionHeader';
import ProductCard from '../components/ProductCard';
import { searchProducts } from '../api/product';
import type { PmsProduct } from '../api/home';
const Home: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { content, recommendations, loading, error } = useSelector((state: RootState) => state.home);

  const [searchKeyword, setSearchKeyword] = useState('');
  const [isSearching, setIsSearching] = useState(false);
  const [searchResults, setSearchResults] = useState<PmsProduct[]>([]);
  const [searchLoading, setSearchLoading] = useState(false);
  const location = useLocation();

  useEffect(() => {
    if (!content) {
      dispatch(loadHomeData());
    }
  }, [dispatch, content]);

  useEffect(() => {
    if (location.pathname === '/' && location.search === '') {
      handleClearSearch();
    }
  }, [location]);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      handleClearSearch();
      return;
    }
    
    setIsSearching(true);
    setSearchLoading(true);
    try {
      const res = await searchProducts(searchKeyword);
      setSearchResults(res.data.list);
    } catch (err) {
      console.error(err);
    } finally {
      setSearchLoading(false);
    }
  };

  const handleClearSearch = () => {
    setSearchKeyword('');
    setIsSearching(false);
    setSearchResults([]);
  };

  if (loading && !isSearching) {
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
      {/* Search Section */}
      <Box component="form" onSubmit={handleSearch} sx={{ mb: 4, display: 'flex', justifyContent: 'center' }}>
        <TextField
          variant="outlined"
          placeholder="Search products..."
          value={searchKeyword}
          onChange={(e) => setSearchKeyword(e.target.value)}
          sx={{ width: { xs: '100%', sm: '500px' } }}
          slotProps={{
            input: {
              endAdornment: (
                <InputAdornment position="end">
                  {searchKeyword && (
                    <IconButton onClick={handleClearSearch} size="small">
                      <ClearIcon />
                    </IconButton>
                  )}
                  <IconButton type="submit" edge="end">
                    <SearchIcon />
                  </IconButton>
                </InputAdornment>
              ),
            }
          }}
        />
      </Box>

      {isSearching ? (
        <>
          <SectionHeader title="Search Results" subtitle={`Found results for "${searchKeyword}"`} />
          {searchLoading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
              <CircularProgress />
            </Box>
          ) : searchResults.length > 0 ? (
            <Grid container spacing={4}>
              {searchResults.map((product) => (
                <Grid key={product.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                  <ProductCard product={product} />
                </Grid>
              ))}
            </Grid>
          ) : (
            <Box sx={{ textAlign: 'center', my: 4 }}>
              <Typography variant="body1">No products found for "{searchKeyword}".</Typography>
            </Box>
          )}
        </>
      ) : (
        <>
          {/* Hero Banner Section */}
          {content?.advertiseList && <BannerCarousel banners={content.advertiseList} />}

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
        </>
      )}
    </Box>
  );
};

export default Home;
