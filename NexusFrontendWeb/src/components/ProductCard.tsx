import React from 'react';
import { Card, CardMedia, CardContent, Typography, CardActions, Button } from '@mui/material';
import type { PmsProduct } from '../api/home';
import { Link } from 'react-router';

interface ProductCardProps {
  product: PmsProduct;
}

const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', transition: '0.3s', '&:hover': { transform: 'scale(1.02)', boxShadow: 6 } }}>
      <CardMedia
        component="img"
        height="200"
        image={product.pic || 'https://via.placeholder.com/200'}
        alt={product.name}
        sx={{ objectFit: 'contain', p: 2 }}
      />
      <CardContent sx={{ flexGrow: 1 }}>
        <Typography gutterBottom variant="h6" component="h2" noWrap title={product.name}>
          {product.name}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2, height: 40, overflow: 'hidden' }}>
          {product.subTitle}
        </Typography>
        <Typography variant="h5" color="primary" sx={{ fontWeight: 'bold' }}>
          ₹{product.price?.toFixed(2)}
        </Typography>
      </CardContent>
      <CardActions>
        <Button size="small" variant="contained" fullWidth component={Link} to={`/product/${product.id}`}>
          View Details
        </Button>
      </CardActions>
    </Card>
  );
};

export default ProductCard;
