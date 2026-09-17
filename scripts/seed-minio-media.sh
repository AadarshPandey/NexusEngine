#!/bin/bash
# Shell script to copy image1.jpeg to all required MinIO paths as per Phase 3

echo "Copying image1.jpeg to MinIO via mc..."
docker exec minio mc mb myminio/nexus-media/public 2>/dev/null || true
docker exec minio mc anonymous set download myminio/nexus-media/public

# Execute SQL via psql locally
URLS=$(PGPASSWORD=postgres psql -h localhost -p 5433 -U postgres -d nexuscore -t -c "
SELECT logo FROM pms_brand WHERE logo IS NOT NULL
UNION SELECT big_pic FROM pms_brand WHERE big_pic IS NOT NULL
UNION SELECT icon FROM pms_product_category WHERE icon IS NOT NULL
UNION SELECT pic FROM pms_product WHERE pic IS NOT NULL
UNION SELECT media_url FROM pms_product_media WHERE media_url IS NOT NULL
UNION SELECT pic FROM pms_sku_stock WHERE pic IS NOT NULL
" | sed 's/ //g' | grep -v "^$")

for url in $URLS; do
    path=$(echo $url | sed 's|http://localhost:9000/nexus-media/public/||')
    docker exec minio mc cp /image1.jpeg "myminio/nexus-media/public/$path" >/dev/null
done

docker exec minio mc cp /image1.jpeg myminio/nexus-media/public/marketing/banners/banner1.webp >/dev/null
docker exec minio mc cp /image1.jpeg myminio/nexus-media/public/users/avatars/default/avatar.webp >/dev/null
echo "Done"
