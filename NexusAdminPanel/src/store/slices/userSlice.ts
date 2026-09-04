import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import { adminLoginAPI, getAdminInfoAPI, adminLogoutAPI } from '@/apis/admin';
import type { LoginParam, UserInfo } from '@/types/admin';
import type { UmsMenu } from '@/types/menu';

interface UserState {
  userInfo: UserInfo;
}

const initialState: UserState = {
  userInfo: {
    username: '',
    password: '',
    avatar: '',
    roles: [],
    token: '',
    menus: [],
  },
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
    },
    setCredentials(state, action: PayloadAction<LoginParam>) {
      state.userInfo.username = action.payload.username;
      state.userInfo.password = action.payload.password;
    },
    fedLogout(state) {
      state.userInfo.token = '';
    },
    clearUser(state) {
      state.userInfo.token = '';
      state.userInfo.roles = [];
      state.userInfo.menus = [];
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchUserInfo.fulfilled, (state, action) => {
      const data = action.payload;
      if (data.roles && data.roles.length > 0) {
        state.userInfo.roles = data.roles;
      }
      state.userInfo.menus = data.menus;
      state.userInfo.avatar = data.icon;
    });
    builder.addCase(userLogout.fulfilled, (state) => {
      state.userInfo.token = '';
      state.userInfo.roles = [];
    });
  },
});

export const { setToken, setCredentials, fedLogout, clearUser } = userSlice.actions;
export default userSlice.reducer;
