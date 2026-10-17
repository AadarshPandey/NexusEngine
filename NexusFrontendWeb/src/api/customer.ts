/**
 * Customer Context API Client
 * Aligned with ADR 0003 and modular monolith domain model
 */

import request from '../utils/request';
import type { Member, MemberAddress } from '../types/customer';

export type { Member, MemberAddress };

export const getCustomerProfile = () => {
  return request.get<unknown, Member>('/customer/auth/me');
};

export const fetchMemberInfo = getCustomerProfile;

export const getCustomerAddresses = () => {
  return request.get<unknown, MemberAddress[]>('/customer/addresses');
};

export const fetchAddressList = getCustomerAddresses;

export const createCustomerAddress = (address: Partial<MemberAddress>) => {
  return request.post<unknown, number>('/customer/addresses', address);
};

export const addAddress = createCustomerAddress;

export const updateCustomerAddress = (id: number, address: Partial<MemberAddress>) => {
  return request.put<unknown, number>(`/customer/addresses/${id}`, address);
};

export const deleteCustomerAddress = (id: number) => {
  return request.delete<unknown, number>(`/customer/addresses/${id}`);
};

export const updateCustomerProfile = (data: Partial<Member>) => {
  return request.put<unknown, Member>('/customer/auth/profile', data);
};

export const updateProfile = updateCustomerProfile;

export const getAuthCode = (email: string) => {
  return request.post<unknown, void>(`/customer/auth/otp?email=${encodeURIComponent(email)}`);
};

export const uploadAvatar = (file: File) => {
  const formData = new FormData();
  formData.append('file', file);
  return request.post<unknown, string>('/customer/auth/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

export const loginMember = (params: { username: string; password?: string; passwordHash?: string }) => {
  const queryParams = new URLSearchParams();
  queryParams.append('username', params.username);
  if (params.password) queryParams.append('password', params.password);
  if (params.passwordHash) queryParams.append('passwordHash', params.passwordHash);
  return request.post<unknown, { token: string; tokenHead: string }>('/customer/auth/login', queryParams);
};

export const registerMember = (params: { username: string; password?: string; passwordHash?: string; email: string; authCode: string }) => {
  const queryParams = new URLSearchParams();
  queryParams.append('username', params.username);
  if (params.password) queryParams.append('password', params.password);
  if (params.passwordHash) queryParams.append('passwordHash', params.passwordHash);
  queryParams.append('email', params.email);
  queryParams.append('authCode', params.authCode);
  return request.post<unknown, void>('/customer/auth/register', queryParams);
};
