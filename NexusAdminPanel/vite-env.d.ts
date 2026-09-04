/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BASE_SERVER_URL: string
  readonly VITE_USE_OSS: string
  readonly VITE_OSS_UPLOAD_URL: string
  readonly VITE_MINIO_UPLOAD_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
