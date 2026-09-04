import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchHomeContent, fetchRecommendProductList } from '../../api/home';
import type { HomeContentResult, PmsProduct } from '../../api/home';

export const loadHomeData = createAsyncThunk('home/loadData', async () => {
  const contentResponse = await fetchHomeContent();
  const recommendResponse = await fetchRecommendProductList(8, 1);
  return {
    content: contentResponse.data,
    recommendations: recommendResponse.data,
  };
});

interface HomeState {
  content: HomeContentResult | null;
  recommendations: PmsProduct[];
  loading: boolean;
  error: string | null;
}

const initialState: HomeState = {
  content: null,
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
        state.content = action.payload.content;
        state.recommendations = action.payload.recommendations;
      })
      .addCase(loadHomeData.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to load home data';
      });
  },
});

export default homeSlice.reducer;
