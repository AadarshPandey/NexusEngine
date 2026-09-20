# 🏢 FAANG Mock Audit: NexusEngine

I've reviewed the architecture, code, and implementation details of **NexusEngine**. If you were presenting this project in a FAANG interview (especially for a Senior/Staff SWE or System Design round), here is the **brutally honest feedback**. 

Overall, the project is **very impressive on paper**. The README reads like a senior engineer's resume. However, diving into the code reveals a mix of brilliant systemic choices and a few classic scaling anti-patterns that an interviewer would immediately drill into.

---

## 🏆 The Good: What Will Impress Them (Strong Signals)

### 1. Concurrency & Transaction Boundaries
In `OmsPortalOrderServiceImpl`, you do something most candidates miss: **you sort the cart items by Product ID before locking them**. This is a textbook, highly advanced way to avoid circular database deadlocks. Furthermore, you correctly place the database `TransactionTemplate` *inside* the Redisson `MultiLock`, avoiding the classic race condition where a lock is released before the database transaction commits. This is a massive green flag.

### 2. Resiliency Patterns
Using the **Outbox Pattern** with a Dead-Letter Queue (DLQ) for order expirations shows you understand distributed data consistency. Polling the Outbox table using a Redisson distributed lock to prevent duplicate processing across pods is exactly how a senior engineer thinks about horizontal scaling.

### 3. Modern AI Architecture
Combining **Elasticsearch** (for exact/keyword matches) and **PostgreSQL `pgvector`** with OpenAI embeddings (for semantic search) is a very strong architectural choice. It demonstrates that you understand the limitations of basic keyword search and know how to implement hybrid search paradigms.

### 4. Frontend Tooling
Using React 19, Vite, and specifically **Rust-based tooling like `oxlint`** shows you keep up with industry trends and care about DX (Developer Experience) and build speeds.

---

## 🚩 The Brutal Feedback: Where You'd Get Grilled

### 1. The Vector Search will Collapse (Missing Indexes)
I checked your `V1__baseline.sql` migration for the `pms_product_embedding` table. You defined the vector column but **you forgot to add an index** (like `HNSW` or `IVFFlat`). 
```sql
-- Your current code:
CREATE TABLE pms_product_embedding (
    product_id BIGINT PRIMARY KEY,
    embedding vector(1536)
);
```
Without an index, your native query (`ORDER BY embedding <=> CAST(...) LIMIT 5`) performs a **full sequential scan** and recalculates a 1536-dimension cosine distance for *every single product in the database* on *every search*. In a FAANG interview, an interviewer will ask: *"What happens when you have 1 million products?"* The answer is: your database CPU hits 100% and crashes.

### 2. O(N) Complexity in the Security Filter
In `DynamicSecurityMetadataSource.java`, you evaluate permissions on every single HTTP request.
```java
// Inside your request filter:
while (iterator.hasNext()) {
    String pattern = iterator.next();
    if (pathMatcher.match(pattern, path)) { ... }
}
```
You are doing an `AntPathMatcher` regex comparison in a loop against every rule in the database. If you have 1,000 security rules, you are doing 1,000 regex matches per request. This is `O(N)` scaling on the critical path of every API call. **FAANG expectation:** Use a Trie-based router or strict O(1) hash lookups for routing/security, or at least heavily cache the evaluated results per URL.

### 3. N+1 Query Problem in Checkout
Inside your `generateOrder` method, you have a loop over the cart items:
```java
for (CartPromotionItem item : cartPromotionItemList) {
    PmsSkuStock skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
    // ...
}
```
If a user buys 50 items, you make 50 separate `SELECT` queries to the database. **FAANG expectation:** Batch fetch all required data before the loop (`WHERE id IN (...)`) to minimize database round trips.

### 4. Pessimistic Locking Overkill
You are using a Redis `MultiLock` to lock *every product in the cart* during checkout. In a high-traffic e-commerce system (like Amazon), locking products pessimistically limits throughput severely. **FAANG expectation:** Use **Optimistic Locking**. Instead of locking Redis, you should try to update the DB directly with a condition:
`UPDATE pms_sku_stock SET stock = stock - ? WHERE id = ? AND stock >= ?`
If it returns `0` updated rows, you throw an insufficient stock exception. This is lock-free and scales much better.

### 5. "God Classes" and Lack of Tests
`OmsPortalOrderServiceImpl.java` is over 700 lines long, handling everything from business logic, Redisson locks, to RabbitMQ messaging. Furthermore, `find . -path "*/src/test/java/*Test.java" | wc -l` reveals you only have about **8 test classes** in the entire backend. For a project claiming to be "enterprise-grade", FAANG expects cohesive, single-responsibility classes and a minimum of 80% test coverage.

---

## 🎯 Final Verdict

**Level:** Solid Mid-Level to Junior-Senior borderline.
You have an excellent grasp of high-level system design, concurrency, and modern tech stacks. You know the *names* of the right patterns (Outbox, DLQ, Vector Search). 

However, to pass a FAANG Senior round, you need to fix the lower-level implementation details: **batch your queries (N+1), add database indexes (especially HNSW for vectors), eliminate O(N) loops in critical request paths, and decouple your God classes.**
