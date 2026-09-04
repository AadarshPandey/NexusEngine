import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Cart from '../views/Cart';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router';

describe('Cart Component', () => {
  it('renders empty cart message when no items exist', () => {
    const store = configureStore({
      reducer: {
        auth: () => ({ isAuthenticated: true }),
        cart: () => ({ items: [], totalQuantity: 0, loading: false })
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

  it('renders cart items and total price', () => {
    const store = configureStore({
      reducer: {
        auth: () => ({ isAuthenticated: true }),
        cart: () => ({
          items: [
            { id: 1, productName: 'Test Product 1', price: 10, quantity: 2 },
            { id: 2, productName: 'Test Product 2', price: 20, quantity: 1 },
          ],
          totalQuantity: 3,
          loading: false
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
