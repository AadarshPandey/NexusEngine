NexusEngine - System Design & Architecture

1. High-Level Architecture Overview
NexusEngine is a modern, AI-augmented e-commerce platform built on a Modular Monolith architecture. It is designed to handle high concurrency, provide advanced vector-based semantic search, and enforce strict domain boundaries without the operational complexity and network latency of microservices.

Why a Modular Monolith?
Instead of a distributed microservices approach, NexusEngine utilizes Maven multi-module boundaries to logically isolate domains (nexus-portal, nexus-admin, nexus-security, nexus-search, nexus-ai). This approach reduces deployment complexity and network hop latency while allowing seamless migration to microservices in the future if extreme scaling is required.

1. Technology Stack
Core Application: Java 21, Spring Boot 3.5
Database: PostgreSQL 17 (Primary source of truth)
Vector Engine: pgvector extension for PostgreSQL
Caching & Distributed Coordination: Redis
Message Broker: RabbitMQ
AI Framework: Spring AI (OpenAI API integration)
Containerization: Docker & Testcontainers (Integration Testing)
2. Core Architectural Patterns
3.1 High-Concurrency Distributed Locking (Flash Sales)
During high-traffic events (e.g., flash sales), multiple users may attempt to purchase the same limited-stock item simultaneously, leading to race conditions.

Implementation: NexusEngine uses Redisson to implement distributed Mutex locks.
Workflow: When generateOrder is invoked, the system dynamically constructs an RLock (MultiLock) for all productIds in the user's cart. The transaction strictly prevents phantom reads and overselling across all active JVM instances by locking the inventory records in Redis before database mutation.
3.2 Eventual Consistency via Transactional Outbox Pattern
Cross-domain communication (e.g., placing an order requiring an asynchronous delay message for order cancellation/payment timeout) must be resilient to message broker failures.

Implementation: Instead of pushing to RabbitMQ synchronously (which risks transaction rollback if the broker is down), the order service persists a PENDING OutboxEvent to PostgreSQL within the same ACID transaction as the order creation.
Background Worker: A Spring @Scheduled processor asynchronously polls for PENDING events, pushes them to RabbitMQ, and marks them SENT, guaranteeing At-Least-Once Delivery and zero data loss during network partitions.
3.3 Distributed API Rate Limiting
To prevent abuse, bot attacks, and DDoS attempts across a distributed cluster, NexusEngine employs a global rate limiter.

Implementation: Using Redisson RRateLimiter backed by Redis.
Workflow: The RateLimitInterceptor checks incoming requests (by IP for unauthenticated routes, and by Username for authenticated routes) against a centralized Redis token bucket. It ensures strict limits (e.g., 100 requests/min per IP, 10 checkout requests/min per user) across all load-balanced application instances.
3.4 AI-Powered Semantic Search (RAG)
Standard keyword search fails to capture user intent. NexusEngine implements intelligent Vector Search.

Implementation: Spring AI integrates with OpenAI's embedding models (text-embedding-3-small) to convert product descriptions into high-dimensional vectors.
Storage: Vectors are stored in PostgreSQL using the pgvector extension. Incoming user search queries are embedded and compared using Cosine Similarity to return highly relevant, semantically matching products.
4. Module Boundaries & Responsibilities
The monolith is logically segmented into independent Maven modules:

Module Responsibility
nexus-common Global exceptions, Swagger/OpenAPI configs, Redis components, Base DTOs.
nexus-security JWT generation, filter chains, dynamic authorization managers.
nexus-data-jpa Spring Data JPA entities, repositories, and database configuration.
nexus-admin Backend management APIs (Products, Users, CMS), robust RBAC implementation.
nexus-portal Customer-facing storefront APIs (Cart, Checkout, Payment, Outbox processor, Semantic Search endpoints).
nexus-search Modern Elasticsearch engine for fast product keyword indexing and faceted search, integrated via Spring Data Elasticsearch.
nexus-ai Embeddings generation, LLM communication, pgvector integration (Modern Search Engine).
nexus-application The main Spring Boot entry point that orchestrates and boots the modules.
5. Security & Authorization (RBAC)
NexusEngine utilizes a stateless JWT (JSON Web Token) architecture.

Authentication: Verifies credentials and issues a JWT with claims.
Authorization: nexus-admin utilizes a dynamic Role-Based Access Control (RBAC) matrix. Permissions are loaded from the database and evaluated at runtime using a custom DynamicAuthorizationManager, allowing granular control over which admin roles can access specific API paths.
6. Testing Strategy
NexusEngine follows a rigorous enterprise testing strategy:

Unit Testing: Using JUnit 5 and Mockito to isolate business logic.
Integration Testing: Utilizes Testcontainers to dynamically spin up Dockerized instances of PostgreSQL (pgvector), Redis, and RabbitMQ. This ensures tests run against real infrastructure without requiring host-machine configuration, preventing "it works on my machine" bugs.
7. Future Scalability Path
While currently a Modular Monolith, the system is designed for easy extraction:

Database Sharding: As the OmsOrder tables grow, they can be horizontally partitioned or sharded by memberId.
Microservices Extraction: If the nexus-portal experiences significantly higher load than nexus-admin, the Maven modules can be easily separated into independent Spring Boot applications communicating via REST or gRPC, utilizing Spring Cloud Gateway for routing.
How To Run
Viewed docker-compose.yml:1-60

Here is the step-by-step guide to running the complete NexusEngine platform on your local machine.

Since this is a full-stack e-commerce platform with microservice-level infrastructure, I recommend running the infrastructure (Databases, Redis, RabbitMQ) in Docker, and running the Backend and Frontend code directly on your machine so you can easily edit and debug.

Step 1: Start the Infrastructure (Docker)
You need to spin up the required databases and message brokers. Run this command from the root of your project (/home/aadarsh/Documents/NexusEngine) to start only the infrastructure components:

docker compose up -d postgres redis mongodb rabbitmq elasticsearch minio
Note: Wait a few seconds for all containers (especially Elasticsearch and RabbitMQ) to fully start up and become healthy.

Step 2: Start the Backend (Spring Boot 3)
The backend is a Maven multi-module project. The main entry point that boots the entire modular monolith is nexus-application.

Navigate to the backend application directory:

cd NexusCore/nexus-application
Run the main application using the dev profile (which connects to your localhost docker containers):

mvn spring-boot:run -Dspring-boot.run.profiles=dev
Your backend is now running at <http://localhost:8080>. You can view the Swagger API documentation at <http://localhost:8080/swagger-ui/index.html>.

Step 3: Start the Admin Dashboard (React)
The admin panel is where you manage products, orders, and users.

Open a new terminal tab and navigate to the admin panel:

cd NexusAdminPanel
Install dependencies and start the development server:

npm install
npm run dev
The admin panel will typically open at <http://localhost:5173> (or port 3000 depending on Vite/CRA).

Step 4: Start the Customer Storefront (React)
This is the customer-facing e-commerce web application.

Open a new terminal tab and navigate to the storefront:

cd NexusFrontendWeb
Install dependencies and start the development server:

npm install
npm run dev
Alternative: Run EVERYTHING via Docker (Production Simulation)
If you don't want to run the code locally and just want to see the whole system boot up automatically as if it were deployed on a server, you can simply run:

docker compose up --build -d
This will build the .jar files, package them into Docker images, and spin up the backend alongside the infrastructure.

Let me know if you run into any port conflicts or connection errors when starting it up!

1. Directory Structure
./
    ├── document/
        ├── adr/
    ├── NexusFrontendWeb/
        ├── src/
            ├── layout/
            ├── router/
            ├── assets/
            ├── utils/
            ├── store/
                ├── slices/
            ├── views/
            ├── components/
            ├── tests/
            ├── api/
        ├── public/
    ├── .vscode/
    ├── NexusAdminPanel/
        ├── src/
            ├── apis/
            ├── router/
            ├── theme/
            ├── assets/
                ├── images/
            ├── types/
            ├── pages/
                ├── oms/
                ├── ums/
                ├── sms/
                ├── pms/
            ├── utils/
            ├── store/
                ├── slices/
            ├── styles/
            ├── components/
            ├── layouts/
                ├── components/
        ├── public/
            ├── tinymce6.8.6/
                ├── plugins/
                    ├── nonbreaking/
                    ├── quickbars/
                    ├── emoticons/
                        ├── js/
                    ├── help/
                        ├── js/
                            ├── i18n/
                                ├── keynav/
                    ├── preview/
                    ├── directionality/
                    ├── fullscreen/
                    ├── accordion/
                    ├── visualchars/
                    ├── image/
                    ├── pagebreak/
                    ├── importcss/
                    ├── autosave/
                    ├── searchreplace/
                    ├── save/
                    ├── lists/
                    ├── insertdatetime/
                    ├── autoresize/
                    ├── advlist/
                    ├── template/
                    ├── table/
                    ├── charmap/
                    ├── anchor/
                    ├── media/
                    ├── wordcount/
                    ├── link/
                    ├── codesample/
                    ├── autolink/
                    ├── code/
                    ├── visualblocks/
                ├── themes/
                    ├── silver/
                ├── langs/
                ├── icons/
                    ├── default/
                ├── skins/
                    ├── content/
                        ├── document/
                        ├── tinymce-5-dark/
                        ├── writer/
                        ├── tinymce-5/
                        ├── dark/
                        ├── default/
                    ├── ui/
                        ├── tinymce-5-dark/
                        ├── tinymce-5/
                        ├── oxide-dark/
                        ├── oxide/
                ├── models/
                    ├── dom/
    ├── NexusCore/
        ├── document/
            ├── elk/
            ├── otel/
            ├── docker/
            ├── sql/
            ├── postman/
        ├── nexus-portal/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── portal/
                                        ├── dao/
                                        ├── component/
                                        ├── controller/
                                        ├── config/
                                        ├── service/
                                            ├── impl/
                                        ├── domain/
                                        ├── util/
                                        ├── repository/
                    ├── resources/
                ├── test/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── portal/
                                        ├── slice/
                                        ├── integration/
                                        ├── unit/
        ├── nexus-application/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── application/
                                        ├── config/
                    ├── resources/
        ├── nexus-admin/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── component/
                                    ├── controller/
                                    ├── config/
                                    ├── service/
                                        ├── impl/
                                    ├── dto/
                                    ├── validator/
                                    ├── bo/
                    ├── resources/
                        ├── META-INF/
                ├── test/
                    ├── com/
                        ├── macro/
                            ├── mall/
        ├── .vscode/
        ├── nexus-search/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── search/
                                        ├── dao/
                                        ├── component/
                                        ├── controller/
                                        ├── config/
                                        ├── service/
                                            ├── impl/
                                        ├── domain/
                                        ├── repository/
                    ├── resources/
                ├── test/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── search/
        ├── nexus-ai/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── ai/
                                        ├── config/
                                        ├── service/
                                        ├── dto/
        ├── nexus-security/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── security/
                                        ├── component/
                                        ├── config/
                                        ├── aspect/
                                        ├── util/
                                        ├── annotation/
        ├── nexus-data-jpa/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── model/
                                    ├── repository/
        ├── nexus-common/
            ├── src/
                ├── main/
                    ├── java/
                        ├── com/
                            ├── nexusengine/
                                ├── core/
                                    ├── common/
                                        ├── exception/
                                        ├── config/
                                        ├── service/
                                            ├── impl/
                                        ├── domain/
                                        ├── log/
                                        ├── api/
                                        ├── util/
                    ├── resources/
    ├── .github/
        ├── workflows/
