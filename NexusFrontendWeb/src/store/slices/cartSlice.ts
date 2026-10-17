import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getCart, addItemToCart as apiAddToCart, removeCartItems as apiDeleteCartItem, updateItemQuantity as apiUpdateQuantity } from '../../api/ordering';
import type { EnrichedCartItem } from '../../types/ordering';
import type { RootState } from '../index';

export const loadCartData = createAsyncThunk('cart/loadData', async (_, { getState }) => {
  const state = getState() as RootState;
  if (!state.auth.isAuthenticated) {
    return [];
  }
  const response = await getCart();
  const rawItems: any[] = Array.isArray(response) ? response : [];
  return rawItems.map((item: any) => ({
    id: item.id,
    productId: item.productId,
    skuId: item.skuId || item.productSkuId || 0,
    name: item.name || '',
    primaryImageUrl: item.primaryImageUrl || '',
    skuCode: item.skuCode || '',
    productCode: item.productCode || '',
    currentListPrice: item.currentListPrice ?? item.price ?? 0,
    currentSalePrice: item.currentSalePrice ?? item.price,
    price: item.currentSalePrice ?? item.currentListPrice ?? item.price ?? 0,
    quantity: item.quantity || 1,
    availableQty: item.availableQty ?? 999,
    skuAttributes: item.skuAttributes,
    isAvailable: item.isAvailable ?? true,
    hasPriceChanged: item.hasPriceChanged ?? false,
  })) as EnrichedCartItem[];
});

export const addItemToCart = createAsyncThunk(
  'cart/addItem',
  async (item: { productId: number; skuId?: number; productSkuId?: number; quantity: number; price?: number }, { dispatch, getState }) => {
    const state = getState() as RootState;
    if (!state.auth.isAuthenticated) {
      throw new Error('Please login to add items to cart');
    }
    await apiAddToCart({
      productId: item.productId,
      skuId: item.skuId || item.productSkuId,
      quantity: item.quantity || 1,
      price: item.price,
    });
    dispatch(loadCartData());
  },
);

export const removeCartItem = createAsyncThunk('cart/removeItem', async (id: number, { dispatch, getState }) => {
  const state = getState() as RootState;
  if (!state.auth.isAuthenticated) return;
  await apiDeleteCartItem([id]);
  dispatch(loadCartData());
});

export const updateQuantity = createAsyncThunk(
  'cart/updateQuantity',
  async ({ id, quantity }: { id: number; quantity: number }, { dispatch, getState }) => {
    const state = getState() as RootState;
    if (!state.auth.isAuthenticated) return;
    await apiUpdateQuantity(id, quantity);
    dispatch(loadCartData());
  },
);

interface CartState {
  items: EnrichedCartItem[];
  loading: boolean;
  error: string | null;
  totalQuantity: number;
  totalSubtotal: number;
}

const initialState: CartState = {
  items: [],
  loading: false,
  error: null,
  totalQuantity: 0,
  totalSubtotal: 0,
};

export const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    clearCart: (state) => {
      state.items = [];
      state.totalQuantity = 0;
      state.totalSubtotal = 0;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loadCartData.pending, (state) => {
        state.loading = true;
      })
      .addCase(loadCartData.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
        state.totalQuantity = action.payload.reduce((total, item) => total + item.quantity, 0);
        state.totalSubtotal = action.payload.reduce(
          (sum, item) => sum + (item.currentSalePrice ?? item.currentListPrice ?? item.price ?? 0) * item.quantity,
          0,
        );
      })
      .addCase(loadCartData.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to load cart';
      });
  },
});

export const { clearCart } = cartSlice.actions;
export default cartSlice.reducer;
