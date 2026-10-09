export type OmsOrderSetting = {
  /** ID */
  id: number
  flashPaymentTimeoutMin?: number
  paymentTimeoutMin?: number
  autoConfirmReceiptDays?: number
  autoCompleteDays?: number
  reviewWindowDays?: number
}
