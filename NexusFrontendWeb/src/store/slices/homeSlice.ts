import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchHomeContent, fetchRecommendProductList } from '../../api/catalog';
import type { HomeContentResult } from '../../api/catalog';
import type { Product } from '../../types/catalog';
import type { Banner } from '../../types/promotion';

export const loadHomeData = createAsyncThunk('home/loadData', async () => {
  const contentResponse = await fetchHomeContent();
  const recommendResponse = await fetchRecommendProductList(8, 1);
  return {
    body: contentResponse,
    recommendations: recommendResponse,
  };
});

export interface ModernHomeContent {
  banners: Banner[];
  newProducts: Product[];
  hotProducts: Product[];
  brandList?: unknown[];
}

interface HomeState {
  body: HomeContentResult | null;
  recommendations: Product[];
  loading: boolean;
  error: string | null;
}

const initialState: HomeState = {
  body: null,
  recommendations: [],
  loading: false,
  error: null,
};

export const homeSlice = createSlice({
  name: 'home',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(loadHomeData.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(loadHomeData.fulfilled, (state, action) => {
        state.loading = false;
        state.body = action.payload.body;
        state.recommendations = action.payload.recommendations || [];
      })
      .addCase(loadHomeData.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to load home data';
      });
  },
});

export default homeSlice.reducer;
