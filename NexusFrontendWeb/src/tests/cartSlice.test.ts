import { describe, it, expect } from 'vitest';
import cartReducer, { clearCart } from '../store/slices/cartSlice';

describe('Cart Slice', () => {
  it('should return the initial state', () => {
    expect(cartReducer(undefined, { type: 'unknown' })).toEqual({
      items: [],
      loading: false,
      error: null,
      totalQuantity: 0,
    });
  });

  it('should handle clearCart', () => {
    const previousState = {
      items: [{ id: 1, quantity: 2 } as any],
      loading: false,
      error: null,
      totalQuantity: 2,
    };
    
    expect(cartReducer(previousState, clearCart())).toEqual({
      items: [],
      loading: false,
      error: null,
      totalQuantity: 0,
    });
  });
});
