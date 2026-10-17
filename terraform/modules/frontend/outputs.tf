output "cloudfront_url" {
  description = "Storefront CloudFront distribution URL (alias)"
  value       = aws_cloudfront_distribution.frontend.domain_name
}
output "storefront_cloudfront_url" {
  description = "Storefront CloudFront distribution URL"
  value       = aws_cloudfront_distribution.frontend.domain_name
}
output "admin_cloudfront_url" {
  description = "Admin panel CloudFront distribution URL"
  value       = aws_cloudfront_distribution.admin.domain_name
}
output "s3_bucket_name" {
  description = "Storefront S3 bucket name (alias)"
  value       = aws_s3_bucket.frontend.bucket
}
output "s3_storefront_bucket_name" {
  description = "Storefront S3 bucket name"
  value       = aws_s3_bucket.frontend.bucket
}
output "s3_admin_bucket_name" {
  description = "Admin panel S3 bucket name"
  value       = aws_s3_bucket.admin.bucket
}
output "s3_media_bucket_name" {
  description = "Media storage S3 bucket name"
  value       = aws_s3_bucket.media.bucket
}
