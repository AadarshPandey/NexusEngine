.PHONY: setup up down infra-up infra-down ps logs restart clean test test-backend test-frontend format-check help

# Default target
all: help

help:
	@echo "NexusEngine Development Makefile"
	@echo "Usage:"
	@echo "  make setup          - Single-command bootstrap: starts all containers and seeds all data"
	@echo "  make up             - Start full stack in background"
	@echo "  make down           - Stop full stack"
	@echo "  make infra-up       - Start infrastructure only (Postgres, Redis, RabbitMQ, ES, LocalStack)"
	@echo "  make infra-down     - Stop infrastructure"
	@echo "  make ps             - Show container status"
	@echo "  make logs           - Follow container logs"
	@echo "  make restart        - Restart full stack"
	@echo "  make test           - Run backend and frontend test suites"
	@echo "  make test-backend   - Run backend test and verification suite"
	@echo "  make test-frontend  - Run storefront Vitest test suite"
	@echo "  make format-check   - Check backend Spotless code formatting"
	@echo "  make clean          - Stop containers and remove volumes"

setup:
	./resource_setup/setup.sh

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

test-backend:
	cd NexusCore && mvn -B clean verify

test-frontend:
	cd NexusFrontendWeb && npm test

test: test-backend test-frontend

format-check:
	cd NexusCore && mvn -B spotless:check

clean:
	docker compose down -v
