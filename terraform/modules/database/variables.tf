variable "env" { type = string }
variable "vpc_id" { type = string }
variable "subnet_ids" { type = list(string) }
variable "db_instance_class" { type = string }
variable "redis_node_type" { type = string }
variable "allowed_security_group_id" {
  type        = string
  description = "Optional security group ID allowed to connect to database and redis (e.g. EKS cluster SG)"
  default     = ""
}
