import "@testing-library/jest-dom/vitest";
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import ProductCard from '../components/ProductCard';
import { MemoryRouter } from 'react-router';
import type { PmsProduct } from '../api/home';

const mockProduct: PmsProduct = {
  id: 1,
  brandId: 1,
  permissionGroupId: 1,
  name: 'Nexus Phone Pro',
  primaryImageUrl: 'https://via.placeholder.com/200',
  productCode: 'NEXUS-001',
  deleteStatus: 0,
  isPublished: 1,
  isNewArrival: 1,
  isFeatured: 1,
  approvalStatus: 1,
  displayOrder: 100,
  unitsSold: 50,
  listPrice: 999.99,
  salePrice: 899.99,
  promotionType: 1,
  subTitle: 'The ultimate smartphone experience.',
  customerComment: 'Features an edge-to-edge display.',
  onHandQty: 1000,
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
    expect(screen.getByText('₹899.99')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /view details/i })).toHaveAttribute('href', '/product/1');
  });
});
