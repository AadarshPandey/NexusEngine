output "opensearch_endpoint" {
  value = aws_opensearch_domain.main.endpoint
}
output "opensearch_secret_arn" {
  value = aws_secretsmanager_secret.opensearch_credentials.arn
}
