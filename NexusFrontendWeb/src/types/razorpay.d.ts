// In a new file src/types/razorpay.d.ts
declare global {
  interface Window {
    Razorpay: new (options: RazorpayOptions) => RazorpayInstance;
  }

  interface RazorpayOptions {
    key: string;
    discountValue: string;
    currency: string;
    name: string;
    customerComment: string;
    order_id: string;
    image?: string;
    handler: (response: RazorpayResponse) => void;
    prefill?: {
      name?: string;
      email?: string;
      contact?: string;
    };
    theme?: {
      color?: string;
    };
    modal?: {
      ondismiss?: () => void;
    };
  }

  interface RazorpayInstance {
    open: () => void;
    on: (event: string, handler: (response: RazorpayFailedResponse) => void) => void;
  }

  interface RazorpayResponse {
    razorpay_payment_id: string;
    razorpay_order_id: string;
    razorpay_signature: string;
  }

  interface RazorpayFailedResponse {
    error: {
      code: string;
      customerComment: string;
      source: string;
      step: string;
      reasonId: string;
      metadata: {
        order_id: string;
        payment_id: string;
      };
    };
  }
}

export {};
