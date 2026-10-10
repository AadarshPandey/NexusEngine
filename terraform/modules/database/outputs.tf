output "aurora_endpoint" {
  value = aws_rds_cluster.postgresql.endpoint
}
output "redis_endpoint" {
  value = aws_elasticache_cluster.redis.cache_nodes[0].address
}
