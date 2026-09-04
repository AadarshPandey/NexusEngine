import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import ProductCard from '../components/ProductCard';
import { MemoryRouter } from 'react-router';
import type { PmsProduct } from '../api/home';

const mockProduct: PmsProduct = {
  id: 1,
  brandId: 1,
  productCategoryId: 1,
  name: 'Nexus Phone Pro',
  pic: 'https://via.placeholder.com/200',
  productSn: 'NEXUS-001',
  deleteStatus: 0,
  publishStatus: 1,
  newStatus: 1,
  recommandStatus: 1,
  verifyStatus: 1,
  sort: 100,
  sale: 50,
  price: 999.99,
  promotionPrice: 899.99,
  subTitle: 'The ultimate smartphone experience.',
  description: 'Features an edge-to-edge display.',
  stock: 1000,
};

describe('ProductCard Component', () => {
  it('renders the product details correctly', () => {
    render(
      <MemoryRouter>
        <ProductCard product={mockProduct} />
      </MemoryRouter>
    );

    expect(screen.getByText('Nexus Phone Pro')).toBeInTheDocument();
    expect(screen.getByText('The ultimate smartphone experience.')).toBeInTheDocument();
    expect(screen.getByText('₹999.99')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /view details/i })).toHaveAttribute('href', '/product/1');
  });
});
