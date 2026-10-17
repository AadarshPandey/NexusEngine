import "@testing-library/jest-dom/vitest";
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import Profile from '../views/Profile';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router';

// Mock the API calls
vi.mock('../api/customer', () => ({
  getCustomerProfile: vi.fn().mockResolvedValue({ username: 'testuser', displayName: 'Test Nickname' }),
  getCustomerAddresses: vi.fn().mockResolvedValue([]),
  getMemberLoginEvents: vi.fn().mockResolvedValue([]),
}));
vi.mock('../api/ordering', () => ({
  getOrders: vi.fn().mockResolvedValue({ content: [], totalElements: 0, totalPages: 0, size: 100, number: 0 }),
}));

describe('Profile Component', () => {
  it('renders the tabs and can switch between them', async () => {
    const store = configureStore({
      reducer: {
        auth: () => ({ isAuthenticated: true, user: { username: 'testuser' } }),
      }
    });

    render(
      <Provider store={store}>
        <MemoryRouter>
          <Profile />
        </MemoryRouter>
      </Provider>
    );

    // Initial load shows CircularProgress, we need to wait for it to clear.
    // However, since we mock it, we can just look for the tabs once it finishes loading.
    const accountTab = await screen.findByText('Account Info');
    expect(accountTab).toBeInTheDocument();

    const ordersTab = screen.getByText('My Orders');
    const addressTab = screen.getByText('Address Book');
    expect(ordersTab).toBeInTheDocument();
    expect(addressTab).toBeInTheDocument();

    // Verify Tab 1 (Account Info) body
    expect(screen.getByText('Personal Information')).toBeInTheDocument();

    // Switch to Tab 2 (Orders)
    fireEvent.click(ordersTab);
    expect(screen.getByText('Order History')).toBeInTheDocument();

    // Switch to Tab 3 (Addresses)
    fireEvent.click(addressTab);
    expect(screen.getByText(/My Addresses/)).toBeInTheDocument();
  });
});
