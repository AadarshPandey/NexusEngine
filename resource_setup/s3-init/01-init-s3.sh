#!/usr/bin/env bash
set -e

echo "=== [LocalStack Init] Initializing S3 Buckets & Data ==="

# Create buckets
awslocal s3 mb s3://nexus-media || true
awslocal s3 mb s3://nexus-bucket || true

# Apply public read policy on public prefix for nexus-media
cat << 'EOF' > /tmp/public-policy-media.json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": ["s3:GetObject"],
      "Resource": ["arn:aws:s3:::nexus-media/public/*"]
    }
  ]
}
EOF
awslocal s3api put-bucket-policy --bucket nexus-media --policy file:///tmp/public-policy-media.json || true

# Apply public read policy on public prefix for nexus-bucket
cat << 'EOF' > /tmp/public-policy-bucket.json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": ["s3:GetObject"],
      "Resource": ["arn:aws:s3:::nexus-bucket/public/*"]
    }
  ]
}
EOF
awslocal s3api put-bucket-policy --bucket nexus-bucket --policy file:///tmp/public-policy-bucket.json || true

# Sync all extracted media assets into LocalStack S3 buckets
if [ -d "/s3-data/nexus-media" ]; then
    echo "=== [LocalStack Init] Syncing media assets to s3://nexus-media ==="
    awslocal s3 sync /s3-data/nexus-media s3://nexus-media --quiet

    echo "=== [LocalStack Init] Syncing legacy /public/pms alias to s3://nexus-media ==="
    awslocal s3 sync /s3-data/nexus-media/public/catalog s3://nexus-media/public/pms --quiet

    echo "=== [LocalStack Init] Syncing media assets to s3://nexus-bucket ==="
    awslocal s3 sync /s3-data/nexus-media s3://nexus-bucket --quiet
    awslocal s3 sync /s3-data/nexus-media/public/catalog s3://nexus-bucket/public/pms --quiet

    echo "=== [LocalStack Init] Sync completed successfully! ==="
fi

echo "=== [LocalStack Init] S3 ready and populated ==="
