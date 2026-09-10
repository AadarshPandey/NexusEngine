#!/bin/bash

# Ensure a Docker Hub username was provided
if [ -z "$1" ]; then
    echo "Usage: ./push_to_dockerhub.sh <your_dockerhub_username>"
    echo "Example: ./push_to_dockerhub.sh aadarshpandey"
    exit 1
fi

USERNAME=$1

echo "======================================"
echo " Docker Hub Publishing Script"
echo " Username: $USERNAME"
echo "======================================"

echo ""
echo "Step 1: Logging into Docker Hub"
echo "Please enter your Docker Hub password or Personal Access Token when prompted."
docker login -u "$USERNAME"
if [ $? -ne 0 ]; then
    echo "❌ Docker login failed. Exiting."
    exit 1
fi

echo ""
echo "Step 2: Building all Docker images"
# We build them using docker-compose to ensure they use the Dockerfiles we just created
docker compose build

echo ""
echo "Step 3: Tagging images with your Docker Hub namespace"
# Tag backend services
docker tag nexuscore/nexus-application:latest "$USERNAME/nexus-engine-backend:latest"
docker tag nexuscore/nexus-search:latest "$USERNAME/nexus-engine-search:latest"

# Tag frontend services
docker tag nexuscore/nexus-frontend-web:latest "$USERNAME/nexus-engine-frontend-store:latest"
docker tag nexuscore/nexus-admin-panel:latest "$USERNAME/nexus-engine-frontend-admin:latest"

echo ""
echo "Step 4: Pushing images to Docker Hub"
# Push backend services
docker push "$USERNAME/nexus-engine-backend:latest"
docker push "$USERNAME/nexus-engine-search:latest"

# Push frontend services
docker push "$USERNAME/nexus-engine-frontend-store:latest"
docker push "$USERNAME/nexus-engine-frontend-admin:latest"

echo ""
echo "✅ All images successfully pushed to Docker Hub!"
echo "You can now view your repositories at: https://hub.docker.com/u/$USERNAME"
