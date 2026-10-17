import axios from 'axios';
import { store } from '../store';
import { logout } from '../store/slices/authSlice';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/store',
  timeout: 30000,
});

request.interceptors.request.use(
  (config) => {
    const state = store.getState();
    const token = state.auth.token;
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response && error.response.status === 401) {
      if (window.location.pathname !== '/login') {
        store.dispatch(logout());
        window.location.href = '/login';
      }
    }
    const data = error.response?.data;
    const errMsg = data?.detail || data?.message || error.message || 'Request failed';
    return Promise.reject(new Error(errMsg));
  }
);

export default request;
