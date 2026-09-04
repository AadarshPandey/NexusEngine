# NexusEngine - Core Backend Service

![Java](https://img.shields.io/badge/Java-21-blue.svg?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791.svg?logo=postgresql)

This repository contains the containerized core backend service for **NexusEngine**, a high-performance, modular monolithic e-commerce engine designed for enterprise scale. 

## 🔗 Official Repository
The full source code, extensive architecture documentation, and issue tracker can be found on GitHub:  
**[https://github.com/AadarshPandey/NexusEngine](https://github.com/AadarshPandey/NexusEngine)**

---

## 🌟 Architectural Overview

NexusEngine employs a **Modular Monolith** architecture. By strictly enforcing logical domains (like `nexus-portal`, `nexus-admin`, `nexus-security`, and `nexus-common`) using Maven multi-module structures, the application maintains the pristine separation of concerns found in microservices, without suffering from network latency or complex distributed deployment pipelines.

The `nexus-engine-backend` image encompasses all core business operations:
- **Order Management System (OMS):** Handles checkout flows, shopping carts, and return applications. Implements the **Transactional Outbox Pattern** to guarantee eventual consistency across RabbitMQ messages.
- **Product Management System (PMS):** Manages product variants, SKUs, and categories.
- **User Management System (UMS):** Handles role-based access control (RBAC), JWT authentication, and user data.
- **Flash Sales (SMS):** Handles extreme high-concurrency events using **Redisson Distributed Locks** to strictly prevent inventory overselling.

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5, JDK 21
- **Database**: PostgreSQL 17 (accessed via Spring Data JPA)
- **Caching & Locks**: Redis, Redisson
- **Security**: Spring Security + JWT
- **Observability**: Micrometer, OpenTelemetry, Grafana
- **Fault Tolerance**: Resilience4j Circuit Breakers, Bucket4j Rate Limiting

## 🚀 Quick Start (Docker Compose)

The easiest way to run the backend is via Docker Compose alongside the required infrastructure (PostgreSQL, Redis, RabbitMQ, Elasticsearch, MinIO) and the frontend services.

Download the `docker-compose.yml` from the [GitHub Repository](https://github.com/AadarshPandey/NexusEngine) and simply run:

```bash
docker compose up -d
```

### 🔑 Environment Variables

To fully utilize the mock Razorpay checkout flow, you must supply the following environment variables (or place them in a `.env` file at the root of the project):

- `RAZORPAY_KEY_ID`: Your Razorpay Test Key ID
- `RAZORPAY_KEY_SECRET`: Your Razorpay Test Secret

## 🤝 Open Source Acknowledgements
This project was highly inspired by the incredible open-source architecture of the `macrozheng/mall` project, heavily adapted and modernized for this specific deployment.
