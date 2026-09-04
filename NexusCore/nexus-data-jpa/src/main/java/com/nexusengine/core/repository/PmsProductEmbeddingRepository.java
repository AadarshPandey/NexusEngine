package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductEmbedding;
import com.pgvector.PGvector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PmsProductEmbeddingRepository extends JpaRepository<PmsProductEmbedding, Long> {

    @Query(value = "SELECT product_id FROM pms_product_embedding ORDER BY embedding <=> CAST(:vector AS vector) LIMIT :limit", nativeQuery = true)
    List<Long> findNearestProducts(@Param("vector") String vector, @Param("limit") int limit);
}
