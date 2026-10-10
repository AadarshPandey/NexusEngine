variable "env" { type = string }
variable "vpc_id" { type = string }
variable "subnet_ids" { type = list(string) }
variable "db_instance_class" { type = string }
variable "redis_node_type" { type = string }
