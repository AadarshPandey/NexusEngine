import type { PageParam } from './common'
import type { OmsCompanyAddress } from './companyAddress'

export type OmsOrderReturnApply = {
  id: number
  orderId: number
  companyAddressId: number
  productId: number
  orderSn: string
  createTime: string
  memberUsername: string
  returnAmount: number
  returnName: string
  returnPhone: string
  status: number
  handleTime: string
  productPic: string
  productName: string
  productBrand: string
  productAttr: string
  productCount: number
  productPrice: number
  productRealPrice: number
  reason: string
  description: string
  proofPics: string
  handleNote: string
  handleMan: string
  receiveMan: string
  receiveTime: string
  receiveNote: string
}

export type ReturnApplyQueryParam = PageParam & {
  id?: number
  receiverKeyword?: string
  status?: number
  createTime?: string
  handleMan?: string
  handleTime?: string
}

export type OmsOrderReturnApplyResult = OmsOrderReturnApply & {
  companyAddress: OmsCompanyAddress
}

export type OmsUpdateStatusParam = {
  id: number
  companyAddressId: number
  returnAmount: number
  handleNote: string
  handleMan: string
  receiveNote: string
  receiveMan: string
  status: number
}
