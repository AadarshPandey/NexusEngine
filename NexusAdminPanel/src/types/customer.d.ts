/**
 * Customer Bounded Context Type Definitions
 * Aligned with schema_redesign.md (customer schema) and BackendMigrationPlan.md
 */

export interface Member {
  id?: number
  username: string
  displayName?: string
  nickname?: string
  phoneE164?: string
  phone?: string
  email?: string
  avatarUrl?: string
  icon?: string
  gender?: number
  birthDate?: string
  birthday?: string
  bio?: string
  personalizedSignature?: string
  signupChannel?: number
  pointsBalance?: number
  integration?: number
  lifetimePointsEarned?: number
  growth?: number
  accountStatus?: number
  status?: number
  createdAt?: string
  lastLoginAt?: string
}

export interface MemberAddress {
  id?: number
  memberId?: number
  recipientName: string
  name?: string
  recipientPhone: string
  phoneNumber?: string
  isDefault?: boolean | number
  defaultStatus?: number
  addressLine: string
  detailAddress?: string
  district?: string
  region?: string
  city: string
  state: string
  province?: string
  postalCode?: string
  postCode?: string
  countryCode?: string
}

export type UmsMember = Member
export type UmsMemberReceiveAddress = MemberAddress
