terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

variable "aws_region" {
  description = "AWS deployment region"
  type        = string
  default     = "us-east-1"
}

# Root entry point delegates to the dev environment by default.
# For environment-specific deploys, navigate to environments/dev or environments/prod.
module "nexus_dev" {
  source = "./environments/dev"
}

output "dev_aurora_endpoint" {
  description = "Aurora PostgreSQL cluster endpoint"
  value       = module.nexus_dev.aurora_endpoint
}

output "dev_storefront_cloudfront_url" {
  description = "Storefront CDN distribution URL"
  value       = module.nexus_dev.storefront_cloudfront_url
}

output "dev_admin_cloudfront_url" {
  description = "Admin panel CDN distribution URL"
  value       = module.nexus_dev.admin_cloudfront_url
}

output "dev_s3_media_bucket" {
  description = "Media assets bucket name"
  value       = module.nexus_dev.s3_media_bucket
}
