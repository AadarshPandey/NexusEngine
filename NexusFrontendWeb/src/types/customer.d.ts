/**
 * Customer Bounded Context Types for Customer Storefront
 * Aligned with ADR 0003 and modular monolith domain model
 */

export interface Member {
  id: number
  username: string
  displayName: string
  phoneE164: string
  email: string
  avatarUrl: string
  gender: 0 | 1 | 2
  birthDate?: string
  bio?: string
  signupChannel?: number
  accountStatus: number
  createdAt: string
  // Backward compatibility aliases
  nickname?: string
  phone?: string
  icon?: string
  iconUrl?: string
  contactPhone?: string
}

export interface MemberAddress {
  id: number
  memberId?: number
  recipientName: string
  recipientPhone: string
  isDefault: boolean
  addressLine: string
  district?: string
  city: string
  state: string
  postalCode: string
  countryCode?: string
  // Backward compatibility aliases
  name?: string
  phoneNumber?: string
  receiverPhone?: string
  defaultStatus?: number
  detailAddress?: string
  postCode?: string
  region?: string
}

export type MemberInfo = Member
export type UmsMember = Member
export type UmsMemberReceiveAddress = MemberAddress
