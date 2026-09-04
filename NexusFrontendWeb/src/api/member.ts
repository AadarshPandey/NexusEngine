import request from '../utils/request';

export interface UmsMember {
  id: number;
  memberLevelId: number;
  username: string;
  password?: string;
  nickname: string;
  phone: string;
  status: number;
  createTime: string;
  icon: string;
  gender: number;
  birthday: string;
  city: string;
  job: string;
  personalizedSignature: string;
  sourceType: number;
  integration: number;
  growth: number;
  luckeyCount: number;
  historyIntegration: number;
}

export interface UmsMemberReceiveAddress {
  id: number;
  memberId: number;
  name: string;
  phoneNumber: string;
  defaultStatus: number;
  postCode: string;
  province: string;
  city: string;
  region: string;
  detailAddress: string;
}

export const fetchMemberInfo = () => {
  return request.get<any, { data: UmsMember }>('/sso/info');
};

export const fetchAddressList = () => {
  return request.get<any, { data: UmsMemberReceiveAddress[] }>('/member/address/list');
};

export const addAddress = (address: Partial<UmsMemberReceiveAddress>) => {
  return request.post<any, { data: any }>('/member/address/add', address);
};
