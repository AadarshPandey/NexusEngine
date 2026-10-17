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

# Apply CORS configuration on buckets
cat << 'EOF' > /tmp/cors.json
{
  "CORSRules": [
    {
      "AllowedHeaders": ["*"],
      "AllowedMethods": ["GET", "PUT", "POST", "DELETE", "HEAD"],
      "AllowedOrigins": ["*"],
      "ExposeHeaders": ["ETag", "x-amz-request-id"]
    }
  ]
}
EOF
awslocal s3api put-bucket-cors --bucket nexus-media --cors-configuration file:///tmp/cors.json || true
awslocal s3api put-bucket-cors --bucket nexus-bucket --cors-configuration file:///tmp/cors.json || true

# Sync media assets into LocalStack S3 buckets
DATA_DIR="/s3-data/nexus-media"

if [ -d "$DATA_DIR" ] && [ "$(ls -A "$DATA_DIR" 2>/dev/null)" ]; then
    echo "=== [LocalStack Init] Syncing media assets from $DATA_DIR to s3://nexus-media ==="
    awslocal s3 sync "$DATA_DIR" s3://nexus-media --quiet

    if [ -d "$DATA_DIR/public/catalog" ]; then
        echo "=== [LocalStack Init] Syncing legacy /public/pms alias to s3://nexus-media ==="
        awslocal s3 sync "$DATA_DIR/public/catalog" s3://nexus-media/public/pms --quiet
    fi

    echo "=== [LocalStack Init] Syncing media assets to s3://nexus-bucket ==="
    awslocal s3 sync "$DATA_DIR" s3://nexus-bucket --quiet
    if [ -d "$DATA_DIR/public/catalog" ]; then
        awslocal s3 sync "$DATA_DIR/public/catalog" s3://nexus-bucket/public/pms --quiet
    fi

    echo "=== [LocalStack Init] Sync completed successfully! ==="
else
    echo "=== [LocalStack Init] No external media folder found at $DATA_DIR. Auto-generating essential seed SVG assets... ==="
    python3 - << 'PYEOF'
import os
import subprocess

def make_svg(path, text, bg="#4f46e5"):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    svg = f'''<svg xmlns="http://www.w3.org/2000/svg" width="400" height="400">
  <rect width="100%" height="100%" fill="{bg}"/>
  <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#ffffff" font-size="22" font-family="sans-serif">{text}</text>
</svg>'''
    with open(path, "w") as f:
        f.write(svg)

base = "/tmp/seed-media/public"
# Banners
make_svg(f"{base}/marketing/banners/electronics-sale.svg", "Summer Electronics Sale", "#2563eb")
make_svg(f"{base}/marketing/banners/fashion-week.svg", "Fashion Week 2026", "#7c3aed")
make_svg(f"{base}/marketing/banners/special-discount.svg", "Special 50% Discount", "#db2777")
make_svg(f"{base}/marketing/homepage/hero.svg", "NexusEngine Hero", "#059669")

# Avatars
make_svg(f"{base}/users/avatars/1/avatar.svg", "User Avatar", "#475569")

# Category Icons (1..30)
for cid in range(1, 31):
    make_svg(f"{base}/catalog/categories/{cid}/icon.svg", f"Category {cid}", "#0891b2")
    for scid in range(1, 10):
        make_svg(f"{base}/catalog/categories/{cid}/subcategories/{scid}/icon.svg", f"Subcategory {scid}", "#0d9488")

# Common Products (1..200)
for pid in range(1, 201):
    make_svg(f"{base}/catalog/products/{pid}/main.svg", f"Product {pid}", "#4f46e5")
    for g in range(1, 4):
        make_svg(f"{base}/catalog/products/{pid}/gallery/0{g}.svg", f"Gallery {pid}-{g}", "#6366f1")

print("Generated fallback seed media assets in /tmp/seed-media")
subprocess.run(["awslocal", "s3", "sync", "/tmp/seed-media", "s3://nexus-media/public", "--quiet"])
subprocess.run(["awslocal", "s3", "sync", "/tmp/seed-media/public/catalog", "s3://nexus-media/public/pms", "--quiet"])
subprocess.run(["awslocal", "s3", "sync", "/tmp/seed-media", "s3://nexus-bucket/public", "--quiet"])
PYEOF
    echo "=== [LocalStack Init] Fallback seed media populated into S3! ==="
fi

echo "=== [LocalStack Init] S3 ready and populated ==="
