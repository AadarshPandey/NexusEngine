import type { PageParam } from './common'
import type { OmsCompanyAddress } from './companyAddress'

export type OmsOrderReturnApply = {
  id: number
  orderId: number
  returnAddressId: number
  orderLineId: number
  memberUsername?: string
  orderNumber: string
  createdAt: string
  refundAmount: number
  contactName: string
  contactPhone: string
  status: number
  reviewedAt: string
  productImageUrl: string
  productName: string
  brandName: string
  skuAttributes: string
  quantity: number
  unitPrice: number
  unitRefundAmount: number
  reasonId: string
  customerComment: string
  evidenceUrls: string
  reviewNote: string
  reviewedByAdminId: string
  receivedByAdminId: string
  deliveredAt: string
  receiptNote: string
}

export type ReturnApplyQueryParam = PageParam & {
  id?: number
  receiverKeyword?: string
  status?: number
  createdAt?: string
  reviewedByAdminId?: string
  reviewedAt?: string
}

export type OmsOrderReturnApplyResult = OmsOrderReturnApply & {
  companyAddress: OmsCompanyAddress
}

export type OmsUpdateStatusParam = {
  id: number
  returnAddressId: number
  refundAmount: number
  reviewNote: string
  reviewedByAdminId: string
  receiptNote: string
  receivedByAdminId: string
  status: number
}
