import axios from 'axios';
import { store } from '@/store';
import { fedLogout } from '@/store/slices/userSlice';

// Create axios instance
const http = axios.create({
  baseURL: import.meta.env.VITE_BASE_SERVER_URL || '',
  timeout: 15000,
});

// Request interceptor — attach JWT token
http.interceptors.request.use(
  (config) => {
    const token = store.getState().user.userInfo.token;
    if (token) {
      config.headers.Authorization = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
    }
    return config;
  },
  (e) => Promise.reject(e),
);

// Response interceptor — handle errors and RFC 9457 ProblemDetail
http.interceptors.response.use(
  (response) => {
    const res = response.data;
    // Support legacy {code, message, data} envelope if present
    if (res && typeof res === 'object' && 'code' in res && typeof res.code === 'number') {
      if (res.code !== 200) {
        if (res.code === 401) {
          store.dispatch(fedLogout());
        }
        return Promise.reject(new Error(res.message || 'Request failed'));
      }
      return res.data !== undefined ? res.data : res;
    }
    return res;
  },
  (error) => {
    console.error('Request error:', error);
    if (error.response && error.response.status === 401) {
      store.dispatch(fedLogout());
    }
    const errMsg =
      error.response?.data?.detail ||
      error.response?.data?.title ||
      error.response?.data?.message ||
      error.message ||
      'Request failed';
    return Promise.reject(new Error(errMsg));
  },
);

export default http;
