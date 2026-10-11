.PHONY: up down infra-up infra-down ps logs restart clean help

# Default target
all: help

help:
	@echo "NexusEngine Development Makefile"
	@echo "Usage:"
	@echo "  make up          - Start full stack in background"
	@echo "  make down        - Stop full stack"
	@echo "  make infra-up    - Start infrastructure only (Postgres, Redis, RabbitMQ, ES, LocalStack)"
	@echo "  make infra-down  - Stop infrastructure"
	@echo "  make ps          - Show container status"
	@echo "  make logs        - Follow container logs"
	@echo "  make restart     - Restart full stack"
	@echo "  make clean       - Stop containers and remove volumes"

up:
	docker compose up -d

down:
	docker compose down

infra-up:
	docker compose up -d postgres redis rabbitmq elasticsearch localstack prometheus grafana

infra-down:
	docker compose stop postgres redis rabbitmq elasticsearch localstack prometheus grafana

ps:
	docker compose ps

logs:
	docker compose logs -f

restart: down up

clean:
	docker compose down -v
