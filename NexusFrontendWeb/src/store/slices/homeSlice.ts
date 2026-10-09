import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchHomeContent, fetchRecommendProductList } from '../../api/home';
import type { HomeContentResult, PmsProduct } from '../../api/home';

export const loadHomeData = createAsyncThunk('home/loadData', async () => {
  const contentResponse = await fetchHomeContent();
  const recommendResponse = await fetchRecommendProductList(8, 1);
  return {
    body: contentResponse.data,
    recommendations: recommendResponse.data,
  };
});

interface HomeState {
  body: HomeContentResult | null;
  recommendations: PmsProduct[];
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
        state.recommendations = action.payload.recommendations;
      })
      .addCase(loadHomeData.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to load home data';
      });
  },
});

export default homeSlice.reducer;
