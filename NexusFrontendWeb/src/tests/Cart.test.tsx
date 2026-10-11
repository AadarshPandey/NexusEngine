import "@testing-library/jest-dom/vitest";
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Cart from '../views/Cart';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router';
import type { EnrichedCartItem } from '../types/ordering';

describe('Cart Component', () => {
  it('renders empty cart message when no items exist', () => {
    const store = configureStore({
      reducer: {
        auth: () => ({ isAuthenticated: true }),
        cart: () => ({ items: [], totalQuantity: 0, totalSubtotal: 0, loading: false })
      }
    });

    render(
      <Provider store={store}>
        <MemoryRouter>
          <Cart />
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Your cart is empty.')).toBeInTheDocument();
  });

  it('renders modern cart items and total amount', () => {
    const mockItems: Partial<EnrichedCartItem>[] = [
      {
        id: 1,
        productId: 101,
        skuId: 201,
        name: 'Test Product 1',
        currentListPrice: 10,
        quantity: 2,
        skuCode: 'TEST-SKU-01',
        isAvailable: true,
      },
      {
        id: 2,
        productId: 102,
        skuId: 202,
        name: 'Test Product 2',
        currentListPrice: 20,
        quantity: 1,
        skuCode: 'TEST-SKU-02',
        isAvailable: true,
      },
    ];

    const store = configureStore({
      reducer: {
        auth: () => ({ isAuthenticated: true }),
        cart: () => ({
          items: mockItems,
          totalQuantity: 3,
          totalSubtotal: 40,
          loading: false,
        })
      }
    });

    render(
      <Provider store={store}>
        <MemoryRouter>
          <Cart />
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Test Product 1')).toBeInTheDocument();
    expect(screen.getByText('Test Product 2')).toBeInTheDocument();
    expect(screen.getByText('Items (3):')).toBeInTheDocument();
    // 10*2 + 20*1 = 40 (appears twice: summary line and total line)
    expect(screen.getAllByText('₹40.00').length).toBe(2);
  });
});
