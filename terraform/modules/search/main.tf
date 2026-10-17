data "aws_region" "current" {}
data "aws_caller_identity" "current" {}

resource "random_password" "opensearch_password" {
  length           = 16
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

resource "aws_secretsmanager_secret" "opensearch_credentials" {
  name                    = "${var.env}-opensearch-credentials"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "opensearch_credentials_val" {
  secret_id = aws_secretsmanager_secret.opensearch_credentials.id
  secret_string = jsonencode({
    endpoint = aws_opensearch_domain.main.endpoint
    username = "admin"
    password = random_password.opensearch_password.result
  })
}

resource "aws_security_group" "search_sg" {
  name        = "${var.env}-search-sg"
  description = "Security group for OpenSearch domain"
  vpc_id      = var.vpc_id

  dynamic "ingress" {
    for_each = var.allowed_security_group_id != "" ? [var.allowed_security_group_id] : []
    content {
      from_port       = 443
      to_port         = 443
      protocol        = "tcp"
      security_groups = [ingress.value]
    }
  }

  dynamic "ingress" {
    for_each = var.allowed_security_group_id == "" ? ["10.0.0.0/8"] : []
    content {
      from_port   = 443
      to_port     = 443
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

resource "aws_opensearch_domain" "main" {
  domain_name    = "${var.env}-opensearch"
  engine_version = "OpenSearch_2.11"

  cluster_config {
    instance_type  = var.opensearch_instance_type
    instance_count = 1
  }

  vpc_options {
    subnet_ids         = [var.subnet_ids[0]]
    security_group_ids = [aws_security_group.search_sg.id]
  }

  advanced_security_options {
    enabled                        = true
    internal_user_database_enabled = true
    master_user_options {
      master_user_name     = "admin"
      master_user_password = random_password.opensearch_password.result
    }
  }

  ebs_options {
    ebs_enabled = true
    volume_size = 10
  }

  access_policies = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action    = "es:*"
        Principal = "*"
        Effect    = "Allow"
        Resource  = "arn:aws:es:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:domain/${var.env}-opensearch/*"
      }
    ]
  })
}
