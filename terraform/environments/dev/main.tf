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
  env      = "dev"
  vpc_cidr = "10.0.0.0/16"
}

module "cluster" {
  source              = "../../modules/cluster"
  env                 = "dev"
  vpc_id              = module.networking.vpc_id
  subnet_ids          = module.networking.private_subnet_ids
  node_instance_types = ["t3.micro"]
}

module "database" {
  source                    = "../../modules/database"
  env                       = "dev"
  vpc_id                    = module.networking.vpc_id
  subnet_ids                = module.networking.private_subnet_ids
  db_instance_class         = "db.t3.micro"
  redis_node_type           = "cache.t3.micro"
  allowed_security_group_id = module.cluster.cluster_security_group_id
}

module "messaging" {
  source                    = "../../modules/messaging"
  env                       = "dev"
  vpc_id                    = module.networking.vpc_id
  subnet_ids                = module.networking.private_subnet_ids
  mq_instance_type          = "mq.t3.micro"
  allowed_security_group_id = module.cluster.cluster_security_group_id
}

module "search" {
  source                    = "../../modules/search"
  env                       = "dev"
  vpc_id                    = module.networking.vpc_id
  subnet_ids                = module.networking.private_subnet_ids
  opensearch_instance_type  = "t3.small.search"
  allowed_security_group_id = module.cluster.cluster_security_group_id
}

module "frontend" {
  source = "../../modules/frontend"
  env    = "dev"
}
