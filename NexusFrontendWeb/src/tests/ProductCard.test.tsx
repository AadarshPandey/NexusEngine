import "@testing-library/jest-dom/vitest";
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import ProductCard from '../components/ProductCard';
import { MemoryRouter } from 'react-router';
import type { Product } from '../types/catalog';

const mockProduct: Product = {
  id: 1,
  brandId: 1,
  categoryId: 1,
  name: 'Nexus Phone Pro',
  primaryImageUrl: 'https://via.placeholder.com/200',
  productCode: 'NEXUS-001',
  isPublished: true,
  isNewArrival: true,
  isFeatured: true,
  unitsSold: 50,
  listPrice: 999.99,
  price: 999.99,
  salePrice: 899.99,
  subTitle: 'The ultimate smartphone experience.',
  description: 'Features an edge-to-edge display.',
};

describe('ProductCard Component', () => {
  it('renders modern product details with sale price and units sold correctly', () => {
    render(
      <MemoryRouter>
        <ProductCard product={mockProduct} />
      </MemoryRouter>
    );

    expect(screen.getByText('Nexus Phone Pro')).toBeInTheDocument();
    expect(screen.getByText('The ultimate smartphone experience.')).toBeInTheDocument();
    expect(screen.getByText('₹899.99')).toBeInTheDocument();
    expect(screen.getByText('50 units sold')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /view details/i })).toHaveAttribute('href', '/product/1');
  });
});
