import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchCartList, addToCart as apiAddToCart, deleteCartItem as apiDeleteCartItem } from '../../api/cart';
import type { OmsCartItem } from '../../api/cart';
import type { RootState } from '../index';

export const loadCartData = createAsyncThunk('cart/loadData', async (_, { getState }) => {
  const state = getState() as RootState;
  if (!state.auth.isAuthenticated) {
    return [];
  }
  const response = await fetchCartList();
  return response.data;
});

export const addItemToCart = createAsyncThunk('cart/addItem', async (item: Partial<OmsCartItem>, { dispatch, getState }) => {
  const state = getState() as RootState;
  if (!state.auth.isAuthenticated) {
    throw new Error('Please login to add items to cart');
  }
  await apiAddToCart(item);
  dispatch(loadCartData());
});

export const removeCartItem = createAsyncThunk('cart/removeItem', async (id: number, { dispatch, getState }) => {
  const state = getState() as RootState;
  if (!state.auth.isAuthenticated) return;
  await apiDeleteCartItem([id]);
  dispatch(loadCartData());
});

interface CartState {
  items: OmsCartItem[];
  loading: boolean;
  error: string | null;
  totalQuantity: number;
}

const initialState: CartState = {
  items: [],
  loading: false,
  error: null,
  totalQuantity: 0,
};

export const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    clearCart: (state) => {
      state.items = [];
      state.totalQuantity = 0;
    }
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
      })
      .addCase(loadCartData.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to load cart';
      });
  },
});

export const { clearCart } = cartSlice.actions;
export default cartSlice.reducer;
