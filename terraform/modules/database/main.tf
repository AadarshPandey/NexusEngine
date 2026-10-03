variable "env" {}
variable "vpc_id" {}
variable "subnet_ids" { type = list(string) }
variable "db_instance_class" {}
variable "redis_node_type" {}

resource "aws_db_subnet_group" "main" {
  name       = "${var.env}-db-subnet-group"
  subnet_ids = var.subnet_ids
}

resource "aws_rds_cluster" "postgresql" {
  cluster_identifier      = "${var.env}-aurora-cluster"
  engine                  = "aurora-postgresql"
  database_name           = "mydb"
  master_username         = "postgres"
  # ADD CREDENTIALS HERE
  # Note: In production, consider using AWS Secrets Manager or variable passing
  master_password         = "ADD_YOUR_DB_PASSWORD_HERE!123"
  skip_final_snapshot     = true
  db_subnet_group_name    = aws_db_subnet_group.main.name
}

resource "aws_rds_cluster_instance" "postgresql_instances" {
  count              = 2
  identifier         = "${var.env}-aurora-instance-${count.index}"
  cluster_identifier = aws_rds_cluster.postgresql.id
  instance_class     = var.db_instance_class
  engine             = aws_rds_cluster.postgresql.engine
  engine_version     = aws_rds_cluster.postgresql.engine_version
}

resource "aws_elasticache_subnet_group" "main" {
  name       = "${var.env}-redis-subnet-group"
  subnet_ids = var.subnet_ids
}

resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "${var.env}-redis"
  engine               = "redis"
  node_type            = var.redis_node_type
  num_cache_nodes      = 1
  subnet_group_name    = aws_elasticache_subnet_group.main.name
}
