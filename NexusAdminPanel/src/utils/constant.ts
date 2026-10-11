export const BASE_API = 'http://localhost:8080';
export const LOCALSTACK_S3_BASE = 'http://localhost:4566';
export const S3_BUCKET_NAME = 'nexus-media';
export const S3_PUBLIC_URL_PREFIX = `${LOCALSTACK_S3_BASE}/${S3_BUCKET_NAME}`;
export const UPLOAD_BASE = LOCALSTACK_S3_BASE;

export const couponTypes = [
  {
    label: 'All-site Coupon',
    value: 0,
  },
  {
    label: 'Member Coupon',
    value: 1,
  },
  {
    label: 'Shopping Coupon',
    value: 2,
  },
  {
    label: 'Registration Coupon',
    value: 3,
  },
]

export const couponPlatforms = [
  {
    label: 'All Platforms',
    value: 0,
  },
  {
    label: 'Mobile Platform',
    value: 1,
  },
  {
    label: 'PC Platform',
    value: 2,
  },
]
