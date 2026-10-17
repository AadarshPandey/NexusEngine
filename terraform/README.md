# NexusEngine Infrastructure as Code (Terraform)

Production-grade AWS infrastructure for the NexusEngine enterprise e-commerce platform, structured following modular monolithic cloud architecture and FAANG-level security standards.

---

## Architecture Overview

```
                                      Internet
                                         │
                         ┌───────────────┴───────────────┐
                         ▼                               ▼
                 [CloudFront: Storefront]        [CloudFront: Admin]
                         │                               │
                         ▼                               ▼
                 [S3: Storefront HTML/JS]        [S3: Admin HTML/JS]
                         │                               │
                         └───────────────┬───────────────┘
                                         ▼
                            [S3: Media Storage Bucket]
                               (CORS enabled, public-read)

                                     Virtual Private Cloud (VPC)
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │  Multi-AZ Public Subnets (NAT Gateway + Internet Gateway)                   │
  │                                                                             │
  │  Multi-AZ Private Subnets:                                                  │
  │  ┌───────────────────────┐   ┌───────────────────────────────────────────┐  │
  │  │   Amazon EKS Cluster  │──▶│  Aurora PostgreSQL (nexuscore DB)         │  │
  │  │   (Spring Boot App)   │   │  (Credentials in AWS Secrets Manager)     │  │
  │  │   [Cluster SG]        │──▶│  ElastiCache Redis                        │  │
  │  │                       │──▶│  Amazon MQ (RabbitMQ 3.13)                │  │
  │  │                       │──▶│  Amazon OpenSearch (Catalog Search)       │  │
  │  └───────────────────────┘   └───────────────────────────────────────────┘  │
  └─────────────────────────────────────────────────────────────────────────────┘
```

---

## Directory Structure

```
terraform/
├── main.tf                    # Root entry point delegating to default environment
├── environments/
│   ├── dev/                   # Development environment (t3.micro, single-AZ OpenSearch)
│   │   ├── main.tf
│   │   └── outputs.tf
│   └── prod/                  # Production environment (m5.large, multi-AZ, Aurora HA)
│       ├── main.tf
│       └── outputs.tf
└── modules/
    ├── networking/            # Multi-AZ VPC, public/private subnets, NAT Gateway, EKS tags
    ├── cluster/               # Amazon EKS cluster, managed node groups, IAM policies
    ├── database/              # Aurora PostgreSQL + ElastiCache Redis + Secrets Manager
    ├── search/                # Amazon OpenSearch Domain + Secrets Manager credentials
    ├── messaging/             # Amazon MQ RabbitMQ Broker + Secrets Manager credentials
    └── frontend/              # S3 buckets (Storefront, Admin, Media) + CloudFront OAC CDN
```

---

## Security & Best Practices

1. **Zero Hardcoded Secrets**:
   - Master passwords for Aurora PostgreSQL, Amazon MQ, and OpenSearch are dynamically generated via `random_password` and securely stored in **AWS Secrets Manager**.
2. **Multi-AZ High Availability**:
   - VPC subnets span multiple Availability Zones dynamically via `data.aws_availability_zones`.
   - RDS and ElastiCache subnet groups cover multiple AZs, preventing single-point-of-failure outages.
3. **Least-Privilege Security Groups**:
   - Database, Redis, RabbitMQ, and OpenSearch ingress rules accept traffic exclusively from the Amazon EKS cluster security group (`allowed_security_group_id`), denying arbitrary network access.
4. **CloudFront Origin Access Control (OAC)**:
   - S3 frontend buckets block all public ACLs/policies; static website files are served exclusively through authenticated CloudFront distributions via SigV4 OAC.
5. **Media Asset Storage (`nexus-media`)**:
   - Media bucket configured with proper CORS rules (GET/HEAD/PUT/POST/DELETE) for cross-origin image uploads and serving.

---

## Validation & Deployment

### Check Formatting & Validate

```bash
# Check formatting
terraform fmt -check -recursive

# Validate root module
terraform init -backend=false
terraform validate

# Validate dev environment
cd environments/dev
terraform init -backend=false
terraform validate

# Validate prod environment
cd ../prod
terraform init -backend=false
terraform validate
```

### Deploying an Environment

```bash
cd environments/dev
terraform init
terraform plan -out=tfplan
terraform apply tfplan
```
