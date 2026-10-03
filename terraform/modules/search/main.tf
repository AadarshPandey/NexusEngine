variable "env" {}
variable "subnet_ids" { type = list(string) }
variable "opensearch_instance_type" {}

resource "aws_opensearch_domain" "main" {
  domain_name    = "${var.env}-opensearch"
  engine_version = "OpenSearch_2.11"

  cluster_config {
    instance_type  = var.opensearch_instance_type
    instance_count = 1
  }

  vpc_options {
    subnet_ids = [var.subnet_ids[0]]
  }
  
  advanced_security_options {
    enabled                        = true
    internal_user_database_enabled = true
    master_user_options {
      master_user_name     = "admin"
      # ADD CREDENTIALS HERE
      # Note: In production, consider using AWS Secrets Manager or variable passing
      master_user_password = "ADD_YOUR_OS_PASSWORD_HERE!123"
    }
  }
}
