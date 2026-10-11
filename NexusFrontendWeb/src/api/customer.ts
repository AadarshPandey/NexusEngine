/**
 * Customer Context API Client
 * Aligned with ADR 0003 and modular monolith domain model
 */

import request from '../utils/request';
import type { Member, MemberAddress } from '../types/customer';

export type { Member, MemberAddress };

export const getCustomerProfile = () => {
  return request.get<unknown, { data: Member }>('/sso/info');
};

export const fetchMemberInfo = getCustomerProfile;

export const getCustomerAddresses = () => {
  return request.get<unknown, { data: MemberAddress[] }>('/member/address/list');
};

export const fetchAddressList = getCustomerAddresses;

export const createCustomerAddress = (address: Partial<MemberAddress>) => {
  return request.post<unknown, { data: unknown }>('/member/address/add', address);
};

export const addAddress = createCustomerAddress;

export const updateCustomerProfile = (data: Partial<Member>) => {
  return request.post<unknown, { data: Member }>('/sso/updateProfile', data);
};

export const updateProfile = updateCustomerProfile;

export const getAuthCode = (email: string) => {
  const params = new URLSearchParams();
  params.append('email', email);
  return request.post<unknown, { message: string }>('/sso/getAuthCode', params);
};

export const uploadAvatar = (file: File) => {
  const formData = new FormData();
  formData.append('file', file);
  return request.post<unknown, { data: string }>('/sso/uploadAvatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

// Backward-compatible type aliases
export type UmsMember = Member;
export type UmsMemberReceiveAddress = MemberAddress;
