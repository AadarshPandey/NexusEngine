# This is the root entry point connecting modules if you want to deploy all environments from here,
# or it can serve as a shared resources definition.
# Often, users run `terraform apply` directly inside `environments/dev` or `environments/prod`.
# If you run it here, you can define workspaces or common resources.

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

# Example of calling the dev environment directly from root (optional)
# module "dev_environment" {
#   source = "./environments/dev"
# }

# Example of calling the prod environment directly from root (optional)
# module "prod_environment" {
#   source = "./environments/prod"
# }
