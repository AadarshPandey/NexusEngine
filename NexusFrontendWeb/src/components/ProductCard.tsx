import React from 'react';
import { Card, CardMedia, CardContent, Typography, CardActions, Button, Box } from '@mui/material';
import type { Product } from '../types/catalog';
import { resolveImageUrl } from '../utils/media';
import { Link } from 'react-router';

interface ProductCardProps {
  product: Product;
}

const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  const imageUrl = resolveImageUrl(product.primaryImageUrl);
  const originalPrice = product.price ?? product.listPrice;
  const effectivePrice = product.salePrice ?? originalPrice;
  const hasSale = product.salePrice !== undefined && product.salePrice < (originalPrice ?? 0);

  return (
    <Card
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        transition: '0.3s',
        '&:hover': { transform: 'scale(1.02)', boxShadow: 6 },
      }}
    >
      <CardMedia
        component="img"
        height="200"
        image={imageUrl}
        alt={product.name}
        sx={{ objectFit: 'contain', p: 2, bgcolor: '#fafafa' }}
      />
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography gutterBottom variant="h6" component="h2" noWrap title={product.name}>
          {product.name}
        </Typography>
        {product.subTitle && (
          <Typography variant="body2" color="text.secondary" noWrap sx={{ mb: 1 }}>
            {product.subTitle}
          </Typography>
        )}
        <Box sx={{ display: 'flex', alignItems: 'baseline', gap: 1 }}>
          <Typography variant="h5" color="primary" sx={{ fontWeight: 'bold' }}>
            ₹{effectivePrice ? Number(effectivePrice).toFixed(2) : '0.00'}
          </Typography>
          {hasSale && (
            <Typography variant="body2" sx={{ textDecoration: 'line-through', color: 'text.disabled' }}>
              ₹{Number(originalPrice).toFixed(2)}
            </Typography>
          )}
        </Box>
        <Typography
          variant="caption"
          color={(product.onHandQty ?? 0) > 0 ? 'success.main' : 'error.main'}
          sx={{ display: 'block', mt: 0.5, fontWeight: 500 }}
        >
          {(product.onHandQty ?? 0) > 0 ? `${product.onHandQty} in stock` : 'Out of stock'}
        </Typography>
      </CardContent>
      <CardActions sx={{ p: 2, pt: 0 }}>
        <Button size="small" variant="contained" fullWidth component={Link} to={`/product/${product.id}`}>
          View Details
        </Button>
      </CardActions>
    </Card>
  );
};

export default ProductCard;
