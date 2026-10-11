const getS3BaseUrl = (): string => {
  if (import.meta.env.VITE_S3_BASE_URL) {
    return import.meta.env.VITE_S3_BASE_URL;
  }
  if (typeof window !== 'undefined' && window.location?.hostname) {
    return `http://${window.location.hostname}:4566`;
  }
  return 'http://localhost:4566';
};

const BUCKET_NAME = 'nexus-media';

/**
 * Resolves an image URL or bucket-relative key into a fully-qualified LocalStack S3 URL.
 * Safely rewrites legacy MinIO (port 9000), legacy module paths (/pms/, /sms/, /ums/),
 * and old bucket names (nexus-bucket -> nexus-media).
 */
export const resolveImageUrl = (urlOrPath: string | null | undefined): string => {
  if (!urlOrPath) return '/placeholder-product.png';

  let url = urlOrPath.trim();
  if (!url) return '/placeholder-product.png';

  const baseS3 = getS3BaseUrl();

  // If it's a third-party external image (e.g., placeholder, unsplash, cdn) not pointing to local/minio/localstack
  if (
    (url.startsWith('http://') || url.startsWith('https://')) &&
    !url.includes('localhost') &&
    !url.includes('127.0.0.1') &&
    !url.includes('localstack') &&
    !url.includes('nexus-media') &&
    !url.includes('nexus-bucket')
  ) {
    return url;
  }

  // Rewrite legacy hosts/ports to active LocalStack endpoint
  url = url
    .replace(/^https?:\/\/(localhost|127\.0\.0\.1):9000/, baseS3)
    .replace(/^https?:\/\/localstack:4566/, baseS3)
    .replace(/^https?:\/\/(localhost|127\.0\.0\.1):4566/, baseS3);

  // Normalize bucket name: replace nexus-bucket with nexus-media
  url = url.replace(/\/nexus-bucket(\/|$)/g, `/${BUCKET_NAME}$1`);

  // Normalize legacy prefixes:
  // /public/pms/ -> /public/catalog/
  // /pms/ -> /catalog/
  // /public/sms/ -> /public/marketing/
  // /sms/ -> /marketing/
  // /public/ums/ -> /public/users/
  // /ums/ -> /users/
  url = url
    .replace(/\/public\/pms\//g, '/public/catalog/')
    .replace(/\/pms\//g, '/catalog/')
    .replace(/\/public\/sms\//g, '/public/marketing/')
    .replace(/\/sms\//g, '/marketing/')
    .replace(/\/public\/ums\//g, '/public/users/')
    .replace(/\/ums\//g, '/users/');

  // If already an absolute URL (http:// or https://)
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }

  // Handle relative keys / paths
  const cleanPath = url.startsWith('/') ? url.slice(1) : url;

  if (cleanPath.startsWith('nexus-media/')) {
    return `${baseS3}/${cleanPath}`;
  }
  if (cleanPath.startsWith('public/')) {
    return `${baseS3}/${BUCKET_NAME}/${cleanPath}`;
  }
  return `${baseS3}/${BUCKET_NAME}/public/${cleanPath}`;
};
