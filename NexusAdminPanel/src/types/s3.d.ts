/**
 * LocalStack S3 Presigned Policy & Upload Types
 */
export type S3PresignedPolicy = {
  accessKeyId: string
  policy: string
  signature: string
  dir: string
  host: string
  bucket?: string
  callback?: string
}

export type S3PolicyResult = S3PresignedPolicy

export type S3UploadResult = {
  url: string
  key: string
  bucket: string
  etag?: string
  name?: string
}

// Backward-compatible type aliases for legacy references
export type OssPolicyResult = S3PresignedPolicy
export type MinioUploadResult = S3UploadResult
