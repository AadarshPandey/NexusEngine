import axios, { type AxiosInstance, type AxiosRequestConfig } from 'axios';
import { store } from '@/store';
import { fedLogout } from '@/store/slices/userSlice';

export interface HttpClient extends Omit<AxiosInstance, 'get' | 'post' | 'put' | 'patch' | 'delete' | 'request'> {
  <T = any>(config: AxiosRequestConfig): Promise<T>;
  <T = any>(url: string, config?: AxiosRequestConfig): Promise<T>;
  request<T = any>(config: AxiosRequestConfig): Promise<T>;
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T>;
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T>;
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T>;
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T>;
  patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T>;
}

// Create axios instance
const http = axios.create({
  baseURL: import.meta.env.VITE_BASE_SERVER_URL || 'http://localhost:8080',
  timeout: 15000,
}) as HttpClient;

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

// Response interceptor — unwrap response body and handle RFC 9457 ProblemDetail
http.interceptors.response.use(
  (response) => response.data,
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
