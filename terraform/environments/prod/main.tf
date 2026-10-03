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
  # ADD CREDENTIALS HERE
  # Uncomment the lines below and add your AWS credentials if not using profiles or env vars
  # access_key = "ADD_AWS_ACCESS_KEY_HERE"
  # secret_key = "ADD_AWS_SECRET_KEY_HERE"
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
  source            = "../../modules/database"
  env               = "prod"
  vpc_id            = module.networking.vpc_id
  subnet_ids        = module.networking.private_subnet_ids
  db_instance_class = "db.r5.large"
  redis_node_type   = "cache.m5.large"
}

module "messaging" {
  source           = "../../modules/messaging"
  env              = "prod"
  subnet_ids       = module.networking.private_subnet_ids
  mq_instance_type = "mq.m5.large"
}

module "search" {
  source                   = "../../modules/search"
  env                      = "prod"
  subnet_ids               = module.networking.private_subnet_ids
  opensearch_instance_type = "m5.large.search"
}

module "frontend" {
  source = "../../modules/frontend"
  env    = "prod"
}
