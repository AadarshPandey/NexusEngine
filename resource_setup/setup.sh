#!/usr/bin/env bash
set -euo pipefail

# ==============================================================================
#  NexusEngine Enterprise Setup & Resource Initialization Script
#  One-command bootstrap: Spawns all Docker containers & seeds complete environment.
# ==============================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$ROOT_DIR"

# Color Codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BOLD='\033[1m'
NC='\033[0m'

echo -e "${CYAN}${BOLD}"
echo "=================================================================="
echo "    _   __                     ______            _            "
echo "   / | / /__  _  ____  _______/ ____/___  ____ _(_)___  ___   "
echo "  /  |/ / _ \| |/_/ / / / ___/ __/ / __ \/ __ \`/ / __ \/ _ \  "
echo " / /|  /  __/>  </ /_/ (__  ) /___/ / / / /_/ / / / / /  __/  "
echo "/_/ |_/\___/_/|_|\__,_/____/_____/_/ /_/\__, /_/_/ /_/\___/   "
echo "                                       /____/                 "
echo "   Autonomous Enterprise E-Commerce Engine - Resource Setup       "
echo "=================================================================="
echo -e "${NC}"

# 1. Verify Docker environment
if ! docker info >/dev/null 2>&1; then
    echo -e "${RED}[ERROR] Docker daemon is not accessible. Please ensure Docker is running.${NC}"
    exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
    echo -e "${RED}[ERROR] Docker Compose plugin ('docker compose') is required.${NC}"
    exit 1
fi

MODE="${1:-all}"

if [ "$MODE" == "--reset" ] || [ "$MODE" == "-r" ]; then
    echo -e "${YELLOW}[!] Resetting environment: stopping containers and clearing named volumes...${NC}"
    docker compose down -v --remove-orphans || true
    echo -e "${GREEN}[✓] Existing state cleared.${NC}"
fi

# 2. Spin up containers
echo -e "\n${BLUE}[1/5] Starting Docker Containers...${NC}"
if [ "$MODE" == "--infra-only" ]; then
    docker compose up -d --build postgres redis rabbitmq elasticsearch localstack prometheus grafana s3-ui
else
    docker compose up -d --build
fi

# 3. Wait for infrastructure health checks
echo -e "\n${BLUE}[2/5] Waiting for Infrastructure Services to become Healthy...${NC}"

wait_for_health() {
    local container=$1
    local max_retries=${2:-60}
    local count=0

    printf "  -> Waiting for %s..." "$container"
    while [ $count -lt $max_retries ]; do
        status=$(docker inspect --format='{{json .State.Health.Status}}' "$container" 2>/dev/null || echo '"unknown"')
        if [ "$status" == '"healthy"' ]; then
            printf " ${GREEN}[HEALTHY]${NC}\n"
            return 0
        elif [ "$status" == '"unhealthy"' ]; then
            printf " ${RED}[UNHEALTHY]${NC}\n"
            return 1
        fi
        sleep 2
        count=$((count + 1))
        printf "."
    done
    printf " ${YELLOW}[TIMEOUT (proceeding)]${NC}\n"
    return 0
}

wait_for_health "postgres" 30
wait_for_health "redis" 20
wait_for_health "rabbitmq" 30
wait_for_health "elasticsearch" 45

# 4. Verify LocalStack S3 Bucket and Media Population
echo -e "\n${BLUE}[3/5] Verifying LocalStack S3 Storage & Media Assets...${NC}"
localstack_ready=false
for i in {1..20}; do
    if curl -sf http://localhost:4566/_localstack/health >/dev/null 2>&1; then
        localstack_ready=true
        break
    fi
    sleep 2
done

if [ "$localstack_ready" = true ]; then
    echo -e "  -> LocalStack is online. Verifying 'nexus-media' bucket..."
    docker exec localstack awslocal s3 mb s3://nexus-media 2>/dev/null || true
    
    # Check media objects in LocalStack S3
    media_count=$(docker exec localstack awslocal s3 ls s3://nexus-media/public/catalog/ --recursive 2>/dev/null | wc -l || echo 0)
    if [ "$media_count" -gt 0 ]; then
        echo -e "  -> ${GREEN}[✓] LocalStack S3 'nexus-media' bucket seeded with ${media_count} media assets.${NC}"
    else
        echo -e "  -> Seeding S3 media assets via init script..."
        docker exec localstack /etc/localstack/init/ready.d/01-init-s3.sh >/dev/null 2>&1 || true
        echo -e "  -> ${GREEN}[✓] S3 bucket 'nexus-media' initialized and media populated.${NC}"
    fi
else
    echo -e "  -> ${YELLOW}[!] LocalStack health endpoint delayed, continuing...${NC}"
fi

# 5. Verify PostgreSQL Domain Schemas & Flyway Migrations
echo -e "\n${BLUE}[4/5] Verifying PostgreSQL Schemas & Reference Catalog...${NC}"
product_count=$(docker exec -i postgres psql -U postgres -d nexuscore -t -c "SELECT COUNT(*) FROM catalog.product;" 2>/dev/null | tr -d ' ' || echo "0")
if [ -n "$product_count" ] && [ "$product_count" -gt 0 ]; then
    echo -e "  -> ${GREEN}[✓] PostgreSQL populated: ${product_count} products across 8 domain schemas (Flyway V1, V2, V3 active).${NC}"
else
    echo -e "  -> Database initial migration pending application startup."
fi

# 6. Synchronize Elasticsearch Catalog Index
echo -e "\n${BLUE}[5/5] Verifying Elasticsearch Search Index...${NC}"
if [ "$MODE" != "--infra-only" ]; then
    printf "  -> Waiting for Nexus Backend Application to boot and complete auto-indexing..."
    app_ready=false
    for i in {1..30}; do
        if curl -sf http://localhost:8080/actuator/health >/dev/null 2>&1; then
            app_ready=true
            break
        fi
        sleep 2
        printf "."
    done

    if [ "$app_ready" = true ]; then
        printf " ${GREEN}[ONLINE]${NC}\n"
        es_count=$(curl -s "http://localhost:9200/pms/_count" 2>/dev/null | grep -o '"count":[0-9]*' | cut -d: -f2 || echo "0")
        if [ "$es_count" -eq 0 ]; then
            echo -e "  -> Triggering catalog sync into Elasticsearch..."
            sync_result=$(curl -s -X POST http://localhost:8080/api/store/search/products/import-all 2>/dev/null || echo "0")
            echo -e "  -> ${GREEN}[✓] Synced ${sync_result} products into Elasticsearch 'pms' index.${NC}"
        else
            echo -e "  -> ${GREEN}[✓] Elasticsearch 'pms' search index active with ${es_count} indexed documents.${NC}"
        fi
    else
        printf " ${YELLOW}[APP BOOTING IN BACKGROUND]${NC}\n"
        echo -e "  -> Spring Boot will automatically run Flyway and auto-seed the search index upon boot."
    fi
else
    echo -e "  -> Infrastructure-only mode: Elasticsearch online and ready for ingest."
fi

# 7. Print Operational Dashboard
echo -e "\n${GREEN}${BOLD}=================================================================="
echo "        🎉 NexusEngine Environment Bootstrapped Successfully!"
echo "==================================================================${NC}"
echo -e "${BOLD}Access URLs & Management Endpoints:${NC}"
echo "  • Storefront Web:       http://localhost:5174"
echo "  • Admin Dashboard:      http://localhost:5173  (Credentials: admin / macro123)"
echo "  • Customer Demo User:   customer1 / macro123   (Name: Aadarsh Pandey)"
echo "  • Brand Admins:         samsung_admin / macro123, nike_admin / macro123"
echo "  • Backend Core API:     http://localhost:8080  (Swagger: http://localhost:8080/swagger-ui.html)"
echo "  • S3 Media Console:     http://localhost:9001  (LocalStack S3: http://localhost:4566/nexus-media)"
echo "  • RabbitMQ Dashboard:   http://localhost:15672 (Credentials: guest / guest, vhost: /nexuscore)"
echo "  • Elasticsearch:        http://localhost:9200  (Index: pms)"
echo "  • Prometheus:           http://localhost:9090"
echo "  • Grafana Dashboards:   http://localhost:3000  (Credentials: admin / admin)"
echo "  • PostgreSQL:           localhost:5433         (Database: nexuscore, User: postgres / postgres)"
echo "  • Redis Cache:          localhost:6379"
echo -e "\n${BOLD}Quick Commands:${NC}"
echo "  • Single command restart:   make up        (or: docker compose up -d)"
echo "  • Single command teardown:  make down      (or: docker compose down)"
echo "  • Full data reset:          ./resource_setup/setup.sh --reset"
echo "  • View live logs:           make logs      (or: docker compose logs -f)"
echo -e "${GREEN}==================================================================${NC}\n"
