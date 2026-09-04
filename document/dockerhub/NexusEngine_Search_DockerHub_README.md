# NexusEngine - Search Service

![Java](https://img.shields.io/badge/Java-21-blue.svg?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg?logo=spring)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8-005571.svg?logo=elasticsearch)

This repository contains the dedicated **Search Microservice** for **NexusEngine**, a high-performance, enterprise-grade e-commerce engine. 

## 🔗 Official Repository
The full source code, extensive architecture documentation, and issue tracker can be found on GitHub:  
**[https://github.com/AadarshPandey/NexusEngine](https://github.com/AadarshPandey/NexusEngine)**

---

## 🌟 Why a Dedicated Search Service?

In the NexusEngine architecture, the core backend (`nexus-engine-backend`) is optimized for processing high-concurrency transactions (like payments and flash sales) via PostgreSQL.

Search indexing, particularly in an e-commerce platform with tens of thousands of SKUs and attributes, is computationally expensive. If the core monolith was forced to index product data into Elasticsearch synchronously during every API call, it would result in massive performance bottlenecks.

To eliminate this, NexusEngine completely decouples search logic into this standalone `nexus-engine-search` service:
1. When a product is created or updated in the core backend, it fires an **asynchronous event** over **RabbitMQ**.
2. This Search Service acts as an independent consumer, picking up those messages and writing the data into Elasticsearch in the background.
3. Customer product searches are routed directly to this service, ensuring lightning-fast semantic queries that do not burden the transactional database.

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5, JDK 21
- **Search Engine**: Elasticsearch
- **Data Access**: Spring Data Elasticsearch
- **Message Broker**: RabbitMQ

## 🚀 Quick Start (Docker Compose)

This service is designed to run in tandem with the core monolith and the rest of the infrastructure (Elasticsearch, PostgreSQL, RabbitMQ, Redis).

Download the `docker-compose.yml` from the [GitHub Repository](https://github.com/AadarshPandey/NexusEngine) and simply run:

```bash
docker compose up -d
```

## 🤝 Open Source Acknowledgements
This project was highly inspired by the incredible open-source architecture of the `macrozheng/mall` project, heavily adapted and modernized for this specific deployment.
