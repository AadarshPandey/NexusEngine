import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from '../App';

describe('App Component', () => {
  it('renders the Nexus Storefront header', () => {
    render(<App />);
    expect(screen.getByText('Nexus Storefront')).toBeInTheDocument();
  });
});
