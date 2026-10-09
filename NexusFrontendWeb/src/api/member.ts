import request from '../utils/request';

export interface UmsMember {
  id: number;
  username: string;
  passwordHash?: string;
  displayName: string;
  contactPhone: string;
  status: number;
  createdAt: string;
  iconUrl: string;
  gender: number;
  city: string;
  job: string;
  signupChannel: number;
  pointsBalance: number;
  lifetimePointsEarned: number;
}

export interface UmsMemberReceiveAddress {
  id: number;
  memberId: number;
  name: string;
  recipientPhone: string;
  isDefault: number;
  postalCode: string;
  state: string;
  city: string;
  district: string;
  addressLine: string;
}

export const fetchMemberInfo = () => {
  return request.get<unknown, { data: UmsMember }>('/sso/info');
};

export const fetchAddressList = () => {
  return request.get<unknown, { data: UmsMemberReceiveAddress[] }>('/member/address/list');
};

export const addAddress = (address: Partial<UmsMemberReceiveAddress>) => {
  return request.post<unknown, { data: unknown }>('/member/address/add', address);
};

export const updateProfile = (data: Partial<UmsMember>) => {
  return request.post<unknown, { data: UmsMember }>('/sso/updateProfile', data);
};
