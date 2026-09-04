import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { Box, Typography, Button, Grid, Paper, CircularProgress, Divider } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import type { AppDispatch, RootState } from '../store';
import { fetchProductDetail } from '../api/product';
import type { PmsPortalProductDetail } from '../api/product';
import { addItemToCart } from '../store/slices/cartSlice';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  
  const [productDetail, setProductDetail] = useState<PmsPortalProductDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [addingToCart, setAddingToCart] = useState(false);

  useEffect(() => {
    if (id) {
      loadDetail(Number(id));
    }
  }, [id]);

  const loadDetail = async (productId: number) => {
    try {
      setLoading(true);
      const res = await fetchProductDetail(productId);
      setProductDetail(res.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (!productDetail) return;

    try {
      setAddingToCart(true);
      await dispatch(addItemToCart({
        productId: productDetail.product.id,
        quantity: 1,
        price: productDetail.product.price,
        productPic: productDetail.product.pic,
        productName: productDetail.product.name,
      })).unwrap();
      alert('Item added to cart!');
    } catch (error: any) {
      alert(error.message || 'Failed to add to cart');
    } finally {
      setAddingToCart(false);
    }
  };

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;
  }

  if (!productDetail) {
    return <Box sx={{ mt: 10, textAlign: 'center' }}><Typography variant="h5">Product not found</Typography></Box>;
  }

  const { product } = productDetail;

  return (
    <Box sx={{ mt: 4 }}>
      <Paper sx={{ p: 4 }}>
        <Grid container spacing={4}>
          <Grid size={{ xs: 12, md: 6 }}>
            <Box
              component="img"
              src={product.pic || 'https://via.placeholder.com/400'}
              alt={product.name}
              sx={{ width: '100%', maxHeight: 500, objectFit: 'contain' }}
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 'bold' }}>{product.name}</Typography>
            <Typography variant="subtitle1" color="text.secondary" gutterBottom>{product.subTitle}</Typography>
            <Divider sx={{ my: 2 }} />
            <Typography variant="h3" color="primary" gutterBottom>₹{product.price?.toFixed(2)}</Typography>
            
            <Box sx={{ mt: 4 }}>
              <Button 
                variant="contained" 
                size="large" 
                color="primary" 
                fullWidth 
                onClick={handleAddToCart}
                disabled={addingToCart}
              >
                {addingToCart ? 'Adding...' : 'Add to Cart'}
              </Button>
            </Box>

            <Box sx={{ mt: 4 }}>
              <Typography variant="h6" gutterBottom>Product Description</Typography>
              {/* Note: Rendering raw HTML needs to be sanitized in production */}
              <div dangerouslySetInnerHTML={{ __html: product.description || '<p>No description available.</p>' }} />
            </Box>
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );
};

export default ProductDetail;
