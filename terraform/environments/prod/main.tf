terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

module "networking" {
  source   = "../../modules/networking"
  env      = "prod"
  vpc_cidr = "10.1.0.0/16"
}

module "cluster" {
  source              = "../../modules/cluster"
  env                 = "prod"
  vpc_id              = module.networking.vpc_id
  subnet_ids          = module.networking.private_subnet_ids
  node_instance_types = ["m5.large", "m5.xlarge"]
}

module "database" {
  source                    = "../../modules/database"
  env                       = "prod"
  vpc_id                    = module.networking.vpc_id
  subnet_ids                = module.networking.private_subnet_ids
  db_instance_class         = "db.r5.large"
  redis_node_type           = "cache.m5.large"
  allowed_security_group_id = module.cluster.cluster_security_group_id
}

module "messaging" {
  source                    = "../../modules/messaging"
  env                       = "prod"
  vpc_id                    = module.networking.vpc_id
  subnet_ids                = module.networking.private_subnet_ids
  mq_instance_type          = "mq.m5.large"
  allowed_security_group_id = module.cluster.cluster_security_group_id
}

module "search" {
  source                    = "../../modules/search"
  env                       = "prod"
  vpc_id                    = module.networking.vpc_id
  subnet_ids                = module.networking.private_subnet_ids
  opensearch_instance_type  = "m5.large.search"
  allowed_security_group_id = module.cluster.cluster_security_group_id
}

module "frontend" {
  source = "../../modules/frontend"
  env    = "prod"
}
