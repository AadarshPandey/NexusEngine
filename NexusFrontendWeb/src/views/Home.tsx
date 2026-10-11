import React, { useEffect, useState } from 'react';
import { Box, Grid, CircularProgress, Typography, TextField, InputAdornment, IconButton } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import { useDispatch, useSelector } from 'react-redux';
import { useSearchParams, useNavigate } from 'react-router';
import type { RootState, AppDispatch } from '../store';
import { loadHomeData } from '../store/slices/homeSlice';
import BannerCarousel from '../components/BannerCarousel';
import SectionHeader from '../components/SectionHeader';
import ProductCard from '../components/ProductCard';
import { searchProducts } from '../api/catalog';
import type { Product } from '../types/catalog';

const Home: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { body, recommendations, loading, error } = useSelector((state: RootState) => state.home);

  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  const queryParam = searchParams.get('keyword') || '';

  const [searchKeyword, setSearchKeyword] = useState(queryParam);
  const [isSearching, setIsSearching] = useState(!!queryParam);
  const [searchResults, setSearchResults] = useState<Product[]>([]);
  const [searchLoading, setSearchLoading] = useState(false);

  useEffect(() => {
    if (!body) {
      dispatch(loadHomeData());
    }
  }, [dispatch, body]);

  useEffect(() => {
    const keyword = searchParams.get('keyword');
    if (keyword) {
      setSearchKeyword(keyword);
      setIsSearching(true);
      performSearch(keyword);
    } else {
      handleClearSearch();
    }
  }, [searchParams]);

  const performSearch = async (keyword: string) => {
    setSearchLoading(true);
    try {
      const res = await searchProducts(keyword);
      setSearchResults(res.data.list);
    } catch (err) {
      console.error(err);
    } finally {
      setSearchLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      navigate('/');
      return;
    }
    setSearchParams({ keyword: searchKeyword.trim() });
  };

  const handleClearSearch = () => {
    setSearchKeyword('');
    setIsSearching(false);
    setSearchResults([]);
  };

  const handleClearInput = () => {
    setSearchKeyword('');
    navigate('/');
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

  const banners = body?.banners || body?.advertiseList || [];

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
                    <IconButton onClick={handleClearInput} size="small">
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
          {banners.length > 0 && <BannerCarousel banners={banners} />}

          {/* New Arrivals Section */}
          {body?.newProductList && body.newProductList.length > 0 && (
            <>
              <SectionHeader title="New Arrivals" subtitle="Check out our latest products" />
              <Grid container spacing={4}>
                {body.newProductList.map((product) => (
                  <Grid key={product.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                    <ProductCard product={product} />
                  </Grid>
                ))}
              </Grid>
            </>
          )}

          {/* Hot Products Section */}
          {body?.hotProductList && body.hotProductList.length > 0 && (
            <>
              <SectionHeader title="Hot Products" subtitle="Trending items everyone is buying" />
              <Grid container spacing={4}>
                {body.hotProductList.map((product) => (
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
