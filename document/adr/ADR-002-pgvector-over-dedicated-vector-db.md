# ADR-002: PostgreSQL + pgvector over Dedicated Vector Database

## Status
Accepted

## Context
As part of integrating AI-powered Smart Recommendations into NexusEngine, we need a mechanism to store product embeddings (generated via OpenAI's `text-embedding-ada-002`) and perform similarity searches using cosine distance. 

The industry standard for Retrieval-Augmented Generation (RAG) and embedding storage includes dedicated vector databases such as Pinecone, Weaviate, Milvus, and Qdrant. However, our primary operational datastore is already PostgreSQL.

## Decision
We will use the **PostgreSQL `pgvector` extension** to store vector embeddings and compute similarity, rather than introducing a dedicated, standalone vector database into our technology stack.

## Rationale / Trade-offs
- **Fewer Moving Parts:** By using PostgreSQL, we eliminate the need to provision, monitor, and secure an entirely new infrastructure component. We also avoid the overhead of syncing data between our transactional database and an external vector database.
- **ACID Transactions:** Storing embeddings alongside product data in PostgreSQL allows us to maintain strict transactional consistency. If a product is deleted or updated, its embedding can be updated or cascaded in the same transaction.
- **Query Flexibility:** `pgvector` allows us to perform hybrid searches seamlessly. We can write a single SQL query that filters on structured metadata (e.g., `WHERE delete_status = 0 AND vendor_id = 1`) while simultaneously ordering by vector similarity (`ORDER BY embedding <=> ?`). Achieving this with Pinecone would require complex application-side joins or aggressive data duplication.
- **Performance:** For our current scale (tens of thousands of products), `pgvector` provides near-instantaneous cosine distance computations utilizing HNSW (Hierarchical Navigable Small World) indexes.

## Consequences
- **Positive:** Reduced infrastructure cost, simplified deployment (added via a single Docker image change), and maintained referential integrity.
- **Negative:** If the number of embeddings grows into the hundreds of millions, `pgvector` may eventually hit scaling bottlenecks on CPU and RAM compared to a distributed, purpose-built vector database. We accept this trade-off for the current phase of the product lifecycle.
