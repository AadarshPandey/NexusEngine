import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import React from 'react';
import { Provider } from 'react-redux';
import { ThemeProvider } from '@mui/material/styles';
import { store } from '@/store';
import theme from '@/theme/theme';
import App from '../App';

describe('Admin Panel App', () => {
  it('renders without crashing', () => {
    const { container } = render(
      <Provider store={store}>
        <ThemeProvider theme={theme}>
          <App />
        </ThemeProvider>
      </Provider>
    );
    expect(container).toBeDefined();
  });
});
