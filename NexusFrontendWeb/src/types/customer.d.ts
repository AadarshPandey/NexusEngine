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
}

export type MemberInfo = Member
