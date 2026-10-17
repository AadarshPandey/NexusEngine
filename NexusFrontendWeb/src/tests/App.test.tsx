import "@testing-library/jest-dom/vitest";
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from '../App';

describe('App Component', () => {
  it('renders the NexusFrontend header', () => {
    render(<App />);
    expect(screen.getByText('NexusFrontend')).toBeInTheDocument();
  });
});
