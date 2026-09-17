#!/bin/bash
set -e

# Wait for MinIO to be available
echo "Checking MinIO status..."
until curl -s http://localhost:9000/minio/health/live; do
    echo "Waiting for MinIO..."
    sleep 2
done

echo "MinIO is up. Setting up mc..."
# Setup alias for mc (assuming admin/admin123 or minioadmin/minioadmin)
mc alias set myminio http://localhost:9000 minioadmin minioadmin || mc alias set myminio http://localhost:9000 admin admin123

echo "Creating bucket nexus-media if it doesn't exist..."
mc mb myminio/nexus-media || true

echo "Setting public read policy..."
cat << 'EOF' > /tmp/public-policy.json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {"AWS": ["*"]},
      "Action": ["s3:GetObject"],
      "Resource": ["arn:aws:s3:::nexus-media/public/*"]
    }
  ]
}
EOF
mc anonymous set-json /tmp/public-policy.json myminio/nexus-media

echo "Uploading placeholder image to catalog paths..."

IMAGE_PATH="/home/aadarsh/Documents/NexusEngine/image1.jpeg"

if [ ! -f "$IMAGE_PATH" ]; then
    echo "Placeholder image $IMAGE_PATH not found! Skipping upload."
    exit 1
fi

# Example uploads for the categories, brands, and products created in V2
# Just upload it to a few standard paths to demonstrate
mc cp "$IMAGE_PATH" myminio/nexus-media/public/marketing/banners/electronics-sale.webp
mc cp "$IMAGE_PATH" myminio/nexus-media/public/marketing/banners/fashion-week.webp

mc cp "$IMAGE_PATH" myminio/nexus-media/public/catalog/brands/1-apple/logo.webp
mc cp "$IMAGE_PATH" myminio/nexus-media/public/catalog/brands/2-samsung/logo.webp

mc cp "$IMAGE_PATH" myminio/nexus-media/public/catalog/products/1-iphone-17/main.webp
mc cp "$IMAGE_PATH" myminio/nexus-media/public/catalog/products/2-galaxy-s25/main.webp

mc cp "$IMAGE_PATH" myminio/nexus-media/public/catalog/skus/APP-IPH-1-BLK-128/main.webp
mc cp "$IMAGE_PATH" myminio/nexus-media/public/catalog/skus/SAM-GAL-2-PHA-256/main.webp

echo "MinIO media setup complete!"
