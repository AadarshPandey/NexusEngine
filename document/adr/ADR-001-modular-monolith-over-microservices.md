# ADR-001: Modular Monolith over Microservices

## Status
Accepted

## Context
As we modernize the NexusEngine (formerly "mall") e-commerce platform, we need to decide on the deployment and architectural boundary model. The original system was a single monolithic application. Modern trends heavily favor microservices to allow independent scaling, language flexibility, and decoupled deployments.

However, microservices introduce significant distributed systems complexity:
- Network latency between services
- Complex distributed transactions (Sagas, 2PC)
- Heavier operational overhead (Kubernetes, service meshes, distributed tracing)
- Difficult local development environments

NexusEngine currently serves as a comprehensive e-commerce backend with strongly cohesive domains (Orders, Products, Members, Marketing).

## Decision
We will adopt a **Modular Monolith** architecture instead of migrating to **Microservices**.

We will enforce strict logical boundaries at the module level (e.g., `nexus-admin`, `nexus-portal`, `nexus-search`, `nexus-ai`) using Maven multi-module features, but deploy them together as a single executable artifact (`nexus-application.jar`).

## Rationale / Trade-offs
- **Operational Simplicity:** A single deployment unit is vastly easier to operate, monitor, and deploy on AWS via standard auto-scaling groups or simple ECS tasks, reducing infrastructure costs.
- **Transactional Integrity:** We can rely on standard ACID transactions (`@Transactional`) across domains rather than dealing with eventual consistency or distributed Sagas (e.g., when an order is placed and stock must be decremented).
- **Refactoring Safety:** Refactoring boundaries between modules is just a standard Java method call change, ensuring compile-time safety, whereas changing microservice APIs requires versioning and backward compatibility.
- **When to split:** We will only extract a module into a microservice when:
  1. The team size working exclusively on that domain exceeds 3-5 engineers.
  2. A specific module has drastically different scaling requirements than the rest of the application (e.g., if the AI recommendation engine requires GPU-heavy scaling independent of the order processing API).

## Consequences
- **Positive:** Faster time-to-market, easier debugging, simpler deployment pipeline, and lower infrastructure cost.
- **Negative:** A bug or memory leak in one module (e.g., `mall-ai`) can bring down the entire application. We mitigate this using isolated thread pools and Resilience4j bulkheads where appropriate. All code must be deployed together; we cannot independently deploy just the frontend API.
