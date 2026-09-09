# NexusEngine

NexusEngine is a modern, enterprise-grade e-commerce microservices and monolith hybrid platform designed as a scalable foundation for retail applications. 

## 🏗 Architecture

NexusEngine is structured as a robust Spring Boot application with a React-based customer portal and admin panel.

### Backend Stack
- **Framework**: Java 17, Spring Boot 3.x, Spring Data JPA
- **Database**: PostgreSQL with pgvector for AI semantic search capabilities
- **Cache & Message Broker**: Redis, RabbitMQ
- **Search Engine**: Elasticsearch
- **Object Storage**: MinIO (S3-compatible)
- **AI Integration**: Spring AI (OpenAI)
- **Resilience**: Resilience4j (Circuit Breaker, Rate Limiter)

### Frontend Stack
- **Customer Portal**: React 19, TypeScript, Vite, Redux Toolkit
- **Admin Panel**: React 19, TypeScript, Vite, MUI v6, Recharts

### DevOps & Observability
- **Containerization**: Docker & Docker Compose
- **Monitoring**: Prometheus, Grafana, Spring Boot Actuator
- **CI/CD**: GitHub Actions (Build, Test, Push to ECR)

## 🚀 Getting Started

### Prerequisites
- Docker and Docker Compose
- Node.js >= 20
- Java 17 (if running outside Docker)
- Maven

### Running with Docker Compose
The easiest way to start the entire stack (infrastructure + applications) is using Docker Compose.

```bash
# Start infrastructure and application services
docker-compose up -d
```

Services will be available at:
- **Customer Portal (Frontend)**: http://localhost:5174
- **Admin Panel**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Search API**: http://localhost:8081
- **Grafana (Monitoring)**: http://localhost:3000 (admin/admin)
- **MinIO Console**: http://localhost:9001 (minioadmin/minioadmin)

### Local Development

**1. Start Infrastructure**
```bash
docker-compose up -d postgres redis mongodb rabbitmq elasticsearch minio
```

**2. Start Backend APIs**
```bash
cd NexusCore
./mvnw clean install -DskipTests
java -jar nexus-application/target/nexus-application.jar
java -jar nexus-search/target/nexus-search.jar
```

**3. Start Frontend Portal**
```bash
cd NexusFrontendWeb
npm install
npm run dev
```

**4. Start Admin Panel**
```bash
cd NexusAdminPanel
npm install
npm run dev
```

## 🔐 Key Features
- Complete user registration and JWT-based authentication
- Product browsing, cart management, and checkout
- Payment integration (Razorpay / Stripe)
- Intelligent product recommendations via Spring AI & pgvector
- Order management and refund processing
- Role-based Admin Dashboard with real-time metrics

## 📈 Monitoring
NexusEngine includes a fully configured observability stack out-of-the-box. Metrics from the Spring Boot application are scraped by Prometheus and visualized in Grafana.

---
*NexusEngine is a portfolio project demonstrating scalable system design and modern full-stack development practices.*
