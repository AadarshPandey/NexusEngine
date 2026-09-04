import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

interface AppState {
  sidebar: {
    opened: boolean;
    withoutAnimation: boolean;
  };
  device: 'desktop' | 'mobile';
}

const initialState: AppState = {
  sidebar: {
    opened: true,
    withoutAnimation: false,
  },
  device: 'desktop',
};

const appSlice = createSlice({
  name: 'app',
  initialState,
  reducers: {
    toggleSideBar(state) {
      state.sidebar.opened = !state.sidebar.opened;
    },
    closeSideBar(state, action: PayloadAction<boolean>) {
      state.sidebar.opened = false;
      state.sidebar.withoutAnimation = action.payload;
    },
    toggleDevice(state, action: PayloadAction<'desktop' | 'mobile'>) {
      state.device = action.payload;
    },
  },
});

export const { toggleSideBar, closeSideBar, toggleDevice } = appSlice.actions;
export default appSlice.reducer;
