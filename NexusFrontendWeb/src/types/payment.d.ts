/**
 * Payment Bounded Context Types for Customer Storefront
 * Aligned with schema_redesign.md and FrontendMigrationPlan.md
 */

export interface PaymentTransaction {
  id?: number
  orderId: number
  provider: 'RAZORPAY' | 'STRIPE'
  providerTxnId?: string
  idempotencyKey?: string
  txnType: 'CHARGE' | 'REFUND'
  amount: number
  currencyCode: string
  status: string
  createdAt?: string
  updatedAt?: string
}

export interface RazorpayOrderResponse {
  razorpay_order_id: string
  amount: number
  currency: string
  orderId: number
}
