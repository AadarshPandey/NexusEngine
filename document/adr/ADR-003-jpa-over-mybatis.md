# ADR-003: Spring Data JPA over MyBatis

## Status
Accepted

## Context
The legacy NexusEngine platform utilized MyBatis as its primary ORM, heavily relying on XML mappers and `mall-mbg` (MyBatis Generator) for standard CRUD operations. While MyBatis offers granular control over SQL, it resulted in a massive footprint of boilerplate XML and tedious maintenance whenever database schemas changed.

As part of the modernization effort to Spring Boot 3.5, we needed to evaluate whether to upgrade the MyBatis dependencies or switch entirely to Spring Data JPA / Hibernate.

## Decision
We will completely migrate from **MyBatis** to **Spring Data JPA** (backed by Hibernate) as our primary ORM and data access layer.

## Rationale / Trade-offs
- **Developer Productivity:** Spring Data JPA drastically reduces boilerplate by automatically generating queries based on interface method names (e.g., `findByMemberIdAndStatus`). This allows developers to focus on business logic rather than writing repetitive XML mappings.
- **Maintainability:** Entity relationships are mapped directly in Java code using annotations (`@OneToMany`, `@ManyToOne`), keeping the data model definitions tightly coupled with the code rather than dispersed across disconnected XML files.
- **Database Agnosticism:** Hibernate provides an abstraction over the underlying SQL dialect. While we are currently using PostgreSQL, relying on JPA makes it significantly easier to migrate datastores (or test against in-memory H2 databases) in the future.
- **Trade-off - SQL Control:** MyBatis provides absolute control over the exact SQL executed. JPA, conversely, abstracts the SQL generation, which can sometimes lead to the N+1 select problem or inefficient queries if not carefully tuned.

## Consequences
- **Positive:** We successfully deleted thousands of lines of XML mapper files and boilerplate, resulting in a cleaner, more readable, and faster-compiling codebase. Rapid prototyping and schema evolution are significantly smoother.
- **Negative:** Developers must be vigilant about understanding JPA entity lifecycles (detached vs managed entities) and carefully utilize `@EntityGraph` or customized JPQL/native queries to avoid N+1 query performance hits on complex joins. We fallback to `@Query(nativeQuery = true)` (as seen in our pgvector implementation) when Hibernate abstractions fall short.
