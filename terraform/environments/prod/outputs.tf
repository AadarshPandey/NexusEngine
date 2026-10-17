output "vpc_id" {
  value = module.networking.vpc_id
}

output "cluster_name" {
  value = module.cluster.cluster_name
}

output "cluster_endpoint" {
  value = module.cluster.cluster_endpoint
}

output "aurora_endpoint" {
  value = module.database.aurora_endpoint
}

output "db_secret_arn" {
  value = module.database.db_secret_arn
}

output "redis_endpoint" {
  value = module.database.redis_endpoint
}

output "rabbitmq_endpoint" {
  value = module.messaging.rabbitmq_endpoint
}

output "rabbitmq_secret_arn" {
  value = module.messaging.mq_secret_arn
}

output "opensearch_endpoint" {
  value = module.search.opensearch_endpoint
}

output "opensearch_secret_arn" {
  value = module.search.opensearch_secret_arn
}

output "storefront_cloudfront_url" {
  value = module.frontend.storefront_cloudfront_url
}

output "admin_cloudfront_url" {
  value = module.frontend.admin_cloudfront_url
}

output "cloudfront_url" {
  value = module.frontend.cloudfront_url
}

output "s3_storefront_bucket" {
  value = module.frontend.s3_storefront_bucket_name
}

output "s3_admin_bucket" {
  value = module.frontend.s3_admin_bucket_name
}

output "s3_media_bucket" {
  value = module.frontend.s3_media_bucket_name
}
