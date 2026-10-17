output "rabbitmq_endpoint" {
  value = aws_mq_broker.rabbitmq.instances[0].endpoints[0]
}
output "mq_secret_arn" {
  value = aws_secretsmanager_secret.mq_credentials.arn
}
