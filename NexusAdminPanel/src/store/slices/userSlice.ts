import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import { adminLoginAPI, getAdminInfoAPI, adminLogoutAPI } from '@/apis/iam';
import type { LoginParam, UserInfo, AdminUser } from '@/types/iam';
import type { ConsoleMenu, UmsMenu } from '@/types/iam';

interface UserState {
  userInfo: UserInfo & {
    displayName?: string;
    avatarUrl?: string;
  };
  adminUser: AdminUser | null;
  isAuthenticated: boolean;
}

const initialState: UserState = {
  userInfo: {
    username: '',
    avatar: '',
    avatarUrl: '',
    displayName: '',
    roles: [],
    token: '',
    menus: [],
  },
  adminUser: null,
  isAuthenticated: false,
};

// Async thunk: login
export const userLogin = createAsyncThunk(
  'user/login',
  async (loginParam: LoginParam, { dispatch }) => {
    const res = await adminLoginAPI(loginParam);
    const tokenStr = res.data.tokenHead + res.data.token;
    dispatch(setToken(tokenStr));
    dispatch(setCredentials(loginParam));
    // Fetch user info after login
    await dispatch(fetchUserInfo());
  },
);

// Async thunk: get user info
export const fetchUserInfo = createAsyncThunk(
  'user/fetchInfo',
  async () => {
    const res = await getAdminInfoAPI();
    return res.data;
  },
);

// Async thunk: logout
export const userLogout = createAsyncThunk(
  'user/logout',
  async () => {
    await adminLogoutAPI();
  },
);

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setToken(state, action: PayloadAction<string>) {
      state.userInfo.token = action.payload;
      state.isAuthenticated = !!action.payload;
    },
    setCredentials(state, action: PayloadAction<LoginParam>) {
      state.userInfo.username = action.payload.username;
    },
    setAdminUser(state, action: PayloadAction<AdminUser>) {
      state.adminUser = action.payload;
      state.userInfo.username = action.payload.username;
      state.userInfo.avatar = action.payload.avatarUrl || action.payload.iconUrl || '';
      state.userInfo.avatarUrl = state.userInfo.avatar;
      state.userInfo.displayName = action.payload.displayName || action.payload.nickName || '';
    },
    fedLogout(state) {
      state.userInfo.token = '';
      state.isAuthenticated = false;
      state.adminUser = null;
    },
    clearUser(state) {
      state.userInfo.token = '';
      state.userInfo.roles = [];
      state.userInfo.menus = [];
      state.adminUser = null;
      state.isAuthenticated = false;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchUserInfo.fulfilled, (state, action) => {
      const data = action.payload;
      if (data.roles && data.roles.length > 0) {
        state.userInfo.roles = data.roles;
      }
      state.userInfo.menus = data.menus;
      const avatar = data.avatarUrl || data.iconUrl || data.icon || '';
      state.userInfo.avatar = avatar;
      state.userInfo.avatarUrl = avatar;
      state.userInfo.displayName = (data as any).displayName || (data as any).nickName || state.userInfo.username;
      state.adminUser = {
        username: data.username,
        avatarUrl: avatar,
        displayName: state.userInfo.displayName,
        roles: data.roles as any,
        menus: data.menus as any,
        isEnabled: true,
      };
      state.isAuthenticated = true;
    });
    builder.addCase(userLogout.fulfilled, (state) => {
      state.userInfo.token = '';
      state.userInfo.roles = [];
      state.adminUser = null;
      state.isAuthenticated = false;
    });
  },
});

export const { setToken, setCredentials, setAdminUser, fedLogout, clearUser } = userSlice.actions;
export default userSlice.reducer;
