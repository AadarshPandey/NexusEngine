import type { OssPolicyResult } from '@/types/oss'
import http from '@/utils/http'

/**
 */
export function ossPolicyAPI() {
  return http<OssPolicyResult>({
    url: '/aliyun/oss/policy',
    method: 'get',
  })
}
