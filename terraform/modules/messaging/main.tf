resource "random_password" "mq_password" {
  length           = 24
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

resource "aws_secretsmanager_secret" "mq_credentials" {
  name                    = "${var.env}-rabbitmq-credentials"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "mq_credentials_val" {
  secret_id = aws_secretsmanager_secret.mq_credentials.id
  secret_string = jsonencode({
    endpoint = aws_mq_broker.rabbitmq.instances[0].endpoints[0]
    username = "admin"
    password = random_password.mq_password.result
  })
}

resource "aws_security_group" "mq_sg" {
  name        = "${var.env}-mq-sg"
  description = "Security group for Amazon MQ RabbitMQ"
  vpc_id      = var.vpc_id

  dynamic "ingress" {
    for_each = var.allowed_security_group_id != "" ? [var.allowed_security_group_id] : []
    content {
      from_port       = 5671
      to_port         = 5671
      protocol        = "tcp"
      security_groups = [ingress.value]
    }
  }

  dynamic "ingress" {
    for_each = var.allowed_security_group_id == "" ? ["10.0.0.0/8"] : []
    content {
      from_port   = 5671
      to_port     = 5671
      protocol    = "tcp"
      cidr_blocks = [ingress.value]
    }
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_mq_broker" "rabbitmq" {
  broker_name        = "${var.env}-rabbitmq"
  engine_type        = "RabbitMQ"
  engine_version     = "3.13"
  host_instance_type = var.mq_instance_type
  security_groups    = [aws_security_group.mq_sg.id]
  subnet_ids         = [var.subnet_ids[0]]

  user {
    username = "admin"
    password = random_password.mq_password.result
  }
}
