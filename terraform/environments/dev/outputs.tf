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

output "redis_endpoint" {
  value = module.database.redis_endpoint
}

output "rabbitmq_endpoint" {
  value = module.messaging.rabbitmq_endpoint
}

output "opensearch_endpoint" {
  value = module.search.opensearch_endpoint
}

output "cloudfront_url" {
  value = module.frontend.cloudfront_url
}

output "s3_media_bucket" {
  value = module.frontend.s3_media_bucket_name
}
