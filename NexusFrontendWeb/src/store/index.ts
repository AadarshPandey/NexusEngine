import { configureStore } from '@reduxjs/toolkit';
import { persistStore, persistReducer } from 'redux-persist';
import authReducer from './slices/authSlice';
import homeReducer from './slices/homeSlice';
import cartReducer from './slices/cartSlice';

// Custom storage adapter to resolve Vite ESM/CommonJS interop issues with redux-persist
const customStorage = {
  getItem(key: string) {
    return Promise.resolve(window.localStorage.getItem(key));
  },
  setItem(key: string, value: string) {
    window.localStorage.setItem(key, value);
    return Promise.resolve();
  },
  removeItem(key: string) {
    window.localStorage.removeItem(key);
    return Promise.resolve();
  },
};

const persistConfig = {
  key: 'nexus-frontend-root',
  storage: customStorage,
};

const persistedAuthReducer = persistReducer(persistConfig, authReducer);

export const store = configureStore({
  reducer: {
    auth: persistedAuthReducer,
    home: homeReducer,
    cart: cartReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export const persistor = persistStore(store);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
