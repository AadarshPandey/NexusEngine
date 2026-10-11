import { describe, it, expect } from 'vitest';
import cartReducer, { clearCart, loadCartData } from '../store/slices/cartSlice';
import type { EnrichedCartItem } from '../types/ordering';

describe('cartSlice', () => {
  it('should return the initial state', () => {
    expect(cartReducer(undefined, { type: 'unknown' })).toEqual({
      items: [],
      loading: false,
      error: null,
      totalQuantity: 0,
      totalSubtotal: 0,
    });
  });

  it('should handle clearCart', () => {
    const previousState = {
      items: [
        {
          id: 1,
          productId: 101,
          skuId: 201,
          quantity: 2,
          name: 'Wireless Mouse',
          currentListPrice: 50,
          isAvailable: true,
        } as EnrichedCartItem,
      ],
      loading: false,
      error: null,
      totalQuantity: 2,
      totalSubtotal: 100,
    };

    expect(cartReducer(previousState, clearCart())).toEqual({
      items: [],
      loading: false,
      error: null,
      totalQuantity: 0,
      totalSubtotal: 0,
    });
  });

  it('computes total subtotal and quantity correctly when loadCartData is fulfilled', () => {
    const mockCartItems: EnrichedCartItem[] = [
      {
        id: 1,
        productId: 201,
        skuId: 301,
        quantity: 2,
        unitPriceAtAdd: 1200,
        name: 'Mechanical Keyboard',
        productCode: 'KB-MECH-01',
        primaryImageUrl: '/images/kb.png',
        skuCode: 'KB-MECH-BLUE',
        currentListPrice: 1200,
        currentSalePrice: 1200,
        price: 1200,
        availableQty: 15,
        skuAttributes: { switch: 'Blue' },
        hasPriceChanged: false,
        isAvailable: true,
      },
      {
        id: 2,
        productId: 202,
        skuId: 302,
        quantity: 1,
        unitPriceAtAdd: 500,
        name: 'Gaming Mouse Pad',
        productCode: 'PAD-01',
        primaryImageUrl: '/images/pad.png',
        skuCode: 'PAD-XL',
        currentListPrice: 600,
        currentSalePrice: 500,
        price: 500,
        availableQty: 30,
        skuAttributes: { size: 'XL' },
        hasPriceChanged: false,
        isAvailable: true,
      },
    ];

    const state = cartReducer(
      { items: [], loading: false, error: null, totalQuantity: 0, totalSubtotal: 0 },
      { type: loadCartData.fulfilled.type, payload: mockCartItems }
    );

    expect(state.items).toHaveLength(2);
    expect(state.totalQuantity).toBe(3); // 2 + 1
    expect(state.totalSubtotal).toBe(2900); // 1200*2 + 500*1
  });
});
