package com.nexusengine.core.domain.ai.repository;

import com.nexusengine.core.domain.ai.entity.ProductEmbedding;
import com.pgvector.PGvector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductEmbeddingRepository extends JpaRepository<ProductEmbedding, Long> {

    @Query(value = "SELECT product_id FROM ai.pms_product_embedding ORDER BY embedding <=> CAST(:vector AS vector) LIMIT :limit", nativeQuery = true)
    List<Long> findNearestProducts(@Param("vector") String vector, @Param("limit") int limit);
}
