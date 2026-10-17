output "aurora_endpoint" {
  value = aws_rds_cluster.postgresql.endpoint
}
output "redis_endpoint" {
  value = aws_elasticache_cluster.redis.cache_nodes[0].address
}
output "db_secret_arn" {
  value = aws_secretsmanager_secret.db_credentials.arn
}
