import type { S3PolicyResult, S3UploadResult } from '@/types/s3'
import http from '@/utils/http'
import { LOCALSTACK_S3_BASE, S3_BUCKET_NAME } from '@/utils/constant'

/**
 * Fetch S3 presigned upload policy from backend
 */
export function s3PolicyAPI() {
  return http<S3PolicyResult>({
    url: '/api/admin/infra/storage/presign',
    method: 'get',
  })
}

/**
 * Direct file upload via backend proxy / presign
 */
export function s3UploadAPI(file: File, folder = 'media') {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('folder', folder)
  return http<S3UploadResult>({
    url: '/api/admin/infra/storage/upload',
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
  const policy = await s3PolicyAPI()
  const fileKey = `${policy.dir || folder}/${Date.now()}_${file.name}`

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
