import type { S3PresignedPolicy, S3UploadResult } from '@/types/s3'
import http from '@/utils/http'
import { LOCALSTACK_S3_BASE, S3_BUCKET_NAME } from '@/utils/constant'

/**
 * Fetch S3 presigned upload policy from backend
 */
export function s3PolicyAPI() {
  return http<S3PresignedPolicy>({
    url: '/admin/s3/policy',
    method: 'get',
  })
}

/**
 * Direct file upload via backend proxy
 */
export function s3UploadAPI(file: File, folder = 'media') {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('folder', folder)
  return http<S3UploadResult>({
    url: '/admin/s3/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

/**
 * Direct client-to-LocalStack S3 upload using presigned policy
 */
export async function uploadDirectToLocalStack(
  file: File,
  folder = 'uploads'
): Promise<string> {
  const res = await s3PolicyAPI()
  const policy = res.data
  const fileKey = `${policy.dir}/${Date.now()}_${file.name}`

  const formData = new FormData()
  formData.append('key', fileKey)
  formData.append('AWSAccessKeyId', policy.accessKeyId)
  formData.append('policy', policy.policy)
  formData.append('signature', policy.signature)
  formData.append('file', file)

  const uploadEndpoint = policy.host || `${LOCALSTACK_S3_BASE}/${S3_BUCKET_NAME}`
  await fetch(uploadEndpoint, {
    method: 'POST',
    body: formData,
  })

  return `${uploadEndpoint}/${fileKey}`
}

// Backward-compatible alias
export const ossPolicyAPI = s3PolicyAPI
