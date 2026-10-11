import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import type { Member } from '../../types/customer';

export type User = Partial<Member>;

interface AuthState {
  token: string | null;
  isAuthenticated: boolean;
  user: User | null;
}

const savedToken = localStorage.getItem('token');

const initialState: AuthState = {
  token: savedToken,
  isAuthenticated: !!savedToken,
  user: null,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    login: (state, action: PayloadAction<{ token: string; user: User }>) => {
      state.token = action.payload.token;
      const u = action.payload.user;
      state.user = {
        ...u,
        displayName: u.displayName || u.nickname || '',
        avatarUrl: u.avatarUrl || u.icon || '',
        phoneE164: u.phoneE164 || u.phone || '',
      };
      state.isAuthenticated = true;
      localStorage.setItem('token', action.payload.token);
    },
    updateUser: (state, action: PayloadAction<Partial<User>>) => {
      if (state.user) {
        state.user = {
          ...state.user,
          ...action.payload,
          displayName: action.payload.displayName || action.payload.nickname || state.user.displayName,
          avatarUrl: action.payload.avatarUrl || action.payload.icon || state.user.avatarUrl,
          phoneE164: action.payload.phoneE164 || action.payload.phone || state.user.phoneE164,
        };
      }
    },
    logout: (state) => {
      state.token = null;
      state.user = null;
      state.isAuthenticated = false;
      localStorage.removeItem('token');
    },
  },
});

export const { login, updateUser, logout } = authSlice.actions;
export default authSlice.reducer;
