variable "env" {}
variable "subnet_ids" { type = list(string) }
variable "mq_instance_type" {}

resource "aws_mq_broker" "rabbitmq" {
  broker_name        = "${var.env}-rabbitmq"
  engine_type        = "RabbitMQ"
  engine_version     = "3.12.13"
  host_instance_type = var.mq_instance_type
  security_groups    = []
  subnet_ids         = [var.subnet_ids[0]]
  
  user {
    username = "admin"
    # ADD CREDENTIALS HERE
    # Note: In production, consider using AWS Secrets Manager or variable passing
    password = "ADD_YOUR_MQ_PASSWORD_HERE!123"
  }
}
