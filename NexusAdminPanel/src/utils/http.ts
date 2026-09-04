import axios from 'axios';
import type { CommonResult } from '@/types/common';
import { store } from '@/store';
import { fedLogout } from '@/store/slices/userSlice';

// Create axios instance
const http = axios.create({
  baseURL: import.meta.env.VITE_BASE_SERVER_URL || 'http://localhost:8080',
  timeout: 10000,
});

// Request interceptor — attach JWT token
http.interceptors.request.use(
  (config) => {
    const token = store.getState().user.userInfo.token;
    if (token) {
      config.headers.Authorization = token;
    }
    return config;
  },
  (e) => Promise.reject(e),
);

// Response interceptor — handle errors
http.interceptors.response.use(
  (response) => {
    const res: CommonResult<unknown> = response.data;
    if (res.code !== 200) {
      // 401: Not logged in
      if (res.code === 401) {
        const shouldRelogin = window.confirm(
          'Your session has expired. Click OK to log in again.',
        );
        if (shouldRelogin) {
          store.dispatch(fedLogout());
          location.reload();
        }
      }
      return Promise.reject(new Error(res.message || 'Request failed'));
    }
    // Return the response data (unwrapped from the code/message/data wrapper)
    return response.data;
  },
  (error) => {
    console.error('Request error:', error);
    if (error.response && error.response.status === 401) {
      const shouldRelogin = window.confirm(
        'Your session has expired. Click OK to log in again.',
      );
      if (shouldRelogin) {
        store.dispatch(fedLogout());
        location.reload();
      }
    }
    return Promise.reject(error);
  },
);

export default http;
