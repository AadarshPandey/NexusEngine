data "aws_region" "current" {}
data "aws_caller_identity" "current" {}

resource "aws_security_group" "search_sg" {
  name        = "${var.env}-search-sg"
  vpc_id      = var.vpc_id
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/8"]
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
      master_user_password = "AdminPassword123!"
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
        Action = "es:*"
        Principal = "*"
        Effect = "Allow"
        Resource = "arn:aws:es:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:domain/${var.env}-opensearch/*"
      }
    ]
  })
}
